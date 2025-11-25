package com.upc.backend.repositories;

import com.upc.backend.entities.ContactoMensaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactoMensajeRepository extends JpaRepository<ContactoMensaje, Long> {
}
