package com.example.ecommerce.DTO;

import lombok.*;

import java.util.Set;


@Setter
@Getter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String password;
    private Set<RoleDTO> roles;
    private String address;


}
