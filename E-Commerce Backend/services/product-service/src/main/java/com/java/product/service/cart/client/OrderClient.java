package com.java.product.service.cart.client;

import com.java.product.service.cart.dto.OrderRequest;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "order-service", url = "http://localhost:8082/api/v1/order")
@Validated
public interface OrderClient {

    @PostMapping("/create")
    void createOrder(@RequestBody final @Valid OrderRequest request);
}
