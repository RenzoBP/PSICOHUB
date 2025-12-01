package com.upc.backend.dtos;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CitaDTO {
    private Long idCita;
    private Long pacienteId;
    private String pacienteNombre;
    private String pacienteApellido;
    private String pacienteEmail;
    private Long psicologoId;
    private String psicologoNombre;
    private String psicologoApellido;
    private String psicologoEmail;
    private Long especialidadId;
    private String especialidadNombre;
    private String especialidadCategoria;
    private LocalDate fecha;
    private LocalTime hora;
    private String modalidad;
    private String estado;
    private String motivoConsulta;
    private String motivoCancelacion;
    private String enlaceVirtual;
    private Boolean activo;
}