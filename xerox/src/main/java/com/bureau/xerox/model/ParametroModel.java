package com.bureau.xerox.model;

import java.util.ArrayList;
import java.util.List;

import com.bureau.xerox.domain.Parametro;
import com.everis.core.enums.Estado;
import com.everis.web.model.BaseModel;

public class ParametroModel extends BaseModel {

    private static final long serialVersionUID = 1L;
    private Parametro item;
    private List<Parametro> listaTipos;
    private List<Parametro> listaPadres;
    private List<Parametro> listaPadresPermiteHijos;
    private List<Parametro> listaParametria;
    private long idTipo;
    private long idPadre;
    private String idEstado;
    private String nombre;
    private String fecha;

    public Parametro getItem() {
        return item;
    }

    public void setItem(Parametro item) {
        this.item = item;
    }

    public List<Parametro> getListaTipos() {
        return listaTipos;
    }

    public void setListaTipos(List<Parametro> listaTipos) {
        this.listaTipos = listaTipos;
    }

    public List<Parametro> getListaPadres() {
        return listaPadres;
    }

    public void setListaPadres(List<Parametro> listaPadres) {
        this.listaPadres = listaPadres;
    }

    public List<Estado> getListaEstados() {
        List<Estado> listEstado = new ArrayList<Estado>();
        for (Estado e : Estado.values()) {
            listEstado.add(e);
        }
        return listEstado;
    }

    public List<Parametro> getListaParametria() {
        return listaParametria;
    }

    public void setListaParametria(List<Parametro> listaParametria) {
        this.listaParametria = listaParametria;
    }

    public long getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(long idTipo) {
        this.idTipo = idTipo;
    }

    public long getIdPadre() {
        return idPadre;
    }

    public void setIdPadre(long idPadre) {
        this.idPadre = idPadre;
    }

    public String getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(String idEstado) {
        this.idEstado = idEstado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Parametro> getListaPadresPermiteHijos() {
        return listaPadresPermiteHijos;
    }

    public void setListaPadresPermiteHijos(List<Parametro> listaPadresPermiteHijos) {
        this.listaPadresPermiteHijos = listaPadresPermiteHijos;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

}
