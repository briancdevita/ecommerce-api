package com.example.ecommerce.service.user;


import com.example.ecommerce.DTO.RoleDTO;
import com.example.ecommerce.DTO.UpdateUserDTO;
import com.example.ecommerce.DTO.UserDTO;
import com.example.ecommerce.exception.GlobalExceptionHandler;
import com.example.ecommerce.model.Role;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.RoleRepository;
import com.example.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;


    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
    }


    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToUserDTO)
                .collect(Collectors.toList());
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found");
        }
        userRepository.deleteById(id);
    }

    public UserDTO updateUser(Long id, UpdateUserDTO updateUserDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setUsername(updateUserDTO.getUsername());
        user.setEmail(updateUserDTO.getEmail());

        if (updateUserDTO.getRoles() != null) {
            user.setRoles(roleRepository.findByNameIn(updateUserDTO.getRoles()));
        }

        userRepository.save(user);
        return mapToUserDTO(user);
    }




    private UserDTO mapToUserDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(user.getRoles().stream()
                        .map(role -> RoleDTO.builder().name(role.getName()).build())
                        .collect(Collectors.toSet()))
                .build();
    }






}
