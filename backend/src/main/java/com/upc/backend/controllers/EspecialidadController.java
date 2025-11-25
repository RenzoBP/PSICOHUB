package com.upc.backend.controllers;

import com.upc.backend.dtos.EspecialidadDTO;
import com.upc.backend.dtos.PacienteDTO;
import com.upc.backend.dtos.PsicologoDTO;
import com.upc.backend.services.EspecialidadService;
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
@RequestMapping("/api/especialidad")
public class EspecialidadController {
    @Autowired
    private EspecialidadService especialidadService;

    @PreAuthorize("hasAnyRole('ADMIN', 'PSICOLOGO')")
    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody EspecialidadDTO especialidadDTO) {
        try {
            EspecialidadDTO created = especialidadService.registrar(especialidadDTO);
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

    @PreAuthorize("hasAnyRole('ADMIN', 'PSICOLOGO')")
    @DeleteMapping("/eliminar/{nombre}")
    public ResponseEntity<?> eliminar(@PathVariable String nombre) {
        try {
            especialidadService.eliminar(nombre);
            return ResponseEntity.ok(
                    Map.of(
                            "success", true,
                            "message", "Especialidad eliminada correctamente",
                            "timestamp", LocalDateTime.now().toString()
                    )
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "success", false,
                            "error", "Error al eliminar",
                            "message", e.getMessage(),
                            "timestamp", LocalDateTime.now().toString()
                    ));
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PSICOLOGO')")
    @GetMapping("/listarPorCategoria/{categoria}")
    public List<EspecialidadDTO> listarPorCategoria(@PathVariable String categoria) {
        return especialidadService.listarPorCategoria(categoria);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PSICOLOGO')")
    @GetMapping("/listarEspecialidadesActivas")
    public List<EspecialidadDTO> listarEspecialidadesActivas() {
        return especialidadService.listarEspecialidadesActivas();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PSICOLOGO')")
    @GetMapping("/listarTodo")
    public List<EspecialidadDTO> listarTodo() {
        return especialidadService.listarTodo();
    }
}
