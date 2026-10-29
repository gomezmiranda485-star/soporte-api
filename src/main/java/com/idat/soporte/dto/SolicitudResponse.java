package com.idat.soporte.dto;

import com.idat.soporte.model.Estado;
import com.idat.soporte.model.Prioridad;
import java.time.LocalDateTime;

/**
 * Datos de salida de una solicitud (incluye nombres de cliente y técnico).
 */
public record SolicitudResponse(
        Long id,
        String titulo,
        String descripcion,
        Prioridad prioridad,
        Estado estado,
        Long clienteId,
        String clienteNombre,
        Long tecnicoId,
        String tecnicoNombre,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaActualizacion
) {
}
