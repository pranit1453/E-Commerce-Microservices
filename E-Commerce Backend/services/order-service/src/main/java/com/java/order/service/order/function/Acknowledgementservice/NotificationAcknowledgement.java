package com.java.order.service.order.function.Acknowledgementservice;

import com.java.order.service.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class NotificationAcknowledgement {

    private final OrderRepository orderRepository;

    @Bean
    public Consumer<UUID> orderCreationAcknowledgement() {
        return (id) ->
                System.out.println("Notified to User that Order ID: " + id + "placed Successfully");
    }

}
