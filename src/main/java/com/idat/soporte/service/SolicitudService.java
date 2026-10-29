package com.idat.soporte.service;

import com.idat.soporte.dto.SolicitudRequest;
import com.idat.soporte.dto.SolicitudResponse;
import com.idat.soporte.model.Estado;
import java.util.List;

/**
 * Contrato de la lógica de negocio de solicitudes de soporte.
 */
public interface SolicitudService {

    List<SolicitudResponse> listar(Estado estado);

    SolicitudResponse obtenerPorId(Long id);

    SolicitudResponse crear(SolicitudRequest request);

    SolicitudResponse actualizar(Long id, SolicitudRequest request);

    void eliminar(Long id);
}
