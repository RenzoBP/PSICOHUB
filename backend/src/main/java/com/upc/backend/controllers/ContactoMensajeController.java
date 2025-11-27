package com.upc.backend.controllers;

import com.upc.backend.dtos.ContactoMensajeDTO;
import com.upc.backend.services.ContactoMensajeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true", exposedHeaders = "Authorization")
@RequestMapping("/api/contacto")
public class ContactoMensajeController {
    @Autowired
    private ContactoMensajeService contactoMensajeService;

    @PreAuthorize("hasAnyRole('ADMIN', 'PSICOLOGO', 'PACIENTE')")
    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody ContactoMensajeDTO contactoMensajeDTO) {
        try {
            ContactoMensajeDTO created = contactoMensajeService.registrar(contactoMensajeDTO);
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "success", false,
                            "error", "Error de validación",
                            "message", e.getMessage(),
                            "timestamp", LocalDateTime.now().toString()
                    ));
        }
    }

    @GetMapping("/listarContactoMensajes")
    public ResponseEntity<?> listarContactoMensajes() {
        try {
            List<ContactoMensajeDTO> mensajes = contactoMensajeService.listarContactoMensajes();
            return ResponseEntity.ok(mensajes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "success", false,
                            "message", e.getMessage()
                    ));
        }
    }
}
