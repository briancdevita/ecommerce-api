package com.example.ecommerce.controller;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Setter
@Getter
public class RegisterRequest {

    private String username;
    private String email;
    private String password;

}
