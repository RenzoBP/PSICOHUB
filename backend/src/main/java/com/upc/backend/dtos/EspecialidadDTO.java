package com.upc.backend.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EspecialidadDTO {
    private Long idEspecialidad;
    private String nombre;
    private String categoria;
    @JsonIgnore
    private Boolean activo = true;
}
