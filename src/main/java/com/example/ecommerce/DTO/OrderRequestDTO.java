package com.example.ecommerce.DTO;


import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class OrderRequestDTO {

    @Size(min = 5, max = 20, message = "El código del cupón debe tener entre 5 y 20 caracteres")
    private String couponCode;
}
