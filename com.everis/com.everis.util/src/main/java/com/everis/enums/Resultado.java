package com.everis.enums;

/**
 * @author jquedena
 */
public enum Resultado {

    EXITO("EXITO"),
    ERROR("ERROR"),
    ADVERTENCIA("ADVERTENCIA"),
    PREGUNTA("PREGUNTA"),
    SIN_REGISTROS("SIN_REGISTROS"),
    SIN_PERMISOS("SIN_PERMISOS"),
    CONFLICTO("CONFLICTO"),
    ERROR_SISTEMA("ERROR_SISTEMA");

    private String tipoResultado;

    Resultado(String tipoResultado) {
        this.tipoResultado = tipoResultado;
    }

    public String toString() {
        return tipoResultado;
    }
}
