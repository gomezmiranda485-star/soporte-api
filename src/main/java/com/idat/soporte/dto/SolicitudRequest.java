package com.idat.soporte.dto;

import com.idat.soporte.model.Estado;
import com.idat.soporte.model.Prioridad;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Datos de entrada para crear o actualizar una solicitud.
 */
public record SolicitudRequest(

        @NotBlank(message = "El título es obligatorio")
        @Size(min = 5, max = 100, message = "El título debe tener entre 5 y 100 caracteres")
        String titulo,

        @NotBlank(message = "La descripción es obligatoria")
        @Size(max = 500, message = "La descripción no puede superar los 500 caracteres")
        String descripcion,

        @NotNull(message = "La prioridad es obligatoria (BAJA, MEDIA, ALTA, CRITICA)")
        Prioridad prioridad,

        /** Opcional al crear (por defecto PENDIENTE); se usa al actualizar. */
        Estado estado,

        @NotNull(message = "El id del cliente es obligatorio")
        Long clienteId,

        /** Opcional: técnico asignado. */
        Long tecnicoId
) {
}
