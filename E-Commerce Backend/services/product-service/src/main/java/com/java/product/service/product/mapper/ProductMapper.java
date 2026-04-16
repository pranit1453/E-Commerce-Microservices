package com.java.product.service.product.mapper;

import com.java.product.service.product.dto.*;
import com.java.product.service.product.entity.Product;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "price", source = "price")
    Product mappedToProduct(final CreateProductRequest request);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "productId", source = "productId")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "price", source = "price")
    CreateProductResponse mappedToCreateProductResponse(final Product product);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "price", source = "price")
    Product mappedToProduct(final UpdateProductRequest request);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "productId", source = "productId")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "price", source = "price")
    UpdateProductResponse mappedToUpdateProductResponse(final Product product);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patchProduct(final UpdateProductRequest request, @MappingTarget final Product product);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "productId", source = "productId")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "price", source = "price")
    ProductResponse mappedToProductResponse(final Product product);
}
