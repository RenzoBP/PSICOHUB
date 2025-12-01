package com.upc.backend.dtos;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CitaRequestDTO {
    private Long pacienteId;
    private Long psicologoId;
    private Long especialidadId;
    private LocalDate fecha;
    private LocalTime hora;
    private String modalidad;
    private String motivoConsulta;
}