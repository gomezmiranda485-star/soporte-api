package com.idat.soporte.model;

/**
 * Cliente de la empresa que registra solicitudes de soporte.
 */
public class Cliente extends Persona {

    private String empresa;
    private String telefono;

    public Cliente() {
    }

    public Cliente(Long id, String nombre, String email, String empresa, String telefono) {
        super(id, nombre, email);
        this.empresa = empresa;
        this.telefono = telefono;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
