package com.example.ecommerce.controller;


import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Setter
@Getter
public class AuthenticationResponse {


    private String token;


}
