package com.everis.core.enums;

public enum Estado {

    ACTIVO("A", "Activo"), INACTIVO("I", "Inactivo");

    private String estado;
    private String descripcion;

    Estado(String estado, String descripcion) {
        this.estado = estado;
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return estado;
    }
}
