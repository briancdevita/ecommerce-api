package com.example.ecommerce.controller.auth;


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
    private String address;

}
