package com.upc.backend.security.dtos;

import lombok.Data;

@Data
public class UsuarioRequestDTO {
    private String email;
    private String password;
}