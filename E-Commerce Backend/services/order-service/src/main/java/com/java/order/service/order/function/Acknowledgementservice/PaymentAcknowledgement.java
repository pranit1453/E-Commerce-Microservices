package com.java.order.service.order.function.Acknowledgementservice;

import com.java.order.service.order.enums.OrderStatus;
import com.java.order.service.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class PaymentAcknowledgement {
    private final OrderRepository orderRepository;

    @Bean
    public Consumer<UUID> paymentCreationAcknowledgement() {
        return (id) -> {
            orderRepository.findOrderByOrderId(id)
                    .ifPresentOrElse(order -> {
                        if (order.getOrderStatus() == OrderStatus.CREATED) {
                            order.setOrderStatus(OrderStatus.PAYMENT_PENDING);
                            orderRepository.save(order);
                        }
                    }, () -> {
                        throw new RuntimeException("Order Not Found Yet with Order Id: " + id);
                    });
        };
    }
}
