package com.example.ecommerce.DTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO {
    private Long id; // ID del ítem de la orden
    private Long productId; // ID del producto
    private String productName; // Nombre del producto
    private Integer quantity; // Cantidad comprada
    private String image;
    private Double price; // Precio del producto

}
