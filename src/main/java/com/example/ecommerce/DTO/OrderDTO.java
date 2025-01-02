package com.example.ecommerce.DTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDTO {
    private Long id; // ID de la orden
    private String username;
    private String receiptUrl; // Agregar este campo
    private LocalDateTime orderDate;// Usuario que creó la orden
    private String status; // Estado de la orden (e.g., "PENDING", "COMPLETED")
    private List<OrderItemDTO> items; // Lista de productos en la orden
    private Double totalPrice; // Precio total de la orden
}
