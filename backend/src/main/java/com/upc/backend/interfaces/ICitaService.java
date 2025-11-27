package com.upc.backend.interfaces;

import com.upc.backend.dtos.CitaDTO;
import com.upc.backend.dtos.EspecialidadDTO;
import com.upc.backend.dtos.PacienteDTO;
import com.upc.backend.dtos.PsicologoDTO;

import java.util.List;

public interface ICitaService {
    public CitaDTO registrar(CitaDTO citaDTO);
    public CitaDTO modificar(Long codigo, CitaDTO citaDTO);
    public List<CitaDTO> listarPorPaciente(String paciente);
    public List<CitaDTO> listarPorPsicologo(String psicologo);
    public List<CitaDTO> listarPorEspecalidad(String especialidad);
    public List<CitaDTO> listarCitas();
}
