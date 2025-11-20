package com.upc.backend.security.services;

import com.upc.backend.security.dtos.*;
import com.upc.backend.security.repositories.UsuarioRepository;
import com.upc.backend.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final CustomUsuarioDetailsService userDetailsService;
    private final UsuarioRepository usuarioRepository;
    private final JwtUtil jwtUtil;

    public AuthResponseDTO login(AuthRequestDTO request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());

        Set<String> roles = usuarioRepository.findByEmail(request.getEmail())
                .map(u -> u.getRoles().stream().map(r -> r.getNombre().name()).collect(Collectors.toSet()))
                .orElse(Set.of());

        String token = jwtUtil.generateToken(userDetails, roles);

        UsuarioResponseDTO usuarioDTO = usuarioRepository.findByEmail(request.getEmail())
                .map(u -> {
                    UsuarioResponseDTO dto = new UsuarioResponseDTO();
                    dto.setId(u.getId());
                    dto.setEmail(u.getEmail());
                    dto.setDni(u.getDni());
                    dto.setRoles(u.getRoles().stream().map(r -> r.getNombre().name()).collect(Collectors.toSet()));
                    return dto;
                }).orElse(null);

        return new AuthResponseDTO(token, usuarioDTO);
    }
}
