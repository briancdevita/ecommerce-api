package com.example.ecommerce.repository;


import com.example.ecommerce.model.Cupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CuponRepository  extends JpaRepository<Cupon, Long> {
    Optional<Cupon> findByCode(String code);
}
