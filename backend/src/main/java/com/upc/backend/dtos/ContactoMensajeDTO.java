package com.upc.backend.dtos;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContactoMensajeDTO {
    private Long idMensaje;
    private String nombre;
    private String email;
    private String asunto;
    private String mensaje;
    private LocalDate fecha;
}
