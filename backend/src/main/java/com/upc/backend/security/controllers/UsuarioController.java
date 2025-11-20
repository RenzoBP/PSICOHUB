package com.upc.backend.security.controllers;

import com.upc.backend.security.dtos.UsuarioResponseDTO;
import com.upc.backend.security.entities.Usuario;
import com.upc.backend.security.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioRepository usuarioRepository;

    @GetMapping("/me")
    public ResponseEntity<?> me(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            if (userDetails == null) {
                throw new RuntimeException("Usuario no autenticado");
            }

            String email = userDetails.getUsername();
            Usuario u = usuarioRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            UsuarioResponseDTO dto = new UsuarioResponseDTO();
            dto.setId(u.getId());
            dto.setEmail(u.getEmail());
            dto.setDni(u.getDni());
            dto.setRoles(u.getRoles().stream()
                    .map(r -> r.getNombre().name())
                    .collect(Collectors.toSet()));

            return ResponseEntity.ok(dto);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "success", false,
                            "error", "Error de autenticación",
                            "message", e.getMessage(),
                            "timestamp", LocalDateTime.now().toString()
                    ));
        }
    }
}
