package com.bureau.xerox.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.everis.core.enums.Estado;
import com.everis.enums.FormatoFecha;
import com.everis.util.FechaUtil;

public class Parametro extends AbstractEntity {

    private static final long serialVersionUID = 1L;
    private Parametro parametro;
    private long tipo;
    private String nombre;
    private String descripcion;
    private String codigo;
    private String codigoEti;
    private char codigoHabil;
    private Long entero;
    private String enteroEti;
    private char enteroHabil;
    private BigDecimal decimales;
    private String decimalesEti;
    private char decimalesHabil;
    private String texto;
    private String textoEti;
    private char textoHabil;
    private String texto2;
    private String textoEti2;
    private char textoHabil2;
    private String texto3;
    private String textoEti3;
    private char textoHabil3;
    private Date fecha;
    private String fechaEti;
    private char fechaHabil;
    private String hora;
    private String horaEti;
    private char horaHabil;
    private Character booleano;
    private String booleanoEti;
    private char booleanoHabil;
    private String funcion;
    private String funcionEti;
    private char funcionHabil;
    private String funcionMsg;
    private Character permiteHijo;
    private List<Parametro> parametros = new ArrayList<Parametro>();
    private Parametro parametroTipo;

    public Parametro() {
    }

    public Parametro getParametro() {
        return this.parametro;
    }

    public void setParametro(Parametro parametro) {
        this.parametro = parametro;
    }

    public long getTipo() {
        return this.tipo;
    }

