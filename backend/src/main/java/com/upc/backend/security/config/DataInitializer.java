package com.upc.backend.security.config;

import com.upc.backend.security.entities.ERol;
import com.upc.backend.security.entities.Rol;
import com.upc.backend.security.repositories.RolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {
    private final RolRepository rolRepository;

    @Bean
    public CommandLineRunner initRoles() {
        return args -> {
            if (rolRepository.findByNombre(ERol.ROLE_ADMIN).isEmpty()) {
                rolRepository.save(Rol.builder().nombre(ERol.ROLE_ADMIN).build());
            }
            if (rolRepository.findByNombre(ERol.ROLE_PSICOLOGO).isEmpty()) {
                rolRepository.save(Rol.builder().nombre(ERol.ROLE_PSICOLOGO).build());
            }
            if (rolRepository.findByNombre(ERol.ROLE_PACIENTE).isEmpty()) {
                rolRepository.save(Rol.builder().nombre(ERol.ROLE_PACIENTE).build());
            }
        };
    }
}