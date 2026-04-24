package com.java.order.service.order.service;

import com.java.order.service.order.client.InventoryClient;
import com.java.order.service.order.client.ProductClient;
import com.java.order.service.order.dto.*;
import com.java.order.service.order.entity.Order;
import com.java.order.service.order.entity.OrderItem;
import com.java.order.service.order.enums.OrderStatus;
import com.java.order.service.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Validated
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductClient productClient;
    private final InventoryClient inventoryClient;
    private final StreamBridge streamBridge;

    /**
     * Order Creation Process
     * Extract All Product IDs in List
     * Fetch All Products from Product service
     * Map with
     */
    @Override
    @Transactional
    public OrderResponse createOrder(final OrderRequest request) {
        // Extract product IDs
        List<UUID> productIds = request.items()
                .stream()
                .map(OrderRequestItem::productId)
                .toList();
        // Fetch Products
        List<ProductResponse> products = productClient.getProductById(productIds);

        Map<UUID, ProductResponse> productMap = products.stream()
                .collect(Collectors.toMap(ProductResponse::productId, p -> p));

        // prepare inventory request
        Map<UUID, Integer> stockRequest = request.items().stream()
                .collect(Collectors.toMap(OrderRequestItem::productId, OrderRequestItem::quantity));

        // check inventory before creating order
        Map<UUID, Boolean> stockResult = inventoryClient.checkStock(stockRequest);

        for (Map.Entry<UUID, Boolean> entry : stockResult.entrySet()) {
            if (!entry.getValue()) {
                throw new RuntimeException("Stock not available for product " + entry.getKey());
            }
        }

        inventoryClient.reserveStock(stockRequest);
        // Create order
        try {
            Order order = Order.builder()
                    .userId(request.userId())
                    .orderStatus(OrderStatus.CREATED)
                    .build();

            BigDecimal grandTotal = BigDecimal.ZERO;
            List<OrderItem> orderItems = new ArrayList<>();

            // Build order items
            for (OrderRequestItem item : request.items()) {
                ProductResponse product = productMap.get(item.productId());

                if (product == null) {
                    throw new RuntimeException("Product not found: " + item.productId());
                }

                BigDecimal price = product.price();
                BigDecimal totalPrice = price.multiply(BigDecimal.valueOf(item.quantity()));
                grandTotal = grandTotal.add(totalPrice);

                OrderItem orderItem = OrderItem.builder()
                        .productId(item.productId())
                        .name(product.name())
                        .quantity(item.quantity())
                        .price(product.price())
                        .order(order)
                        .build();
                orderItems.add(orderItem);
            }
            order.setItems(orderItems);
            order.setTotalPrice(grandTotal);
            orderRepository.save(order);


            List<OrderItemResponse> response = orderItems.stream()
                    .map(item -> {
                        ProductResponse product = productMap.get(item.getProductId());

                        return OrderItemResponse.builder()
                                .orderItemId(item.getOrderItemId())
                                .productId(item.getProductId())
                                .productName(product.name())
                                .price(item.getPrice())
                                .quantity(item.getQuantity())
                                .totalPrice(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                                .build();
                    }).toList();
            List<String> productName = orderItems.stream()
                    .map(item -> {
                        ProductResponse product = productMap.get(item.getProductId());
                        return product.name();
                    })
                    .toList();
            OrderDetails details = OrderDetails.builder()
                    .userId(order.getUserId())
                    .orderId(order.getOrderId())
                    .productName(productName)
                    .grandTotal(grandTotal)
                    .build();

            PaymentCreateDetails paymentCreateDetails = PaymentCreateDetails.builder()
                    .orderId(order.getOrderId())
                    .amount(grandTotal)
                    .userId(order.getUserId())
                    .build();

            TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronization() {
                        @Override
                        public void afterCommit() {
                            createPayment(paymentCreateDetails);
                        }
                    }
            );
            return OrderResponse.builder()
                    .orderId(order.getOrderId())
                    .userId(order.getUserId())
                    .orderItemResponse(response)
                    .grandTotal(grandTotal)
                    .status(order.getOrderStatus())
                    .message("Order Created Successfully")
                    .build();
        } catch (Exception ex) {
            inventoryClient.releaseStock(stockRequest);
            throw ex;
        }
    }

    @Override
    @Transactional
    public CreateOrderResponse createOrderAndProceedToBuy(final UUID userId, final ProductDetails details) {
        Order order = Order.builder()
                .userId(userId)
                .orderStatus(OrderStatus.CREATED)
                .build();

        BigDecimal price = details.price();
        BigDecimal totalPrice = price.multiply(BigDecimal.valueOf(details.quantity()));

        List<OrderItem> items = new ArrayList<>();

        OrderItem item = OrderItem.builder()
                .productId(details.productId())
                .name(details.name())
                .quantity(details.quantity())
                .price(price)
                .order(order)
                .build();

        items.add(item);

        order.setItems(items);
        order.setTotalPrice(totalPrice);

        orderRepository.save(order);
        List<String> name = List.of(item.getName());

        PaymentCreateDetails paymentCreateDetails = PaymentCreateDetails.builder()
                .orderId(order.getOrderId())
                .amount(totalPrice)
                .userId(order.getUserId())
                .build();

        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        createPayment(paymentCreateDetails);
                    }
                }
        );


        return CreateOrderResponse.builder()
                .orderId(item.getOrder().getOrderId())
                .message("Order Created Successfully and Proceed to buy")
                .build();
    }

    @Transactional(readOnly = true)
    public void notifyUserAboutOrder(CreateOrderResponseToUpdateStatus response) {
        Order order = orderRepository
                .findOrderByOrderIdWithItems(response.orderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        List<String> productNames = order.getItems()
                .stream()
                .map(OrderItem::getName)
                .toList();

        OrderDetails orderDetails = OrderDetails.builder()
                .userId(response.userId())
                .orderId(response.orderId())
                .paymentId(response.paymentId())
                .productName(productNames)
                .grandTotal(response.grandTotal())
                .build();

        notifyToUserOrderCreated(orderDetails);
    }

    private void createPayment(PaymentCreateDetails paymentCreateDetails) {
        streamBridge.send("orderCreatedEvent-out-0", paymentCreateDetails);

    }

    private void notifyToUserOrderCreated(final OrderDetails details) {
        streamBridge.send("orderNotificationEvent-out-0", details);
    }
}
