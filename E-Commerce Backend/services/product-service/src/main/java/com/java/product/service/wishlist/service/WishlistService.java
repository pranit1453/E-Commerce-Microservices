package com.java.product.service.wishlist.service;

import com.java.product.service.product.entity.Product;
import com.java.product.service.wishlist.entity.Wishlist;
import com.java.product.service.wishlist.entity.WishlistItem;

import java.util.UUID;

public interface WishlistService {
    Wishlist getOrCreateWishlist(UUID userId);

    WishlistItem addWishlistItem(Wishlist wishlist, Product product);

    void addWishlistItemByProductId(Wishlist wishlist, UUID productId);
}
