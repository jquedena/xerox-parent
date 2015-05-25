package com.everis.web.model;

import java.io.Serializable;
import java.util.List;

import com.everis.enums.Resultado;
import com.everis.web.dto.Accion;

public class BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;
    private Resultado tipoResultado;
    private String mensaje;
    private List<Accion> acciones;

    public Resultado getTipoResultado() {
        return tipoResultado;
    }

    public void setTipoResultado(Resultado tipoResultado) {
        this.tipoResultado = tipoResultado;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public List<Accion> getAcciones() {
        return acciones;
    }

    public void setAcciones(List<Accion> acciones) {
        this.acciones = acciones;
    }
}
