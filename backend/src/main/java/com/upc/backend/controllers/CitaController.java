package com.upc.backend.controllers;

import com.upc.backend.dtos.CitaDTO;
import com.upc.backend.dtos.CitaRequestDTO;
import com.upc.backend.services.CitaService;
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
@RequestMapping("/api/citas")
public class CitaController {
    @Autowired
    private CitaService citaService;

    @PreAuthorize("hasAnyRole('ADMIN', 'PSICOLOGO', 'PACIENTE')")
    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody CitaRequestDTO citaRequestDTO) {
        try {
            CitaDTO created = citaService.registrar(citaRequestDTO);
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

    @PreAuthorize("hasAnyRole('ADMIN', 'PSICOLOGO', 'PACIENTE')")
    @PutMapping("/modificar/{id}")
    public ResponseEntity<?> modificar(@PathVariable Long id, @RequestBody CitaRequestDTO citaRequestDTO) {
        try {
            CitaDTO updated = citaService.modificar(id, citaRequestDTO);
            return ResponseEntity.ok(updated);
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

    @PreAuthorize("hasAnyRole('ADMIN', 'PSICOLOGO', 'PACIENTE')")
    @PutMapping("/cancelar/{id}")
    public ResponseEntity<?> cancelar(@PathVariable Long id, @RequestBody Map<String, String> body) {
        try {
            String motivoCancelacion = body.get("motivoCancelacion");
            citaService.cancelar(id, motivoCancelacion);
            return ResponseEntity.ok(
                    Map.of(
                            "success", true,
                            "message", "Cita cancelada correctamente",
                            "timestamp", LocalDateTime.now().toString()
                    )
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "success", false,
                            "error", "Error al cancelar",
                            "message", e.getMessage(),
                            "timestamp", LocalDateTime.now().toString()
                    ));
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PSICOLOGO')")
    @PutMapping("/confirmar/{id}")
    public ResponseEntity<?> confirmar(@PathVariable Long id) {
        try {
            citaService.confirmar(id);
            return ResponseEntity.ok(
                    Map.of(
                            "success", true,
                            "message", "Cita confirmada correctamente",
                            "timestamp", LocalDateTime.now().toString()
                    )
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "success", false,
                            "error", "Error al confirmar",
                            "message", e.getMessage(),
                            "timestamp", LocalDateTime.now().toString()
                    ));
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PSICOLOGO')")
    @PutMapping("/completar/{id}")
    public ResponseEntity<?> completar(@PathVariable Long id) {
        try {
            citaService.completar(id);
            return ResponseEntity.ok(
                    Map.of(
                            "success", true,
                            "message", "Cita completada correctamente",
                            "timestamp", LocalDateTime.now().toString()
                    )
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "success", false,
                            "error", "Error al completar",
                            "message", e.getMessage(),
                            "timestamp", LocalDateTime.now().toString()
                    ));
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PSICOLOGO', 'PACIENTE')")
    @GetMapping("/listar/{id}")
    public ResponseEntity<?> listarPorId(@PathVariable Long id) {
        try {
            CitaDTO cita = citaService.listarPorId(id);
            return ResponseEntity.ok(cita);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "success", false,
                            "error", "Cita no encontrada",
                            "message", e.getMessage(),
                            "timestamp", LocalDateTime.now().toString()
                    ));
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PACIENTE')")
    @GetMapping("/paciente/{pacienteId}")
    public List<CitaDTO> listarPorPaciente(@PathVariable Long pacienteId) {
        return citaService.listarPorPaciente(pacienteId);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PSICOLOGO')")
    @GetMapping("/psicologo/{psicologoId}")
    public List<CitaDTO> listarPorPsicologo(@PathVariable Long psicologoId) {
        return citaService.listarPorPsicologo(psicologoId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/estado/{estado}")
    public List<CitaDTO> listarPorEstado(@PathVariable String estado) {
        return citaService.listarPorEstado(estado);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/listarTodo")
    public List<CitaDTO> listarTodo() {
        return citaService.listarTodo();
    }
}