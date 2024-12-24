package com.example.ecommerce.DTO;

import lombok.Data;

@Data
public class ProductDTO {
    private String name;
    private Double price;
    private String description;
    private String category;
    private String img;

}
