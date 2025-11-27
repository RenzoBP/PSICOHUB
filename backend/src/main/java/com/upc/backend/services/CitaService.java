package com.upc.backend.services;

import com.upc.backend.dtos.CitaDTO;
import com.upc.backend.entities.Cita;
import com.upc.backend.entities.Especialidad;
import com.upc.backend.entities.Paciente;
import com.upc.backend.entities.Psicologo;
import com.upc.backend.interfaces.ICitaService;
import com.upc.backend.repositories.CitaRepository;
import com.upc.backend.repositories.EspecialidadRepository;
import com.upc.backend.repositories.PacienteRepository;
import com.upc.backend.repositories.PsicologoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CitaService implements ICitaService {
    @Autowired
    private CitaRepository citaRepository;
    @Autowired
    private EspecialidadRepository especialidadRepository;
    @Autowired
    private PsicologoRepository psicologoRepository;
    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CitaDTO registrar(CitaDTO citaDTO) {
        Paciente paciente = pacienteRepository.findByNombre(citaDTO.getPaciente().getNombre());
        Psicologo psicologo = psicologoRepository.findByNombre(citaDTO.getPsicologo().getNombre());
        Especialidad especialidad = especialidadRepository.findByNombre(citaDTO.getEspecialidad().getNombre());

        Cita cita = modelMapper.map(citaDTO, Cita.class);

        cita.setPaciente(paciente);
        cita.setPsicologo(psicologo);
        cita.setEspecialidad(especialidad);

        cita.setIdCita(null);

        Cita citaGuardada = citaRepository.save(cita);

        return modelMapper.map(citaGuardada, CitaDTO.class);
    }
    @Override
    public CitaDTO modificar(Long codigo, CitaDTO citaDTO) {
        Cita citaExistente = citaRepository.findByCodigo(codigo);

        if (citaExistente == null) {
            throw new RuntimeException("Cita no encontrada con el código: " + codigo);
        }

        modelMapper.map(citaDTO, citaExistente);
        citaExistente.setIdCita(codigo);

        Cita citaModificada = citaRepository.save(citaExistente);

        return modelMapper.map(citaModificada, CitaDTO.class);
    }
    @Override
    public List<CitaDTO> listarPorPaciente(String paciente){
        return citaRepository.listarPorPaciente(paciente)
                .stream()
                .map(cita -> modelMapper.map(cita, CitaDTO.class))
                .toList();
    }
    @Override
    public List<CitaDTO> listarPorPsicologo(String psicologo){
        return citaRepository.listarPorPsicologo(psicologo)
                .stream()
                .map(cita -> modelMapper.map(cita, CitaDTO.class))
                .toList();
    }
    @Override
    public List<CitaDTO> listarPorEspecalidad(String especialidad){
        return citaRepository.listarPorEspecialidad(especialidad)
                .stream()
                .map(cita -> modelMapper.map(cita, CitaDTO.class))
                .toList();
    }
    @Override
    public List<CitaDTO> listarCitas(){
        return citaRepository.findAll()
                .stream()
                .map(cita -> modelMapper.map(cita, CitaDTO.class))
                .toList();
    }
}
