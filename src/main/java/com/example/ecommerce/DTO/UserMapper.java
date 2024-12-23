package com.example.ecommerce.DTO;

import com.example.ecommerce.model.User;

import java.util.Set;
import java.util.stream.Collectors;

public class UserMapper {


    public static UserDTO toUserDTO(User user) {
        Set<RoleDTO> roleDTOs = user.getRoles()
                .stream()
                .map(role -> new RoleDTO(role.getId(), role.getName()))
                .collect(Collectors.toSet());

        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                roleDTOs
        );
    }
}
