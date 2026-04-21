package com.java.product.service.cart.service;

import com.java.product.service.cart.dto.CartToOrderProductRequest;
import com.java.product.service.cart.dto.OrderCreationResponse;
import com.java.product.service.cart.entity.Cart;
import com.java.product.service.cart.entity.CartItem;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public interface CartService {
    Cart getOrCreateCart(UUID userId);

    CartItem addOrUpdateCartItem(Cart cart, UUID productId, @Min(1) Integer quantity);

    void movedFromCartToWishlist(@NotNull UUID productId);

    OrderCreationResponse checkoutCart(@Valid CartToOrderProductRequest request);
}
