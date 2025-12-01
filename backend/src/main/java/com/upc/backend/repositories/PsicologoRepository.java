package com.upc.backend.repositories;

import com.upc.backend.entities.Psicologo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PsicologoRepository extends JpaRepository<Psicologo, Long> {
    @Query("SELECT p FROM Psicologo p WHERE p.activo = true")
    public List<Psicologo> listarPsicologosActivos();

    Optional<Psicologo> findByDni(String dni);
    boolean existsByDni(String dni);
    boolean existsByTelefono(String telefono);
}
