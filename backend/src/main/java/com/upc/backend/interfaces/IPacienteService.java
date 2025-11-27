package com.upc.backend.interfaces;

import com.upc.backend.dtos.PacienteDTO;

import java.util.List;

public interface IPacienteService {
    public PacienteDTO registrar(PacienteDTO pacienteDTO);
    public PacienteDTO modificar(String dni, PacienteDTO pacienteDTO);
    public PacienteDTO listarPorDni(String dni);
    public List<PacienteDTO> listarPacientesActivos();
    public List<PacienteDTO> listarPacientes();
}