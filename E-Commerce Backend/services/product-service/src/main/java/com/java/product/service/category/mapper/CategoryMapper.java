package com.java.product.service.category.mapper;

import com.java.product.service.category.dto.*;
import com.java.product.service.category.entity.Category;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    Category mappedToCategory(final CreateCategoryRequest request);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "categoryId", source = "categoryId")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    CreateCategoryResponse mappedToCategoryResponse(final Category category);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "categoryId", source = "categoryId")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    UpdateCategoryResponse mappedToUpdateCategoryResponse(final Category category);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patchCategory(final UpdateCategoryRequest request, @MappingTarget Category category);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "categoryId", source = "categoryId")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    CategoryResponse mappedToResponse(final Category category);
}
