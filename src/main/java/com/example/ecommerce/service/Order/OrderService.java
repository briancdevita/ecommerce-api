package com.example.ecommerce.service.Order;


import com.example.ecommerce.DTO.CartDTO;
import com.example.ecommerce.DTO.OrderDTO;
import com.example.ecommerce.DTO.OrderFilterDTO;
import com.example.ecommerce.DTO.OrderItemDTO;
import com.example.ecommerce.enums.OrderStatus;
import com.example.ecommerce.exception.ApiException;
import com.example.ecommerce.exception.error.ApiError;
import com.example.ecommerce.exception.error.InsufficientStockException;
import com.example.ecommerce.model.*;
import com.example.ecommerce.repository.OrderItemRepository;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.service.CuponService;
import com.example.ecommerce.service.cart.CartService;
import com.example.ecommerce.service.user.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.aspectj.weaver.ast.Or;
import org.hibernate.engine.spi.CollectionEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {


    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartService cartService;
    private final UserService userService;
    private final CuponService cuponService;

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, CartService cartService, ProductRepository productRepository, UserService userService, CuponService cuponService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartService = cartService;
        this.productRepository = productRepository;

        this.userService = userService;
        this.cuponService = cuponService;
    }



    public List<OrderDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(this::mapToOrderDTO).collect(Collectors.toList());
    }


    public List<OrderDTO> getOrdersByStatus(OrderStatus status) {
        List<Order> orders = orderRepository.findByStatus(status);
        return orders.stream().map(this::mapToOrderDTO).collect(Collectors.toList());
    }





    @Transactional
    public OrderDTO createOrder(User user, String couponCode) {
        // Obtener el carrito del usuario
        CartDTO cart = cartService.getCartByUser(user);


        if (cart.getItems().isEmpty()) {
            throw new IllegalArgumentException("El carrito está vacío. No se puede crear una orden.");
        }

        // Calcular el total del carrito
        Double totalPrice = cartService.calculateTotal(cart);
        Double discountAmount = 0.0;
        Cupon cupon = null;

        // Validar y aplicar el cupón si se proporciona
        if (couponCode != null && !couponCode.trim().isEmpty()) {
            try {
                cupon = cuponService.validateCoupon(couponCode);
                discountAmount = totalPrice * (cupon.getDiscountPercentage() / 100);
                // Incrementar el uso del cupón
                cuponService.incrementUsage(cupon);
            } catch ( ApiException e) {
                System.out.println(e.getMessage());
                throw new IllegalArgumentException("Cupón inválido: " + e.getMessage());
            }
        }

        // Calcular el monto final
        Double finalAmount = totalPrice - discountAmount;
        System.out.println("Total: " + totalPrice + ", Descuento: " + discountAmount + ", Final: " + finalAmount);

        // Crear la orden
        Order order = Order.builder()
                .user(user)
                .orderDate(LocalDateTime.now())
                .totalPrice(totalPrice)
                .discountAmount(discountAmount)
                .finalAmount(finalAmount)
                .status(OrderStatus.PENDING)
                .cupon(cupon)
                .build();

        // Procesar los items del carrito
        List<OrderItem> orderItems = cart.getItems().stream().map(cartItem -> {

            Product product = productRepository.findById(cartItem.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

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
        orderItemRepository.saveAll(orderItems); // Corregir aquí


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
                .orderDate(order.getOrderDate())
                .receiptUrl(order.getReceiptUrl())
                .discountAmount(order.getDiscountAmount())
                .finalAmount(order.getFinalAmount())
                .username(order.getUser().getUsername())
                .status(order.getStatus().toString())
                .items(order.getOrderItems().stream().map(item -> OrderItemDTO.builder()
                        .id(item.getId())
                        .productId(item.getProduct().getId())
                        .productName(item.getProduct().getName())
                        .quantity(item.getQuantity())
                        .image(item.getProduct().getImage())
                        .price(item.getPrice()) // Precio por cantidad ya calculado
                        .build()).toList())
                .build();
    }


    @PersistenceContext
    private EntityManager entityManager;
    public void validateStock(Product product, int requestedQuantity) {
        System.out.println("Validating stock for productId: " + product.getId() + ", stock: " + product.getStock() + ", requested: " + requestedQuantity);

        entityManager.refresh(product);
        if (product.getStock() < requestedQuantity) {
            throw new InsufficientStockException(product.getId(), requestedQuantity );
        }
    }


    public void reduceStock(Product product, int quantity) {
        entityManager.refresh(product);
        product.setStock(product.getStock() - quantity);
        productRepository.save(product);
    }


    public List<OrderDTO> getOrdersByUserAndStatus(User user, String status) {
        List<Order> orders;

        if (status != null) {
            OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
            orders = orderRepository.findByUserAndStatus(user, orderStatus);
        } else {
            orders = orderRepository.findByUser(user);
        }
        return orders.stream().map(this::mapToOrderDTO).toList();
    }

    public List<OrderDTO> getOrdersForAdmin(String username, String status) {
        List<Order> orders;
        if (username != null && status != null) {
            User user = userService.findByUsername(username);
            OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
            orders = orderRepository.findByUserAndStatus(user, orderStatus);
        } else if (username != null) {
            User user = userService.findByUsername(username);
            orders = orderRepository.findByUser(user);
        } else if (status != null) {
            OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
            orders = orderRepository.findByStatus(orderStatus);
        } else {
            orders = orderRepository.findAll();
        }
        return orders.stream().map(this::mapToOrderDTO).toList();
    }


    public OrderDTO updateOrderStatus(Long orderId, OrderStatus newStatus, User user) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ApiException(ApiError.NOT_FOUND));

        if (user.getRoles().stream().anyMatch(role -> role.getName().equals("ADMIN"))) {
            // Permitir a los administradores cambiar cualquier estado
            order.setStatus(newStatus);
        } else if (user.getRoles().stream().anyMatch(role -> role.getName().equals("USER"))) {
            // Restricciones específicas para usuarios
            if (newStatus != OrderStatus.COMPLETED) {
                throw new ApiException(ApiError.FORBIDDEN);
            }
            order.setStatus(newStatus);
        } else {
            throw new ApiException(ApiError.FORBIDDEN);
        }

        orderRepository.save(order);
        return mapToOrderDTO(order);
    }


    public List<OrderDTO> getOrdersWithFilters(OrderFilterDTO filters) {
        String username = filters.getUsername();
        OrderStatus status = filters.getStatus();
        var startDate = filters.getStartDate();
        var endDate = filters.getEndDate();


        List<Order> orders  = orderRepository.findAll().stream()
                .filter(order -> username == null || order.getUser().getUsername().equals(username))
                .filter(order -> status == null || order.getStatus().equals(status))
                .filter(order -> startDate == null || !order.getOrderDate().toLocalDate().isBefore(startDate))
                .filter(order -> endDate == null || !order.getOrderDate().toLocalDate().isAfter(endDate))
                .collect(Collectors.toList());

        return orders.stream().map(this::mapToOrderDTO).collect(Collectors.toList());
    }


    public OrderDTO getOrderByIdAndUser(Long orderId, User user) {
        return orderRepository.findById(orderId)
                .filter(order -> order.getUser().equals(user)) // Asegúrate de que la orden pertenece al usuario
                .map(this::mapToOrderDTO) // Convierte la orden en un DTO
                .orElseThrow(() -> new ApiException(ApiError.NOT_FOUND));
    }

    @Transactional
    public OrderDTO updateReceiptUrl(Long orderId, String receiptUrl) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + orderId));

        if (order.getStatus() != OrderStatus.COMPLETED) {
            throw new IllegalStateException("Cannot update receipt URL for an order that is not completed.");
        }

        order.setReceiptUrl(receiptUrl);
        orderRepository.save(order);

        return mapToOrderDTO(order);
    }




}