    public void setTipo(long tipo) {
        this.tipo = tipo;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCodigo() {
        return this.codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigoEti() {
        return this.codigoEti;
    }

    public void setCodigoEti(String codigoEti) {
        this.codigoEti = codigoEti;
    }

    public char getCodigoHabil() {
        return this.codigoHabil;
    }

    public void setCodigoHabil(char codigoHabil) {
        this.codigoHabil = codigoHabil;
    }

    public Long getEntero() {
        return this.entero;
    }

    public void setEntero(Long entero) {
        this.entero = entero;
    }

    public String getEnteroEti() {
        return this.enteroEti;
    }

    public void setEnteroEti(String enteroEti) {
        this.enteroEti = enteroEti;
    }

    public char getEnteroHabil() {
        return this.enteroHabil;
    }

    public void setEnteroHabil(char enteroHabil) {
        this.enteroHabil = enteroHabil;
    }

    public BigDecimal getDecimales() {
        return this.decimales;
    }

    public void setDecimales(BigDecimal decimales) {
        this.decimales = decimales;
    }

    public String getDecimalesEti() {
        return this.decimalesEti;
    }

    public void setDecimalesEti(String decimalesEti) {
        this.decimalesEti = decimalesEti;
    }

    public char getDecimalesHabil() {
        return this.decimalesHabil;
    }

    public void setDecimalesHabil(char decimalesHabil) {
        this.decimalesHabil = decimalesHabil;
    }

    public String getTexto() {
        return this.texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getTextoEti() {
        return this.textoEti;
    }

    public void setTextoEti(String textoEti) {
        this.textoEti = textoEti;
    }

    public char getTextoHabil() {
        return this.textoHabil;
    }

    public void setTextoHabil(char textoHabil) {
        this.textoHabil = textoHabil;
    }

    public String getTexto2() {
        return texto2;
    }

    public void setTexto2(String texto2) {
        this.texto2 = texto2;
    }

    public String getTextoEti2() {
        return textoEti2;
    }

    public void setTextoEti2(String textoEti2) {
        this.textoEti2 = textoEti2;
    }

    public char getTextoHabil2() {
        return textoHabil2;
    }

    public void setTextoHabil2(char textoHabil2) {
        this.textoHabil2 = textoHabil2;
    }

    public String getTexto3() {
        return texto3;
    }

    public void setTexto3(String texto3) {
        this.texto3 = texto3;
    }

    public String getTextoEti3() {
        return textoEti3;
    }

    public void setTextoEti3(String textoEti3) {
        this.textoEti3 = textoEti3;
    }

    public char getTextoHabil3() {
        return textoHabil3;
    }

    public void setTextoHabil3(char textoHabil3) {
        this.textoHabil3 = textoHabil3;
    }

    public Date getFecha() {
        return this.fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getFechaEti() {
        return this.fechaEti;
    }

    public void setFechaEti(String fechaEti) {
        this.fechaEti = fechaEti;
    }

    public char getFechaHabil() {
        return this.fechaHabil;
    }

    public void setFechaHabil(char fechaHabil) {
        this.fechaHabil = fechaHabil;
    }

    public String getHora() {
        return this.hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getHoraEti() {
        return this.horaEti;
    }

    public void setHoraEti(String horaEti) {
        this.horaEti = horaEti;
    }

    public char getHoraHabil() {
        return this.horaHabil;
    }

    public void setHoraHabil(char horaHabil) {
        this.horaHabil = horaHabil;
    }

    public Character getBooleano() {
        return this.booleano;
    }

    public void setBooleano(Character booleano) {
        this.booleano = booleano;
    }

    public String getBooleanoEti() {
        return this.booleanoEti;
    }

    public void setBooleanoEti(String booleanoEti) {
        this.booleanoEti = booleanoEti;
    }

    public char getBooleanoHabil() {
        return this.booleanoHabil;
    }

    public void setBooleanoHabil(char booleanoHabil) {
        this.booleanoHabil = booleanoHabil;
    }

    public String getFuncion() {
        return this.funcion;
    }

    public void setFuncion(String funcion) {
        this.funcion = funcion;
    }

    public String getFuncionEti() {
        return this.funcionEti;
    }

    public void setFuncionEti(String funcionEti) {
        this.funcionEti = funcionEti;
    }

    public char getFuncionHabil() {
        return this.funcionHabil;
    }

    public void setFuncionHabil(char funcionHabil) {
        this.funcionHabil = funcionHabil;
    }

    public String getFuncionMsg() {
        return this.funcionMsg;
    }

    public void setFuncionMsg(String funcionMsg) {
        this.funcionMsg = funcionMsg;
    }

    public Character getPermiteHijo() {
        return this.permiteHijo;
    }

    public void setPermiteHijo(Character permiteHijo) {
        this.permiteHijo = permiteHijo;
    }

    public List<Parametro> getParametros() {
        return this.parametros;
    }

    public void setParametros(List<Parametro> parametros) {
        this.parametros = parametros;
    }

    public Parametro getParametroTipo() {
        return parametroTipo;
    }

    public void setParametroTipo(Parametro parametroTipo) {
        this.parametroTipo = parametroTipo;
    }

    public String getEstadoDescripcion() {
        String estado = "";
        if (Estado.ACTIVO.getEstado().compareTo(this.getEstado()) == 0) {
            estado = Estado.ACTIVO.getDescripcion();
        } else {
            estado = Estado.INACTIVO.getDescripcion();
        }
        return estado;
    }

    public long getIdParametria() {
        long id = 0;
        if (this.getParametro() != null) {
            id = this.getParametro().getId();
        }
        return id;
    }

    public String getNombrePadre() {
        String nombre = "";
        if (this.getParametro() != null) {
            nombre = this.getParametro().getNombre();
        }
        return nombre;
    }

    public String getNombreTipo() {
        String tipo = "";
        if (this.getParametroTipo() != null) {
            tipo = this.getParametroTipo().getNombre();
        }
        return tipo;
    }

    public String getFechaString() {
        String fecha = "";
        if (this.getFecha() != null) {
            fecha = FechaUtil.formatFecha(this.fecha, FormatoFecha.DDMMYYYY_WITH_SEPARATOR);
        }
        return fecha;
    }
}
