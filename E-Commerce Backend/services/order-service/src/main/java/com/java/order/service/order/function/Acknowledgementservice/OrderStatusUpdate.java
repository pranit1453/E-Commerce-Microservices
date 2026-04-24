package com.java.order.service.order.function.Acknowledgementservice;

import com.java.order.service.order.dto.CreateOrderResponseToUpdateStatus;
import com.java.order.service.order.enums.OrderStatus;
import com.java.order.service.order.repository.OrderRepository;
import com.java.order.service.order.service.OrderServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class OrderStatusUpdate {
    private final OrderRepository orderRepository;
    private final OrderServiceImpl orderServiceImpl;

    @Bean
    public Consumer<CreateOrderResponseToUpdateStatus> updateOrderStatus() {
        return (response) -> {
            orderRepository.findOrderByOrderId(response.orderId())
                    .ifPresentOrElse(order -> {
                        if (order.getOrderStatus() == OrderStatus.PAYMENT_PENDING) {
                            order.setOrderStatus(OrderStatus.CONFIRMED);
                            orderRepository.save(order);
                        }
                    }, () -> {
                        throw new RuntimeException("Order Not Found Yet with Order Id: " + response.orderId());
                    });

            // call to OrderServiceImpl
            orderServiceImpl.notifyUserAboutOrder(response);

        };
    }
}
