package com.example.ecommerce.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Cupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;

    @Column(nullable = false)
    private Double discountPercentage; // Porcentaje de descuento (e.g., 10.0 para 10%)

    @Column(nullable = false)
    private Boolean isActive;

    private LocalDateTime expirationDate;

    private Integer usageLimit; // Número máximo de usos
    private Integer timesUsed;  // Número de veces que ha sido usado
}
