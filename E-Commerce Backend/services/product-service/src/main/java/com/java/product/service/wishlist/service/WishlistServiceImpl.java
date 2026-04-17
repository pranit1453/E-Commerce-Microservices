package com.java.product.service.wishlist.service;

import com.java.product.service.exception.custom.DuplicateResourceFoundException;
import com.java.product.service.product.entity.Product;
import com.java.product.service.wishlist.entity.Wishlist;
import com.java.product.service.wishlist.entity.WishlistItem;
import com.java.product.service.wishlist.repository.WishlistItemRepository;
import com.java.product.service.wishlist.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;
    private final WishlistItemRepository wishlistItemRepository;

    @Override
    @Transactional
    public Wishlist getOrCreateWishlist(final UUID userId) {
        return wishlistRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Wishlist wishlist = Wishlist.builder()
                            .userId(userId)
                            .build();
                    return wishlistRepository.save(wishlist);
                });
    }

    @Override
    @Transactional
    public WishlistItem addWishlistItem(final Wishlist wishlist, final Product product) {
        boolean exists = wishlistItemRepository
                .existsByWishlist_WishlistIdAndProductId(wishlist.getWishlistId(), product.getProductId());
        if (exists) {
            throw new DuplicateResourceFoundException("Product with id " + product.getProductId() + " already exists in Wishlist ");
        }

        WishlistItem wishlistItem = WishlistItem.builder()
                .productId(product.getProductId())
                .build();

        wishlist.addItem(wishlistItem);
        return wishlistItem;
    }

    @Override
    @Transactional
    public void addWishlistItemByProductId(final Wishlist wishlist, final UUID productId) {
        boolean exists = wishlistItemRepository
                .existsByWishlist_WishlistIdAndProductId(wishlist.getWishlistId(), productId);
        if (exists) {
            throw new DuplicateResourceFoundException("Product with id " + productId + " already exists in Wishlist ");
        }

        WishlistItem wishlistItem = WishlistItem.builder()
                .productId(productId)
                .build();

        wishlist.addItem(wishlistItem);
    }
}
