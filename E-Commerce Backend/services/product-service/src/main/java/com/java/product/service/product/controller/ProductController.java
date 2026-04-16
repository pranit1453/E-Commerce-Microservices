package com.java.product.service.product.controller;

import com.java.product.service.product.dto.*;
import com.java.product.service.product.enums.ProductSortField;
import com.java.product.service.product.service.ProductService;
import com.java.product.service.wrapper.PageResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
@Validated
public class ProductController {

    private final ProductService productService;

    @PostMapping("/list/create")
    public ResponseEntity<Set<CreateProductResponse>> createProductsInBulk(@RequestBody final Set<@Valid CreateProductRequest> request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.createProductInBulk(request));
    }

    @PostMapping("/create")
    public ResponseEntity<CreateProductResponse> createProduct(@RequestBody final @Valid CreateProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.createProduct(request));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UpdateProductResponse> updateProduct(@PathVariable final @NotNull UUID id,
                                                               @RequestBody @Valid UpdateProductRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.updateProduct(id, request));
    }

    @PatchMapping("/patch/{id}")
    public ResponseEntity<UpdateProductResponse> patchProduct(@PathVariable final @NotNull UUID id,
                                                              @RequestBody @Valid UpdateProductRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.patchProduct(id, request));
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable final @NotNull UUID id) {
        productService.deleteProductById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body("Product with id: " + id + " has been deleted");
    }

    @GetMapping("/fetch/{id}")
    public ResponseEntity<ProductResponse> fetchProductById(@PathVariable final @NotNull UUID id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.fetchProductById(id));
    }

    @GetMapping("/fetch/all")
    public ResponseEntity<PageResponse<ProductResponse>> fetchProductsInPage(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "5") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false, defaultValue = "PRODUCT_ID") ProductSortField sortBy,
            @RequestParam(required = false, defaultValue = "ASC") String sortDirection
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.fetchProductsInPage(page, size, keyword, sortBy, sortDirection));
    }
}
