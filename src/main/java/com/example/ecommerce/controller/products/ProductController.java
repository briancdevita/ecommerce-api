package com.example.ecommerce.controller.products;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {




    @GetMapping
    @PreAuthorize("hasAuthority('USER')")
    public String getProducts() {
        return "List of products";
    }
}
