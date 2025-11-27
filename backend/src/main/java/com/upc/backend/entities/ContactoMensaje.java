package com.upc.backend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "contacto_mensajes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContactoMensaje {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mensaje")
    private Long idMensaje;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String asunto;

    @Column(nullable = false)
    private String mensaje;

    @Column(nullable = false)
    private LocalDate fecha;
}
