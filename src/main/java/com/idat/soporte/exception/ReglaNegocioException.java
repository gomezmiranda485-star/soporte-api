package com.idat.soporte.exception;

/**
 * Se lanza cuando se viola una regla de negocio (HTTP 409 / 422).
 */
public class ReglaNegocioException extends RuntimeException {

    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
