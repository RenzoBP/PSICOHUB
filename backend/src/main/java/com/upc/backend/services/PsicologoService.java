package com.upc.backend.services;

import com.upc.backend.dtos.PacienteDTO;
import com.upc.backend.dtos.PsicologoDTO;
import com.upc.backend.entities.Psicologo;
import com.upc.backend.interfaces.IPsicologoService;
import com.upc.backend.repositories.PsicologoRepository;
import com.upc.backend.security.entities.ERol;
import com.upc.backend.security.entities.Rol;
import com.upc.backend.security.entities.Usuario;
import com.upc.backend.security.repositories.RolRepository;
import com.upc.backend.security.repositories.UsuarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Set;

@Service
public class PsicologoService implements IPsicologoService {
    @Autowired
    private PsicologoRepository psicologoRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private RolRepository rolRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UsuarioRepository usuarioRepository;

    private void validarDatosPaciente(PsicologoDTO psicologoDTO) {
        String dniLimpio = psicologoDTO.getDni().replaceAll("\\s", "");
        if (!dniLimpio.matches("\\d{8}")) {
            throw new RuntimeException("El DNI debe tener exactamente 8 dígitos numéricos");
        }
        psicologoDTO.setDni(dniLimpio);

        String telefonoLimpio = psicologoDTO.getTelefono().replaceAll("\\s", "");
        if (!telefonoLimpio.matches("\\d{9}")) {
            throw new RuntimeException("El teléfono debe tener exactamente 9 dígitos numéricos");
        }
        psicologoDTO.setTelefono(telefonoLimpio);

        if (!psicologoDTO.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new RuntimeException("El formato del email es inválido");
        }

        LocalDate fechaNacimiento = psicologoDTO.getFechaNacimiento();
        LocalDate fechaActual = LocalDate.now();

        int edad = Period.between(fechaNacimiento, fechaActual).getYears();

        if (edad < 18) {
            throw new RuntimeException("Debe ser mayor de edad (18 años o más)");
        }
    }

    @Override
    public PsicologoDTO registrar(PsicologoDTO psicologoDTO) {
        validarDatosPaciente(psicologoDTO);

        if (usuarioRepository.existsByEmail(psicologoDTO.getEmail())) {
            throw new RuntimeException("El email proporcionado ya está registrado");
        }

        if (psicologoRepository.existsByDni(psicologoDTO.getDni())) {
            throw new RuntimeException("El DNI proporcionado ya está registrado");
        }

        if (psicologoRepository.existsByTelefono(psicologoDTO.getTelefono())) {
            throw new RuntimeException("El teléfono proporcionado ya está registrado");
        }

        Rol rol = rolRepository.findByNombre(ERol.ROLE_PSICOLOGO)
                .orElseThrow(() -> new RuntimeException("ROLE_PSICOLOGO no definido"));

        Usuario usuario = Usuario.builder()
                .email(psicologoDTO.getEmail())
                .password(passwordEncoder.encode(psicologoDTO.getPassword()))
                .dni(psicologoDTO.getDni())
                .roles(Set.of(rol))
                .build();

        Usuario guardado = usuarioRepository.save(usuario);

        Psicologo psicologo = modelMapper.map(psicologoDTO, Psicologo.class);
        psicologo.setIdPsicologo(null);
        psicologo.setPassword(passwordEncoder.encode(psicologoDTO.getPassword()));
        psicologo.setActivo(true);
        psicologo.setUsuario(guardado);

        Psicologo psicologoGuardado = psicologoRepository.save(psicologo);
        return modelMapper.map(psicologoGuardado, PsicologoDTO.class);
    }

