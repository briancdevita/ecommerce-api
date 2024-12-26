package com.example.ecommerce.controller.orders;


import com.example.ecommerce.DTO.OrderDTO;
import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.User;
import com.example.ecommerce.service.Order.OrderService;
import com.example.ecommerce.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {




    private final OrderService orderService;
    private final UserService userService;


    @Autowired
    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        OrderDTO order = orderService.createOrder(user);
        return ResponseEntity.ok(order);
    }


    @GetMapping
    public ResponseEntity<List<OrderDTO>> getUserOrders(@AuthenticationPrincipal UserDetails userDetails) {
        var user = userService.findByUsername(userDetails.getUsername());
        var orders = orderService.getOrdersByUser(user);
        return ResponseEntity.ok(orders);
    }


    @GetMapping("/user")
    public ResponseEntity<List<OrderDTO>> getUserOrders(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) String status

    ) {
        User user = userService.findByUsername(userDetails.getUsername());
        List<OrderDTO> orders = orderService.getOrdersByUserAndStatus(user, status);
        return ResponseEntity.ok(orders);
    }


    @GetMapping("/admin")
    public ResponseEntity<List<OrderDTO>> getAllOrders(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String status) {
        // Llama al servicio para obtener las órdenes filtradas
        List<OrderDTO> orders = orderService.getOrdersForAdmin(username, status);
        return ResponseEntity.ok(orders);
    }



}
