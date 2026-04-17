package com.java.product.service.cart.service;

import com.java.product.service.cart.entity.Cart;
import com.java.product.service.cart.entity.CartItem;
import com.java.product.service.cart.repository.CartItemRepository;
import com.java.product.service.cart.repository.CartRepository;
import com.java.product.service.exception.custom.ResourceNotFoundException;
import com.java.product.service.product.repository.ProductRepository;
import com.java.product.service.wishlist.entity.Wishlist;
import com.java.product.service.wishlist.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final WishlistService wishlistService;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public Cart getOrCreateCart(final UUID userId) {
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart cart = Cart.builder()
                            .userId(userId)
                            .build();
                    return cartRepository.save(cart);
                });
    }

    @Override
    @Transactional
    public CartItem addOrUpdateCartItem(final Cart cart,
                                        final UUID productId,
                                        final Integer quantity) {
        return cartItemRepository.findByCart_CartIdAndProductId(cart.getCartId(), productId)
                .map(existing -> {
                    existing.setQuantity(existing.getQuantity() + quantity);
                    return existing;
                })
                .orElseGet(() -> {
                    CartItem cartItem = CartItem.builder()
                            .productId(productId)
                            .quantity(quantity)
                            .build();
                    cart.addItem(cartItem);
                    return cartItem;
                });
    }

    @Override
    @Transactional
    public void movedFromCartToWishlist(final UUID productId) {
        CartItem cartItem = cartItemRepository.findByProductId(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product with id: " + productId + " not found in cart"));
        UUID userId = cartItem.getCart().getUserId();
        UUID cartId = cartItem.getCart().getCartId();
        Wishlist wishlist = wishlistService.getOrCreateWishlist(userId);
        wishlistService.addWishlistItemByProductId(wishlist, cartItem.getProductId());
        cartItemRepository.delete(cartItem);
        boolean hasItems = cartItemRepository.existsByCart_CartId(cartId);
        if (!hasItems) {
            cartRepository.deleteById(cartId);
        }

    }


}
