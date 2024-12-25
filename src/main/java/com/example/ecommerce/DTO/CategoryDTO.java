package com.example.ecommerce.DTO;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryDTO {

    @NotBlank(message = "Category name is required")
    private String name;

}
