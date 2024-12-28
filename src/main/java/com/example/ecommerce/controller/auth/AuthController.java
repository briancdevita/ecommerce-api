package com.example.ecommerce.controller.auth;


import com.example.ecommerce.config.auth.AuthenticationService;

import org.springframework.http.ResponseEntity;


import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*", methods = {RequestMethod.POST})
public class AuthController {


    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register (@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate (@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}