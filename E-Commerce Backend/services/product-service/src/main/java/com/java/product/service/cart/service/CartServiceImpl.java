package com.java.product.service.cart.service;

import com.java.product.service.cart.dto.CartToOrderProductRequest;
import com.java.product.service.cart.dto.OrderCreationResponse;
import com.java.product.service.cart.dto.OrderRequest;
import com.java.product.service.cart.dto.OrderRequestItem;
import com.java.product.service.cart.entity.Cart;
import com.java.product.service.cart.entity.CartItem;
import com.java.product.service.cart.repository.CartItemRepository;
import com.java.product.service.cart.repository.CartRepository;
import com.java.product.service.client.OrderClient;
import com.java.product.service.exception.custom.ResourceNotFoundException;
import com.java.product.service.wishlist.entity.Wishlist;
import com.java.product.service.wishlist.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final WishlistService wishlistService;
    private final OrderClient orderClient;

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

    @Override
    @Transactional
    public OrderCreationResponse checkoutCart(final CartToOrderProductRequest request) {
        UUID userId = request.userId();
        UUID cartId = request.cartId();

        Cart cart = cartRepository.findByCartIdAndUserId(cartId, userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cart with id: " + cartId + " not found in cart"));

        if (cart.getItems().isEmpty()) {
            throw new ResourceNotFoundException("Cart is empty!!!");
        }

        // building order request
        List<OrderRequestItem> items = cart.getItems()
                .stream()
                .map(item -> OrderRequestItem.builder()
                        .productId(item.getProductId())
                        .quantity(item.getQuantity())
                        .build())
                .toList();

        OrderRequest req = OrderRequest.builder()
                .userId(userId)
                .items(items)
                .build();

        orderClient.createOrder(req);

        // clearing cart after successful order
        cart.getItems().clear();
        cartRepository.save(cart);

        return OrderCreationResponse.builder()
                .message("Order Created Successfully")
                .build();
    }


}
