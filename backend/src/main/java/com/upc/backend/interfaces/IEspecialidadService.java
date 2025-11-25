package com.upc.backend.interfaces;

import com.upc.backend.dtos.EspecialidadDTO;

import java.util.List;

public interface IEspecialidadService {
    public EspecialidadDTO registrar(EspecialidadDTO especialidadDTO);
    public void eliminar(String nombre);
    public List<EspecialidadDTO> listarPorCategoria(String categoria);
    public List<EspecialidadDTO> listarEspecialidadesActivas();
    public List<EspecialidadDTO> listarTodo();
}
