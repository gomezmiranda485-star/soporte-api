package com.idat.soporte.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Estructura uniforme para todas las respuestas de error de la API.
 */
public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String mensaje,
        String ruta,
        List<String> detalles
) {
}
