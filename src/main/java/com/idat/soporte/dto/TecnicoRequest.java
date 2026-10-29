package com.idat.soporte.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Datos de entrada para crear o actualizar un técnico.
 */
public record TecnicoRequest(

        @NotBlank(message = "El nombre es obligatorio")
        @Size(min = 3, max = 80, message = "El nombre debe tener entre 3 y 80 caracteres")
        String nombre,

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El email no tiene un formato válido")
        String email,

        @NotBlank(message = "La especialidad es obligatoria")
        String especialidad,

        @NotNull(message = "Debe indicar si el técnico está disponible")
        Boolean disponible
) {
}
