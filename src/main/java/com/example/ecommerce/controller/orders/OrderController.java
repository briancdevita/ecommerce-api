package com.example.ecommerce.controller.orders;


import com.example.ecommerce.DTO.OrderDTO;
import com.example.ecommerce.DTO.OrderFilterDTO;
import com.example.ecommerce.enums.OrderStatus;
import com.example.ecommerce.DTO.OrderRequestDTO;
import com.example.ecommerce.model.User;
import com.example.ecommerce.service.Order.OrderService;
import com.example.ecommerce.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "*", methods = {RequestMethod.POST,RequestMethod.GET, RequestMethod.PATCH})
public class OrderController {




    private final OrderService orderService;
    private final UserService userService;


    @Autowired
    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }



    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<OrderDTO>> getAllOrders(@ModelAttribute OrderFilterDTO filters) {
       List<OrderDTO> orders = orderService.getOrdersWithFilters(filters);
       return ResponseEntity.ok(orders);

    }






    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OrderDTO> createOrder(
            @Valid @RequestBody OrderRequestDTO orderRequest,
            @AuthenticationPrincipal UserDetails userDetails) {
        System.out.println(orderRequest);
        User user = userService.findByUsername(userDetails.getUsername());
        OrderDTO order = orderService.createOrder(user, orderRequest.getCouponCode());
        return ResponseEntity.ok(order);
    }

    @PostMapping("/{orderId}/receipt")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<OrderDTO> updateReceiptUrl(
            @PathVariable Long orderId,
            @RequestBody Map<String, String> requestBody) {
        System.out.println("Request body: " + requestBody);
        String receiptUrl = requestBody.get("receiptUrl");
        OrderDTO updatedOrder = orderService.updateReceiptUrl(orderId, receiptUrl);
        return ResponseEntity.ok(updatedOrder);
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


    @GetMapping("/admin/filters")
    public ResponseEntity<List<OrderDTO>> getAllOrdersFiltered(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String status) {
        // Llama al servicio para obtener las órdenes filtradas
        List<OrderDTO> orders = orderService.getOrdersForAdmin(username, status);
        return ResponseEntity.ok(orders);
    }




    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<OrderDTO> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus orderStatus,
            @AuthenticationPrincipal UserDetails userDetails
            ) {
        var user = userService.findByUsername(userDetails.getUsername());
        var updatedOrder = orderService.updateOrderStatus(id, orderStatus, user);
        return ResponseEntity.ok(updatedOrder);
    }


    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        User user = userService.findByUsername(userDetails.getUsername());
        OrderDTO order = orderService.getOrderByIdAndUser(id, user);
        return ResponseEntity.ok(order);
    }




}
