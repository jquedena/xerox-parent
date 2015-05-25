package com.everis.web.dto;

import java.io.Serializable;

public class Accion implements Serializable {

    private static final long serialVersionUID = 1L;
    private String id;
    private String url;
    private String codigo;
    private String descripcion;

    public Accion(String url, String descripcion) {
        super();
        this.url = url;
        this.descripcion = descripcion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

}
