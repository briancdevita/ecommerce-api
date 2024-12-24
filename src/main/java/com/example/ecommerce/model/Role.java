package com.example.ecommerce.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;


@Entity
@Data// Genera getters, setters, toString, etc.
@NoArgsConstructor // Constructor sin argumentos
@AllArgsConstructor
@Builder// Constructor con todos los argumentos
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;


    @Override
    public String getAuthority() {
        return name;
    }


}
