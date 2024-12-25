package com.example.ecommerce.DTO;


import lombok.Data;

@Data
public class UpdateQuiantityRequest {
    private Long productId;
    private Integer quantity;
}
