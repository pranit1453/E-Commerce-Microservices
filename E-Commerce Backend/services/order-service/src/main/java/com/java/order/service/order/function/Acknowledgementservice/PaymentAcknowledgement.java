package com.java.order.service.order.function.Acknowledgementservice;

import com.java.order.service.order.enums.OrderStatus;
import com.java.order.service.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class PaymentAcknowledgement {
    private final OrderRepository orderRepository;

    @Bean
    public Function<Map<String, UUID>, Map<String, UUID>> paymentCreationAcknowledgement() {
        return (payload) -> {
            System.out.println("Come in Order service");
            UUID orderId = Objects.requireNonNull(payload.get("orderId"), "orderId is required");
            UUID paymentId = Objects.requireNonNull(payload.get("paymentId"), "paymentId is required");
            orderRepository.findOrderByOrderId(orderId)
                    .ifPresentOrElse(order -> {
                        if (order.getOrderStatus() == OrderStatus.CREATED) {
                            System.out.println("Changed order status ");
                            order.setOrderStatus(OrderStatus.PAYMENT_PENDING);
                            orderRepository.save(order);
                        }
                    }, () -> {
                        throw new RuntimeException("Order Not Found Yet with Order Id: " + orderId);
                    });

            return Map.of(
                    "orderId", orderId,
                    "paymentId", paymentId
            );
        };
    }
}
