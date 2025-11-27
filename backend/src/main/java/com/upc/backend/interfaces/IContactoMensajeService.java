package com.upc.backend.interfaces;

import com.upc.backend.dtos.ContactoMensajeDTO;

import java.util.List;

public interface IContactoMensajeService {
    public ContactoMensajeDTO registrar(ContactoMensajeDTO contactoMensajeDTO);
    public List<ContactoMensajeDTO> listarContactoMensajes();
}
