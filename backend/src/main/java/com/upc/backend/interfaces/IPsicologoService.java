package com.upc.backend.interfaces;

import com.upc.backend.dtos.PacienteDTO;
import com.upc.backend.dtos.PsicologoDTO;

import java.util.List;

public interface IPsicologoService {
    public PsicologoDTO registrar(PsicologoDTO psicologoDTO);
    public PsicologoDTO modificar(String dni, PsicologoDTO psicologoDTO);
    public PsicologoDTO listarPorDni(String dni);
    public List<PsicologoDTO> listarPsicologosActivos();
    public List<PsicologoDTO> listarTodos();
}
