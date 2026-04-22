package com.java.notification.service.service;

import com.java.notification.service.dto.OrderDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class OrderCreationService {

    private final EmailService emailService;

    @Bean
    public Function<OrderDetails, UUID> notifyUserAsOrderIsCreated() {
        // logic to send email to user
        return (this::sendNotificationToUser);
    }

    private UUID sendNotificationToUser(final OrderDetails orderDetails) {
        String to = "pranitbhangale07@gmail.com";
        String subject = "Order Created Notification";
        String body = buildEmailBody(orderDetails);

        emailService.sendEmail(to, subject, body);
        return orderDetails.orderId();
    }

    private String buildEmailBody(final OrderDetails orderDetails) {
        return """
                Hello,
                
                Your order has been successfully created move towards PAYMENT.
                
                Order ID: %s
                Product Names: %s
                Total Amount: ₹%s
                
                Thank you for shopping with us!
                """.formatted(
                orderDetails.orderId(),
                String.join(", ", orderDetails.productName()),
                orderDetails.grandTotal()
        );
    }
}
