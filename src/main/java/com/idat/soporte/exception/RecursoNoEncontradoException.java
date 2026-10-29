package com.idat.soporte.exception;

/**
 * Se lanza cuando no existe un recurso con el id solicitado (HTTP 404).
 */
public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String recurso, Long id) {
        super(recurso + " con id " + id + " no fue encontrado");
    }
}