    @Override
    public PsicologoDTO modificar(String dni, PsicologoDTO psicologoDTO) {
        // Validar solo los campos que vienen en el DTO
        if (psicologoDTO.getDni() != null && !psicologoDTO.getDni().isEmpty()) {
            String dniLimpio = psicologoDTO.getDni().replaceAll("\\s", "");
            if (!dniLimpio.matches("\\d{8}")) {
                throw new RuntimeException("El DNI debe tener exactamente 8 dígitos numéricos");
            }
            psicologoDTO.setDni(dniLimpio);
        }

        if (psicologoDTO.getTelefono() != null && !psicologoDTO.getTelefono().isEmpty()) {
            String telefonoLimpio = psicologoDTO.getTelefono().replaceAll("\\s", "");
            if (!telefonoLimpio.matches("\\d{9}")) {
                throw new RuntimeException("El teléfono debe tener exactamente 9 dígitos numéricos");
            }
            psicologoDTO.setTelefono(telefonoLimpio);
        }

        if (psicologoDTO.getEmail() != null && !psicologoDTO.getEmail().isEmpty()) {
            if (!psicologoDTO.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                throw new RuntimeException("El formato del email es inválido");
            }
        }

        Psicologo psicologoExistente = psicologoRepository.findByDni(dni)
                .orElseThrow(() -> new RuntimeException("Psicólogo no encontrado"));

        Usuario usuarioExistente = psicologoExistente.getUsuario();
        boolean hayCambios = false;

        // Actualizar email si es diferente
        String nuevoEmail = psicologoDTO.getEmail();
        if (nuevoEmail != null && !nuevoEmail.trim().isEmpty() &&
                !usuarioExistente.getEmail().equals(nuevoEmail)) {

            if (usuarioRepository.existsByEmail(nuevoEmail)) {
                throw new RuntimeException("El email ya está registrado por otro usuario");
            }
            usuarioExistente.setEmail(nuevoEmail);
            psicologoExistente.setEmail(nuevoEmail);
            hayCambios = true;
        }

        // Actualizar contraseña si se proporciona
        if (psicologoDTO.getPassword() != null && !psicologoDTO.getPassword().trim().isEmpty()) {
            usuarioExistente.setPassword(passwordEncoder.encode(psicologoDTO.getPassword()));
            psicologoExistente.setPassword(passwordEncoder.encode(psicologoDTO.getPassword()));
            hayCambios = true;
        }

        // Actualizar otros campos si se proporcionan
        if (psicologoDTO.getNombre() != null && !psicologoDTO.getNombre().trim().isEmpty()) {
            psicologoExistente.setNombre(psicologoDTO.getNombre());
            hayCambios = true;
        }

        if (psicologoDTO.getApellido() != null && !psicologoDTO.getApellido().trim().isEmpty()) {
            psicologoExistente.setApellido(psicologoDTO.getApellido());
            hayCambios = true;
        }

        if (psicologoDTO.getGenero() != null && !psicologoDTO.getGenero().trim().isEmpty()) {
            psicologoExistente.setGenero(psicologoDTO.getGenero());
            hayCambios = true;
        }

        if (psicologoDTO.getTelefono() != null && !psicologoDTO.getTelefono().trim().isEmpty()) {
            if (psicologoRepository.existsByTelefono(psicologoDTO.getTelefono()) &&
                    !psicologoExistente.getTelefono().equals(psicologoDTO.getTelefono())) {
                throw new RuntimeException("El teléfono ya está registrado");
            }
            psicologoExistente.setTelefono(psicologoDTO.getTelefono());
            hayCambios = true;
        }

        if (psicologoDTO.getDistrito() != null && !psicologoDTO.getDistrito().trim().isEmpty()) {
            psicologoExistente.setDistrito(psicologoDTO.getDistrito());
            hayCambios = true;
        }

        if (psicologoDTO.getDireccion() != null && !psicologoDTO.getDireccion().trim().isEmpty()) {
            psicologoExistente.setDireccion(psicologoDTO.getDireccion());
            hayCambios = true;
        }

        if (psicologoDTO.getFechaNacimiento() != null) {
            psicologoExistente.setFechaNacimiento(psicologoDTO.getFechaNacimiento());
            hayCambios = true;
        }

        if (!hayCambios) {
            throw new RuntimeException("No se proporcionaron datos para modificar");
        }

        usuarioRepository.save(usuarioExistente);
        Psicologo psicologoActualizado = psicologoRepository.save(psicologoExistente);
        return modelMapper.map(psicologoActualizado, PsicologoDTO.class);
    }

    @Override
    public PsicologoDTO listarPorDni(String dni){
        return psicologoRepository.findByDni(dni)
                .map(psicologo -> modelMapper.map(psicologo, PsicologoDTO.class))
                .orElse(null);
    }

    @Override
    public List<PsicologoDTO> listarPsicologosActivos(){
        return psicologoRepository.listarPsicologosActivos()
                .stream()
                .map(psicologo -> modelMapper.map(psicologo, PsicologoDTO.class))
                .toList();
    }

    @Override
    public List<PsicologoDTO> listarPsicologos(){
        return psicologoRepository.findAll()
                .stream()
                .map(psicologo -> modelMapper.map(psicologo, PsicologoDTO.class))
                .toList();
    }
}

