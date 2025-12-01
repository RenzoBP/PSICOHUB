package com.upc.backend.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "citas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cita {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCita;

    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name = "psicologo_id", nullable = false)
    private Psicologo psicologo;

    @ManyToOne
    @JoinColumn(name = "especialidad_id", nullable = false)
    private Especialidad especialidad;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private LocalTime hora;

    @Column(length = 20, nullable = false)
    private String modalidad; // "Virtual" o "Presencial"

    @Column(length = 20, nullable = false)
    private String estado; // "Pendiente", "Confirmada", "Cancelada", "Completada"

    @Column(length = 500)
    private String motivoConsulta;

    @Column(length = 500)
    private String motivoCancelacion;

    @Column(length = 200)
    private String enlaceVirtual; // Para citas virtuales

    @Column(nullable = false)
    private Boolean activo = true;
}