package com.upc.backend.security.services;

import com.upc.backend.dtos.PacienteDTO;
import com.upc.backend.dtos.PsicologoDTO;
import com.upc.backend.security.dtos.UsuarioRequestDTO;
import com.upc.backend.security.dtos.UsuarioResponseDTO;
import com.upc.backend.security.entities.*;
import com.upc.backend.security.repositories.RolRepository;
import com.upc.backend.security.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper; // registra bean en config

    public UsuarioResponseDTO crearUsuarioPaciente(UsuarioRequestDTO dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email ya registrado");
        }

        Rol rol = rolRepository.findByNombre(ERol.ROLE_PACIENTE)
                .orElseThrow(() -> new RuntimeException("ROLE_PACIENTE no definido"));

        Usuario usuario = Usuario.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .roles(Set.of(rol))
                .build();

        Usuario guardado = usuarioRepository.save(usuario);
        return toDto(guardado);
    }

    public UsuarioResponseDTO toDto(Usuario u) {
        UsuarioResponseDTO r = new UsuarioResponseDTO();
        r.setId(u.getId());
        r.setEmail(u.getEmail());
        r.setRoles(u.getRoles().stream().map(rn -> rn.getNombre().name()).collect(Collectors.toSet()));
        return r;
    }
}
