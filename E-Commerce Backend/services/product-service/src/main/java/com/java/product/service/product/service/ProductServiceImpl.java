package com.java.product.service.product.service;

import com.java.product.service.category.entity.Category;
import com.java.product.service.category.repository.CategoryRepository;
import com.java.product.service.exception.custom.DuplicateResourceFoundException;
import com.java.product.service.exception.custom.ResourceNotFoundException;
import com.java.product.service.product.dto.*;
import com.java.product.service.product.entity.Product;
import com.java.product.service.product.enums.ProductSortField;
import com.java.product.service.product.mapper.ProductMapper;
import com.java.product.service.product.repository.ProductRepository;
import com.java.product.service.product.specification.ProductSpecification;
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

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Validated
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public Set<CreateProductResponse> createProductInBulk(final Set<CreateProductRequest> request) {
        // Collect all category IDs from request
        Set<UUID> categoryIds = request.stream()
                .map(CreateProductRequest::categoryId)
                .collect(Collectors.toSet());

        // Fetch all categories in ONE query (avoid N+1)
        Map<UUID, Category> categoryMap = categoryRepository.findAllById(categoryIds)
                .stream()
                .collect(Collectors.toMap(Category::getCategoryId, c -> c));

        // Validate all categories exist
        Set<UUID> missingCategories = categoryIds.stream()
                .filter(id -> !categoryMap.containsKey(id))
                .collect(Collectors.toSet());

        if (!missingCategories.isEmpty()) {
            throw new ResourceNotFoundException("Category not found: " + missingCategories);
        }

        Set<String> seen = new HashSet<>();
        List<Product> products = request.stream()
                .map(req -> {
                    String name = req.name().trim().toLowerCase();
                    String key = name + "_" + req.categoryId();

                    if (!seen.add(key)) {
                        throw new DuplicateResourceFoundException("Duplicate product in request: " + name);
                    }

                    Product product = productMapper.mappedToProduct(req);
                    product.setCategory(categoryMap.get(req.categoryId()));
                    return product;
                })
                .toList();

        List<Product> savedProducts = productRepository.saveAll(products);
        return savedProducts.stream()
                .map(productMapper::mappedToCreateProductResponse)
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public CreateProductResponse createProduct(final CreateProductRequest request) {

        Category category = categoryRepository.findByCategoryId(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + request.categoryId()));

        Product product = productMapper.mappedToProduct(request);
        product.setCategory(category);
        Product savedProduct = productRepository.save(product);

        return productMapper.mappedToCreateProductResponse(savedProduct);
    }

    @Override
    @Transactional
    public UpdateProductResponse updateProduct(final UUID id, final UpdateProductRequest request) {
        Product product = findByProductId(id);
        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());

        Product savedProduct = productRepository.save(product);
        return productMapper.mappedToUpdateProductResponse(savedProduct);
    }

    @Override
    @Transactional
    public UpdateProductResponse patchProduct(UUID id, UpdateProductRequest request) {
        Product product = findByProductId(id);
        productMapper.patchProduct(request, product);
        Product savedProduct = productRepository.save(product);
        return productMapper.mappedToUpdateProductResponse(savedProduct);
    }

    @Override
    @Transactional
    public void deleteProductById(final UUID id) {
        Product product = findByProductId(id);
        Category category = product.getCategory();
        if (category != null) {
            category.removeProduct(product);
        }

        productRepository.delete(product);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse fetchProductById(final UUID id) {
        Product product = findByProductId(id);
        return productMapper.mappedToProductResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ProductResponse> fetchProductsInPage(
            final int page,
            final int size,
            final String keyword,
            final ProductSortField sortBy,
            final String sortDirection
    ) {
        Sort sort = sortDirection.equalsIgnoreCase("ASC")
                ? Sort.by(Sort.Direction.ASC, sortBy.getField())
                : Sort.by(Sort.Direction.DESC, sortBy.getField());

        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<Product> specification = ProductSpecification.searchKeyword(keyword);
        Page<Product> pages = productRepository.findAll(specification, pageable);
        List<ProductResponse> content = pages.getContent()
                .stream()
                .map(productMapper::mappedToProductResponse)
                .toList();
        return PageResponse.<ProductResponse>builder()
                .content(content)
                .page(pages.getNumber())
                .size(pages.getSize())
                .totalElements(pages.getTotalElements())
                .totalPages(pages.getTotalPages())
                .last(pages.isLast())
                .build();
    }

    private Product findByProductId(final UUID id) {
        return productRepository.findByProductId(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product with id: " + id + " not found"));
    }
}
