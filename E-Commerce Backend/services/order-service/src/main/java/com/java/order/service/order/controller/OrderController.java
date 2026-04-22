package com.java.order.service.order.controller;

import com.java.order.service.order.dto.CreateOrderResponse;
import com.java.order.service.order.dto.OrderRequest;
import com.java.order.service.order.dto.OrderResponse;
import com.java.order.service.order.dto.ProductDetails;
import com.java.order.service.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
@Validated
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<OrderResponse> createOrder(@RequestBody final @Valid OrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.createOrder(request));
    }

    @PostMapping("/create-order")
    public ResponseEntity<CreateOrderResponse> createOrderAndProceedToBuy(@RequestBody final @Valid ProductDetails details) {
        UUID userId = UUID.fromString("3a6bea85-1f4e-4a77-b3d6-13ab65f06589");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.createOrderAndProceedToBuy(userId, details));
    }
}
