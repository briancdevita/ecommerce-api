package com.example.ecommerce.controller;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Setter
@Getter
public class AuthenticationRequest {

    private String username;
    private String password;



}
