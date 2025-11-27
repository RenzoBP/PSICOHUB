package com.upc.backend.repositories;

import com.upc.backend.entities.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    @Query("SELECT p FROM Paciente p WHERE p.activo = true")
    public List<Paciente> listarPacientesActivos();

    Optional<Paciente> findByDni(String dni);
    boolean existsByDni(String dni);
    boolean existsByTelefono(String telefono);

    Paciente findByNombre(String nombre);
}
