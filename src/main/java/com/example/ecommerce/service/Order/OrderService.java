package com.example.ecommerce.service.Order;


import com.example.ecommerce.DTO.CartDTO;
import com.example.ecommerce.DTO.OrderDTO;
import com.example.ecommerce.DTO.OrderItemDTO;
import com.example.ecommerce.enums.OrderStatus;
import com.example.ecommerce.exception.error.InsufficientStockException;
import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.OrderItem;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.OrderItemRepository;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.service.cart.CartService;
import org.aspectj.weaver.ast.Or;
import org.hibernate.engine.spi.CollectionEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {


    private final ProductRepository productRepository;
    private OrderRepository orderRepository;
    private OrderItemRepository orderItemRepository;
    private CartService cartService;

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, CartService cartService, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartService = cartService;
        this.productRepository = productRepository;
    }



    public OrderDTO createOrder(User user) {
        CartDTO cart = cartService.getCartByUser(user);

        if (cart.getItems().isEmpty()) {
            throw new IllegalArgumentException("The cart is empty. Cannot create an order.");
        }

        Order order = Order.builder()
                .user(user)
                .orderDate(LocalDateTime.now())
                .totalPrice(cartService.calculateTotal(cart))
                .status(OrderStatus.PENDING)
                .build();

        List<OrderItem> orderItems = cart.getItems().stream().map(cartItem -> {
            Product product = productRepository.findById(cartItem.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found"));


            validateStock(product, cartItem.getQuantity());

            reduceStock(product, cartItem.getQuantity());

            return OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(cartItem.getQuantity())
                    .price(cartItem.getQuantity() * product.getPrice())
                    .build();
        }).collect(Collectors.toList());

        order.setOrderItems(orderItems);
        orderRepository.save(order);
        orderItemRepository.equals(orderItems);

        cartService.clearCart(user);

        return mapToOrderDTO(order);
    }




    public List<OrderDTO> getOrdersByUser(User user) {
        return orderRepository.findByUser(user)
                .stream()
                .map(this::mapToOrderDTO)
                .collect(Collectors.toList());
    }


    public OrderDTO mapToOrderDTO(Order order) {
        return OrderDTO.builder()
                .id(order.getId())
                .username(order.getUser().getUsername())
                .status(order.getStatus().toString())
                .items(order.getOrderItems().stream().map(item -> OrderItemDTO.builder()
                        .id(item.getId())
                        .productId(item.getProduct().getId())
                        .productName(item.getProduct().getName())
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .build()).toList())
                .totalPrice(order.getOrderItems().stream()
                        .mapToDouble(item -> item.getPrice() * item.getQuantity())
                        .sum())
                .build();
    }



    public void validateStock(Product product, int requestedQuantity) {
        if (product.getStock() < requestedQuantity) {
            throw new InsufficientStockException(product.getId(), requestedQuantity );
        }
    }

    public void reduceStock(Product product, int quantity) {
        product.setStock(product.getStock() - quantity);
        productRepository.save(product);
    }


}
