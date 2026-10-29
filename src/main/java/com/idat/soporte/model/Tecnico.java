package com.idat.soporte.model;

/**
 * Técnico encargado de atender las solicitudes de soporte.
 */
public class Tecnico extends Persona {

    private String especialidad;
    private boolean disponible;

    public Tecnico() {
    }

    public Tecnico(Long id, String nombre, String email, String especialidad, boolean disponible) {
        super(id, nombre, email);
        this.especialidad = especialidad;
        this.disponible = disponible;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
}
