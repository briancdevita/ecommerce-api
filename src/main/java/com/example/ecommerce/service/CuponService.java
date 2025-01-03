package com.example.ecommerce.service;


import com.example.ecommerce.model.Cupon;
import com.example.ecommerce.repository.CuponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CuponService {


    @Autowired
    private CuponRepository cuponRepository;


    public Cupon createCoupon(Cupon cupon) {
        // Validar que el código no exista
        if(cuponRepository.findByCode(cupon.getCode()).isPresent()) {
            throw new RuntimeException("El código del cupón ya existe");
        }
        cupon.setIsActive(true);
        cupon.setTimesUsed(0);
        return cuponRepository.save(cupon);
    }


    public Cupon validateCoupon(String code) {
        Cupon cupon = cuponRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Cupón no válido"));

        if(!cupon.getIsActive()) {
            throw new RuntimeException("El cupón no está activo");
        }

        if(cupon.getExpirationDate() != null && cupon.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("El cupón ha expirado");
        }

        if(cupon.getUsageLimit() != null && cupon.getTimesUsed() >= cupon.getUsageLimit()) {
            throw new RuntimeException("El cupón ha alcanzado su límite de uso");
        }

        return cupon;
    }


    public void incrementUsage(Cupon cupon) {
        cupon.setTimesUsed(cupon.getTimesUsed() + 1);
        if(cupon.getUsageLimit() != null && cupon.getTimesUsed() >= cupon.getUsageLimit()) {
            cupon.setIsActive(false);
        }
        cuponRepository.save(cupon);
    }

}


