package com.upc.backend.services;

import com.upc.backend.dtos.ContactoMensajeDTO;
import com.upc.backend.entities.ContactoMensaje;
import com.upc.backend.interfaces.IContactoMensajeService;
import com.upc.backend.repositories.ContactoMensajeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ContactoMensajeService implements IContactoMensajeService {
    @Autowired
    private ContactoMensajeRepository contactoMensajeRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ContactoMensajeDTO registrar(ContactoMensajeDTO contactoMensajeDTO){
        ContactoMensaje contactoMensaje = modelMapper.map(contactoMensajeDTO, ContactoMensaje.class);
        contactoMensaje.setIdMensaje(null);
        contactoMensaje.setFecha(LocalDate.now());
        ContactoMensaje contactoMensajeGuardado = contactoMensajeRepository.save(contactoMensaje);
        return modelMapper.map(contactoMensajeGuardado, ContactoMensajeDTO.class);
    }

    @Override
    public List<ContactoMensajeDTO> listarTodo(){
        return contactoMensajeRepository.findAll()
                .stream()
                .map(contactoMensaje -> modelMapper.map(contactoMensaje, ContactoMensajeDTO.class))
                .toList();
    }
}
