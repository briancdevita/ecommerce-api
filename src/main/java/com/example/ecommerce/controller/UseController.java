package com.example.ecommerce.controller;


import com.example.ecommerce.DTO.UserDTO;
import com.example.ecommerce.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class UseController {


    @Autowired
    private UserService userService;


    @PatchMapping("/{id}/address")
    public ResponseEntity<UserDTO> updateAddress(@PathVariable Long id, @RequestBody Map<String, String> request) {
        String newAddress = request.get("address");
        UserDTO updatedUser = userService.updateAddress(id, newAddress);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

}
