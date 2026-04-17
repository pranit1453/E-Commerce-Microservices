package com.java.product.service.product.service;

import com.java.product.service.product.dto.*;
import com.java.product.service.product.enums.ProductSortField;
import com.java.product.service.wrapper.PageResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.Set;
import java.util.UUID;

public interface ProductService {
    Set<CreateProductResponse> createProductInBulk(Set<@Valid CreateProductRequest> request);

    CreateProductResponse createProduct(@Valid CreateProductRequest request);

    UpdateProductResponse updateProduct(@NotNull UUID id, @Valid UpdateProductRequest request);

    UpdateProductResponse patchProduct(@NotNull UUID id, @Valid UpdateProductRequest request);

    void deleteProductById(@NotNull UUID id);

    ProductResponse fetchProductById(@NotNull UUID id);

    PageResponse<ProductResponse> fetchProductsInPage(int page, int size, String keyword, ProductSortField sortBy, String sortDirection);

    Boolean checkForProductExistence(@NotNull UUID id);

    ProductToCartResponse addProductToCart(@Valid ProductToCartRequest request, UUID userId);

    ProductToWishlistResponse addProductToWishlist(@Valid ProductToWishlistRequest request, UUID userId);
}
