package com.upc.backend.repositories;

import com.upc.backend.entities.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {
    @Query("SELECT c FROM Cita c WHERE c.paciente.idPaciente = :pacienteId AND c.activo = true")
    List<Cita> findByPacienteId(Long pacienteId);

    @Query("SELECT c FROM Cita c WHERE c.psicologo.idPsicologo = :psicologoId AND c.activo = true")
    List<Cita> findByPsicologoId(Long psicologoId);

    @Query("SELECT c FROM Cita c WHERE c.estado = :estado AND c.activo = true")
    List<Cita> findByEstado(String estado);

    @Query("SELECT c FROM Cita c WHERE c.psicologo.idPsicologo = :psicologoId AND c.fecha = :fecha AND c.activo = true")
    List<Cita> findByPsicologoAndFecha(Long psicologoId, LocalDate fecha);

    @Query("SELECT c FROM Cita c WHERE c.activo = true ORDER BY c.fecha DESC, c.hora DESC")
    List<Cita> findAllActive();
}