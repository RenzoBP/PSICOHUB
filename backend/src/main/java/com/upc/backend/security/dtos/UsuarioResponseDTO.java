package com.upc.backend.security.dtos;

import lombok.Data;

import java.util.Set;

@Data
public class UsuarioResponseDTO {
    private Long id;
    private String email;
    private String dni;
    private Set<String> roles;
}