//package com.java.order.service.order.function.service;
//
//import com.java.order.service.order.entity.Order;
//import com.java.order.service.order.enums.OrderStatus;
//import com.java.order.service.order.function.dto.OrderResponse;
//import com.java.order.service.order.repository.OrderRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.UUID;
//import java.util.function.Consumer;
//
//@Service
//@RequiredArgsConstructor
//public class NotificationAcknowledgement {
//
//    private final OrderRepository orderRepository;
//
//    @Bean
//    public Consumer<OrderResponse> orderCreationAcknowledgement() {
//        return this::handleAcknowledgement;
//    }
//
//    @Transactional
//    public void handleAcknowledgement(final OrderResponse response) {
//        UUID orderId = response.orderId();
//
//        Order order = orderRepository.findOrderByOrderId(orderId)
//                .orElseThrow(() ->
//                        new RuntimeException("Order with orderId " + orderId + " not found."));
//        order.setOrderStatus(OrderStatus.PAYMENT_PENDING);
//    }
//}
