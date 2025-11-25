package com.upc.backend.repositories;

import com.upc.backend.entities.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EspecialidadRepository extends JpaRepository<Especialidad, Long> {
    @Query("SELECT e FROM Especialidad e WHERE e.activo = true")
    public List<Especialidad> listarEspecialidadesActivas();

    @Query("SELECT e FROM Especialidad e WHERE e.categoria = :categoria and e.activo = true ")
    public List<Especialidad> listarPorCategoria(String categoria);

    Especialidad findByNombre(String nombre);
}
