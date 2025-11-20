package com.upc.backend.security.repositories;

import com.upc.backend.security.entities.ERol;
import com.upc.backend.security.entities.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol, Long> {
    Optional<Rol> findByNombre(ERol nombre);
}