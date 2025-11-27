package com.upc.backend.controllers;

import com.upc.backend.dtos.CitaDTO;
import com.upc.backend.dtos.PsicologoDTO;
import com.upc.backend.services.PsicologoService;
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
@RequestMapping("/api/psicologo")
public class PsicologoController {
    @Autowired
    private PsicologoService psicologoService;

    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody PsicologoDTO psicologoDTO) {
        try {
            PsicologoDTO created = psicologoService.registrar(psicologoDTO);
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

    @PreAuthorize("hasRole('PSICOLOGO')")
    @PutMapping("/modificar")
    public ResponseEntity<?> modificar(@RequestBody PsicologoDTO psicologoDTO) {
        try {
            String dni = psicologoDTO.getDni();
            PsicologoDTO psicologoActualizado = psicologoService.modificar(dni, psicologoDTO);
            return ResponseEntity.ok(psicologoActualizado);
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

    @PreAuthorize("hasRole('PSICOLOGO')")
    @GetMapping("/listarPorDni/{dni}")
    public PsicologoDTO listarPorDni(@PathVariable String dni) {
        return psicologoService.listarPorDni(dni);
    }

    @PreAuthorize("hasRole('PSICOLOGO')")
    @GetMapping("/listarPsicologosActivos")
    public List<PsicologoDTO> listarPsicologosActivos() {
        return psicologoService.listarPsicologosActivos();
    }

    @PreAuthorize("hasRole('PSICOLOGO')")
    @GetMapping("/listarPsicologos")
    public List<PsicologoDTO> listarPsicologos() {
        return psicologoService.listarPsicologos();
    }
}

