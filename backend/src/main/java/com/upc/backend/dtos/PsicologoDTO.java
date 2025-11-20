package com.upc.backend.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.upc.backend.security.entities.Usuario;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PsicologoDTO {
    private Long idPsicologo;
    private String nombre;
    private String apellido;
    private String dni;
    private LocalDate fechaNacimiento;
    private String genero;
    private String distrito;
    private String direccion;
    private String telefono;
    private String email;
    private String password;
    @JsonIgnore
    private Boolean activo;
    @JsonIgnore
    private Usuario usuario;
}
