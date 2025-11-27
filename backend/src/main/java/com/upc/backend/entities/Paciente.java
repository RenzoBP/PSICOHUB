package com.upc.backend.entities;

import com.upc.backend.security.entities.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "pacientes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Paciente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_paciente")
    private Long idPaciente;

    @Column(length = 50, nullable = false)
    private String nombre;

    @Column(length = 50, nullable = false)
    private String apellido;

    @Column(length = 8, nullable = false, unique = true)
    private String dni;

    @Column(length = 50, nullable = false)
    private LocalDate fechaNacimiento;

    @Column(length = 50, nullable = false)
    private String genero;

    @Column(length = 50, nullable = false)
    private String distrito;

    @Column(length = 50, nullable = false)
    private String direccion;

    @Column(length = 9, nullable = false)
    private String telefono;

    @Column(length = 50, nullable = false, unique = true)
    private String email;

    @Column(length = 255, nullable = false)
    private String password ;

    @Column(nullable = false)
    private Boolean activo = true;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    private Usuario usuario;
}

