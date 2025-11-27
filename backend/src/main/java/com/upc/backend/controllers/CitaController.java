package com.upc.backend.controllers;

import com.upc.backend.dtos.CitaDTO;
import com.upc.backend.dtos.ContactoMensajeDTO;
import com.upc.backend.dtos.PsicologoDTO;
import com.upc.backend.entities.Cita;
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
@RequestMapping("/api/cita")
public class CitaController {
    @Autowired
    private CitaService citaService;

    @PreAuthorize("hasAnyRole('ADMIN', 'PSICOLOGO', 'PACIENTE')")
    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody CitaDTO citaDTO) {
        try {
            CitaDTO created = citaService.registrar(citaDTO);
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
    @PutMapping("/modificar/{codigo}")
    public ResponseEntity<?> modificar(@PathVariable Long codigo, @RequestBody CitaDTO citaDTO) {
        try {
            CitaDTO citaActualizada = citaService.modificar(codigo, citaDTO);
            return ResponseEntity.ok(citaActualizada);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "success", false,
                            "error", "Error de validación",
                            "message", e.getMessage(),
                            "timestamp", LocalDateTime.now().toString()
                    ));
        }
    }
    @GetMapping("/listarPorPaciente/{dni}")
    public ResponseEntity<?> listarPorPaciente(@PathVariable String paciente) {
        try {
            List<CitaDTO> citas = citaService.listarPorPaciente(paciente);
            return ResponseEntity.ok(citas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "success", false,
                            "message", e.getMessage()
                    ));
        }
    }
        @GetMapping("/listarPorPsicologo/{psicologo}")
    public ResponseEntity<?> listarPorPsicologo(@PathVariable String psicologo) {
        try {
            List<CitaDTO> citas = citaService.listarPorPsicologo(psicologo);
            return ResponseEntity.ok(citas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "success", false,
                            "message", e.getMessage()
                    ));
        }
    }
    @GetMapping("/listarPorEspecialidad/{especialidad}")
    public ResponseEntity<?> listarPorEspecialidad(@PathVariable String especialidad) {
        try {
            List<CitaDTO> citas = citaService.listarPorEspecalidad(especialidad);
            return ResponseEntity.ok(citas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "success", false,
                            "message", e.getMessage()
                    ));
        }
    }
    @GetMapping("/listarCitas")
    public ResponseEntity<?> listarCitas() {
        try {
            List<CitaDTO> citas = citaService.listarCitas();
            return ResponseEntity.ok(citas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "success", false,
                            "message", e.getMessage()
                    ));
        }
    }
}
