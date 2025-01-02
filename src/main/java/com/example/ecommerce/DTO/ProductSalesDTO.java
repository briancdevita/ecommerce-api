package com.example.ecommerce.DTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class ProductSalesDTO {
    private Long productId;
    private String productName;
    private int totalSold;
    private Double totalRevenue;





}
