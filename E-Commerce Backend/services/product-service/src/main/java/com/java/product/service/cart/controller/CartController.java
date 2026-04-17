package com.java.product.service.cart.controller;

import com.java.product.service.cart.service.CartService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
@Validated
public class CartController {

    private final CartService cartService;

    @DeleteMapping("/move-to-wishlist")
    public ResponseEntity<String> movedToWishlist(@RequestParam final @NotNull UUID productId) {
        cartService.movedFromCartToWishlist(productId);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Product with id: " + productId + " has been moved to wishlist");
    }
}
