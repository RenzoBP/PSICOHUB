package com.upc.backend.controllers;

import com.upc.backend.dtos.PacienteDTO;
import com.upc.backend.services.PacienteService;
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
@RequestMapping("/api/paciente")
public class PacienteController {
    @Autowired
    private PacienteService pacienteService;

    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody PacienteDTO pacienteDTO) {
        try {
            PacienteDTO created = pacienteService.registrar(pacienteDTO);
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

    @PreAuthorize("hasRole('PACIENTE')")
    @PutMapping("/modificar")
    public ResponseEntity<?> modificar(@RequestBody PacienteDTO pacienteDTO) {
        try {
            String dni = pacienteDTO.getDni();
            PacienteDTO pacienteActualizado = pacienteService.modificar(dni, pacienteDTO);
            return ResponseEntity.ok(pacienteActualizado);
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

    @PreAuthorize("hasRole('PACIENTE')")
    @GetMapping("/listarPorDni/{dni}")
    public PacienteDTO listarPorDni(@PathVariable String dni) {
        return pacienteService.listarPorDni(dni);
    }

    @PreAuthorize("hasRole('PACIENTE')")
    @GetMapping("/listarPacientesActivos")
    public List<PacienteDTO> listarPacientesActivos() {
        return pacienteService.listarPacientesActivos();
    }

    @PreAuthorize("hasRole('PACIENTE')")
    @GetMapping("/listarTodo")
    public List<PacienteDTO> listarTodos() {
        return pacienteService.listarTodos();
    }
}
