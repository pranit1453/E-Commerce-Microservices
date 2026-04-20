package com.java.order.service.order.client;

import com.java.order.service.order.dto.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "product-service", url = "http://localhost:8080/api/v1/product")
public interface ProductClient {

    @PostMapping("/batch")
    List<ProductResponse> getProductById(@RequestBody List<UUID> productIds);
}
