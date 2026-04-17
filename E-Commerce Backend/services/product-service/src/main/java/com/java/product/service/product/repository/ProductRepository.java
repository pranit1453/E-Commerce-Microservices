package com.java.product.service.product.repository;

import com.java.product.service.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {

    boolean existsByCategory_CategoryId(UUID categoryCategoryId);

    @Query("""
                    SELECT p.name FROM Product p WHERE p.name = :names
            """)
    Set<String> findExistingNames(Set<String> names);

    Optional<Product> findByProductId(UUID productId);

}
