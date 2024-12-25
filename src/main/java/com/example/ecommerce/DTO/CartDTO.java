package com.example.ecommerce.DTO;


import lombok.Data;

import java.util.List;

@Data
public class CartDTO {

    private Long id;
    private String username;
    private List<CartItemDTO> items;
}
