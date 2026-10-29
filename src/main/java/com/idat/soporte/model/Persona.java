package com.idat.soporte.model;

/**
 * Clase abstracta que agrupa los datos comunes de Cliente y Técnico.
 * Aplica abstracción y herencia (reutilización de código).
 */
public abstract class Persona {

    private Long id;
    private String nombre;
    private String email;

    protected Persona() {
    }

    protected Persona(Long id, String nombre, String email) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
