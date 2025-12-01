package com.upc.backend.interfaces;

import com.upc.backend.dtos.CitaDTO;
import com.upc.backend.dtos.CitaRequestDTO;
import java.util.List;

public interface ICitaService {
    CitaDTO registrar(CitaRequestDTO citaRequestDTO);
    CitaDTO modificar(Long id, CitaRequestDTO citaRequestDTO);
    void cancelar(Long id, String motivoCancelacion);
    void confirmar(Long id);
    void completar(Long id);
    CitaDTO listarPorId(Long id);
    List<CitaDTO> listarPorPaciente(Long pacienteId);
    List<CitaDTO> listarPorPsicologo(Long psicologoId);
    List<CitaDTO> listarPorEstado(String estado);
    List<CitaDTO> listarTodo();
}