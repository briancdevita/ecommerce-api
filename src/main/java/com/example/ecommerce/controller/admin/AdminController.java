package com.example.ecommerce.controller.admin;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*", methods = {RequestMethod.POST})
public class AdminController {



    @GetMapping("/dashboard")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getDashboard() {
        return "Admin dashboard";
    }
}
