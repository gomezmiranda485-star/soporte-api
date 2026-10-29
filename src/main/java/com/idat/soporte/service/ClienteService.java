package com.idat.soporte.service;

import com.idat.soporte.dto.ClienteRequest;
import com.idat.soporte.model.Cliente;
import java.util.List;

/**
 * Contrato de la lógica de negocio de clientes.
 */
public interface ClienteService {

    List<Cliente> listar();

    Cliente obtenerPorId(Long id);

    Cliente crear(ClienteRequest request);

    Cliente actualizar(Long id, ClienteRequest request);

    void eliminar(Long id);
}
