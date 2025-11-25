package com.upc.backend.services;

import com.upc.backend.dtos.EspecialidadDTO;
import com.upc.backend.entities.Especialidad;
import com.upc.backend.interfaces.IEspecialidadService;
import com.upc.backend.repositories.EspecialidadRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EspecialidadService implements IEspecialidadService {
    @Autowired
    private EspecialidadRepository especialidadRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public EspecialidadDTO registrar(EspecialidadDTO especialidadDTO){
        Especialidad especialidad = modelMapper.map(especialidadDTO, Especialidad.class);
        especialidad.setIdEspecialidad(null);
        Especialidad especialidadGuardada = especialidadRepository.save(especialidad);
        return modelMapper.map(especialidadGuardada, EspecialidadDTO.class);
    }

    @Override
    public void eliminar(String nombre){
        Especialidad especialidad = especialidadRepository.findByNombre(nombre);
        if(especialidad != null){
            especialidad.setActivo(false);
            especialidadRepository.save(especialidad);
        } else {
            throw new RuntimeException("Especialidad no encontrado con nombre: " + nombre);
        }
    }
    @Override
    public List<EspecialidadDTO> listarPorCategoria(String categoria) {
        return especialidadRepository.listarPorCategoria(categoria)
                .stream()
                .map(especialidad -> modelMapper.map(especialidad, EspecialidadDTO.class))
                .toList();
    }

    @Override
    public List<EspecialidadDTO> listarEspecialidadesActivas(){
        return especialidadRepository.listarEspecialidadesActivas()
                .stream()
                .map(especialidad -> modelMapper.map(especialidad, EspecialidadDTO.class))
                .toList();
    }
    @Override
    public List<EspecialidadDTO> listarTodo(){
        return especialidadRepository.findAll()
                .stream()
                .map(especialidad -> modelMapper.map(especialidad, EspecialidadDTO.class))
                .toList();
    }
}
