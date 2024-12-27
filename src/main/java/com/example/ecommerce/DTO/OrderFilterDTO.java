package com.example.ecommerce.DTO;


import com.example.ecommerce.enums.OrderStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class OrderFilterDTO {


    private String username;
    private OrderStatus status;
    private LocalDate startDate;
    private LocalDate endDate;
}
