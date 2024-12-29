package com.example.ecommerce.DTO;

import lombok.Data;

import java.util.List;

@Data
public class UpdateUserDTO {
    private String username;
    private String email;
    private List<String> roles; // Opcional
}
