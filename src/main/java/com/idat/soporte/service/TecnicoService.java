package com.idat.soporte.service;

import com.idat.soporte.dto.TecnicoRequest;
import com.idat.soporte.model.Tecnico;
import java.util.List;

/**
 * Contrato de la lógica de negocio de técnicos.
 */
public interface TecnicoService {

    List<Tecnico> listar();

    Tecnico obtenerPorId(Long id);

    Tecnico crear(TecnicoRequest request);

    Tecnico actualizar(Long id, TecnicoRequest request);

    void eliminar(Long id);
}
