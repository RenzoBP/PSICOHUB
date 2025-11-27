package com.upc.backend.dtos;

import com.upc.backend.entities.Especialidad;
import com.upc.backend.entities.Paciente;
import com.upc.backend.entities.Psicologo;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CitaDTO {
    private Long idCita;
    private Long codigo;
    private Paciente paciente;
    private Psicologo psicologo;
    private Especialidad especialidad;
    private LocalTime hora;
    private Double precio;
    private String descripcion;
    private String estado = "pendiente";
}
