package com.upc.backend.services;

import com.upc.backend.dtos.CitaDTO;
import com.upc.backend.dtos.CitaRequestDTO;
import com.upc.backend.entities.*;
import com.upc.backend.interfaces.ICitaService;
import com.upc.backend.repositories.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CitaService implements ICitaService {
    @Autowired
    private CitaRepository citaRepository;
    @Autowired
    private PacienteRepository pacienteRepository;
    @Autowired
    private PsicologoRepository psicologoRepository;
    @Autowired
    private EspecialidadRepository especialidadRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CitaDTO registrar(CitaRequestDTO citaRequestDTO) {
        // Validar que la fecha no sea pasada
        if (citaRequestDTO.getFecha().isBefore(LocalDate.now())) {
            throw new RuntimeException("No se pueden agendar citas en fechas pasadas");
        }

        // Validar que el paciente existe
        Paciente paciente = pacienteRepository.findById(citaRequestDTO.getPacienteId())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        // Validar que el psicólogo existe
        Psicologo psicologo = psicologoRepository.findById(citaRequestDTO.getPsicologoId())
                .orElseThrow(() -> new RuntimeException("Psicólogo no encontrado"));

        // Validar que la especialidad existe
        Especialidad especialidad = especialidadRepository.findById(citaRequestDTO.getEspecialidadId())
                .orElseThrow(() -> new RuntimeException("Especialidad no encontrada"));

        // Validar disponibilidad del psicólogo
        List<Cita> citasExistentes = citaRepository.findByPsicologoAndFecha(
                citaRequestDTO.getPsicologoId(),
                citaRequestDTO.getFecha()
        );

        boolean horaOcupada = citasExistentes.stream()
                .anyMatch(c -> c.getHora().equals(citaRequestDTO.getHora()) &&
                        !c.getEstado().equals("Cancelada"));

        if (horaOcupada) {
            throw new RuntimeException("El psicólogo no está disponible en ese horario");
        }

        Cita cita = new Cita();
        cita.setPaciente(paciente);
        cita.setPsicologo(psicologo);
        cita.setEspecialidad(especialidad);
        cita.setFecha(citaRequestDTO.getFecha());
        cita.setHora(citaRequestDTO.getHora());
        cita.setModalidad(citaRequestDTO.getModalidad());
        cita.setEstado("Pendiente");
        cita.setMotivoConsulta(citaRequestDTO.getMotivoConsulta());
        cita.setActivo(true);

        // Generar enlace virtual si es necesario
        if ("Virtual".equals(citaRequestDTO.getModalidad())) {
            cita.setEnlaceVirtual("https://meet.psicohub.com/" + System.currentTimeMillis());
        }

        Cita citaGuardada = citaRepository.save(cita);
        return convertToDTO(citaGuardada);
    }

    @Override
    public CitaDTO modificar(Long id, CitaRequestDTO citaRequestDTO) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        if (!"Pendiente".equals(cita.getEstado())) {
            throw new RuntimeException("Solo se pueden modificar citas en estado Pendiente");
        }

        if (citaRequestDTO.getFecha() != null) {
            if (citaRequestDTO.getFecha().isBefore(LocalDate.now())) {
                throw new RuntimeException("No se pueden agendar citas en fechas pasadas");
            }
            cita.setFecha(citaRequestDTO.getFecha());
        }

        if (citaRequestDTO.getHora() != null) {
            cita.setHora(citaRequestDTO.getHora());
        }

        if (citaRequestDTO.getModalidad() != null) {
            cita.setModalidad(citaRequestDTO.getModalidad());
            if ("Virtual".equals(citaRequestDTO.getModalidad()) && cita.getEnlaceVirtual() == null) {
                cita.setEnlaceVirtual("https://meet.psicohub.com/" + System.currentTimeMillis());
            }
        }

        if (citaRequestDTO.getMotivoConsulta() != null) {
            cita.setMotivoConsulta(citaRequestDTO.getMotivoConsulta());
        }

        Cita citaActualizada = citaRepository.save(cita);
        return convertToDTO(citaActualizada);
    }

    @Override
    public void cancelar(Long id, String motivoCancelacion) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        if ("Completada".equals(cita.getEstado()) || "Cancelada".equals(cita.getEstado())) {
            throw new RuntimeException("No se puede cancelar una cita ya completada o cancelada");
        }

        cita.setEstado("Cancelada");
        cita.setMotivoCancelacion(motivoCancelacion);
        citaRepository.save(cita);
    }

    @Override
    public void confirmar(Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        if (!"Pendiente".equals(cita.getEstado())) {
            throw new RuntimeException("Solo se pueden confirmar citas en estado Pendiente");
        }

        cita.setEstado("Confirmada");
        citaRepository.save(cita);
    }

    @Override
    public void completar(Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        if (!"Confirmada".equals(cita.getEstado())) {
            throw new RuntimeException("Solo se pueden completar citas confirmadas");
        }

        cita.setEstado("Completada");
        citaRepository.save(cita);
    }

    @Override
    public CitaDTO listarPorId(Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));
        return convertToDTO(cita);
    }

    @Override
    public List<CitaDTO> listarPorPaciente(Long pacienteId) {
        return citaRepository.findByPacienteId(pacienteId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CitaDTO> listarPorPsicologo(Long psicologoId) {
        return citaRepository.findByPsicologoId(psicologoId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CitaDTO> listarPorEstado(String estado) {
        return citaRepository.findByEstado(estado)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CitaDTO> listarTodo() {
        return citaRepository.findAllActive()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private CitaDTO convertToDTO(Cita cita) {
        CitaDTO dto = new CitaDTO();
        dto.setIdCita(cita.getIdCita());
        dto.setPacienteId(cita.getPaciente().getIdPaciente());
        dto.setPacienteNombre(cita.getPaciente().getNombre());
        dto.setPacienteApellido(cita.getPaciente().getApellido());
        dto.setPacienteEmail(cita.getPaciente().getEmail());
        dto.setPsicologoId(cita.getPsicologo().getIdPsicologo());
        dto.setPsicologoNombre(cita.getPsicologo().getNombre());
        dto.setPsicologoApellido(cita.getPsicologo().getApellido());
        dto.setPsicologoEmail(cita.getPsicologo().getEmail());
        dto.setEspecialidadId(cita.getEspecialidad().getIdEspecialidad());
        dto.setEspecialidadNombre(cita.getEspecialidad().getNombre());
        dto.setEspecialidadCategoria(cita.getEspecialidad().getCategoria());
        dto.setFecha(cita.getFecha());
        dto.setHora(cita.getHora());
        dto.setModalidad(cita.getModalidad());
        dto.setEstado(cita.getEstado());
        dto.setMotivoConsulta(cita.getMotivoConsulta());
        dto.setMotivoCancelacion(cita.getMotivoCancelacion());
        dto.setEnlaceVirtual(cita.getEnlaceVirtual());
        dto.setActivo(cita.getActivo());
        return dto;
    }
}