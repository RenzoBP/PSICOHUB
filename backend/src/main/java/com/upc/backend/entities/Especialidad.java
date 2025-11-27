package com.upc.backend.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "especialidades")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Especialidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_especialidad")
    private Long idEspecialidad;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String categoria;

    @Column(nullable = false)
    private Boolean activo = true;
}
