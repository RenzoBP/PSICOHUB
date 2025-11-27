package com.upc.backend.repositories;

import com.upc.backend.entities.Cita;
import com.upc.backend.entities.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {
    @Query("SELECT c FROM Cita c WHERE c.especialidad = :especialidad and c.estado = 'pendiente' ")
    public List<Cita> listarPorEspecialidad(String especialidad);

    @Query("SELECT c FROM Cita c WHERE c.psicologo = :psicologo and c.estado = 'pendiente' ")
    public List<Cita> listarPorPsicologo(String psicologo);

    @Query("SELECT c FROM Cita c WHERE c.paciente = :paciente and c.estado = 'pendiente' ")
    public List<Cita> listarPorPaciente(String paciente);

    public Cita findByCodigo(Long codigo);
}
