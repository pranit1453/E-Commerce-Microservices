package com.java.product.service.category.service;

import com.java.product.service.category.dto.*;
import com.java.product.service.category.entity.Category;
import com.java.product.service.category.enums.CategorySortField;
import com.java.product.service.category.mapper.CategoryMapper;
import com.java.product.service.category.repository.CategoryRepository;
import com.java.product.service.category.specification.CategorySpecification;
import com.java.product.service.exception.custom.DuplicateResourceFoundException;
import com.java.product.service.exception.custom.ResourceConflictException;
import com.java.product.service.exception.custom.ResourceNotFoundException;
import com.java.product.service.product.repository.ProductRepository;
import com.java.product.service.wrapper.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Validated
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public Set<CreateCategoryResponse> createCategoriesInBulk(Set<CreateCategoryRequest> request) {
        Set<String> names = request.stream()
                .map(CreateCategoryRequest::name)
                .collect(Collectors.toSet());

        Set<String> existingNames = categoryRepository.findExistingNames(names);

        if (!existingNames.isEmpty()) {
            throw new DuplicateResourceFoundException("Categories already exist: " + existingNames);
        }
        List<Category> categories = request.stream()
                .map(categoryMapper::mappedToCategory)
                .collect(Collectors.toList());

        List<Category> savedCategories = categoryRepository.saveAll(categories);
        return savedCategories.stream()
                .map(categoryMapper::mappedToCategoryResponse)
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public CreateCategoryResponse createCategory(final CreateCategoryRequest request) {
        if (categoryRepository.findByName(request.name())) {
            throw new DuplicateResourceFoundException("Category with name " + request.name() + " already exist");
        }
        Category category = categoryMapper.mappedToCategory(request);
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.mappedToCategoryResponse(savedCategory);
    }

    @Override
    @Transactional
    public UpdateCategoryResponse updateCategoryById(final UUID id, final UpdateCategoryRequest request) {
        Category category = findByCategoryId(id);
        category.setName(request.name());
        category.setDescription(request.description());
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.mappedToUpdateCategoryResponse(savedCategory);
    }

    @Override
    @Transactional
    public UpdateCategoryResponse patchCategoryById(final UUID id, final UpdateCategoryRequest request) {
        Category category = findByCategoryId(id);
        categoryMapper.patchCategory(request, category);
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.mappedToUpdateCategoryResponse(savedCategory);
    }

    @Override
    @Transactional
    public void deleteCategoryById(final UUID id) {
        Category category = findByCategoryId(id);
        if (productRepository.existsByCategory_CategoryId(id)) {
            throw new ResourceConflictException("Products are associated with this category");
        }
        categoryRepository.delete(category);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse fetchCategoryById(final UUID id) {
        Category category = findByCategoryId(id);
        return categoryMapper.mappedToResponse(category);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<CategoryResponse> fetchCategoriesInPage(
            final int page,
            final int size,
            final String keyword,
            final CategorySortField sortBy,
            final String sortDirection) {

        Sort sort = sortDirection.equalsIgnoreCase("ASC")
                ? Sort.by(Sort.Direction.ASC, sortBy.getField())
                : Sort.by(Sort.Direction.DESC, sortBy.getField());

        Pageable pageable = PageRequest.of(page, size, sort);
        Specification<Category> specification = CategorySpecification.searchKeyword(keyword);

        Page<Category> pages = categoryRepository.findAll(specification, pageable);
        List<CategoryResponse> content = pages.getContent()
                .stream()
                .map(categoryMapper::mappedToResponse)
                .toList();

        return PageResponse.<CategoryResponse>builder()
                .content(content)
                .page(pages.getNumber())
                .size(pages.getSize())
                .totalElements(pages.getTotalElements())
                .totalPages(pages.getTotalPages())
                .last(pages.isLast())
                .build();
    }

    private Category findByCategoryId(final UUID id) {
        return categoryRepository.findByCategoryId(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category with id " + id + " not found"));
    }


}
