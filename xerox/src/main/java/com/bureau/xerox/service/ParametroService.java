package com.bureau.xerox.service;

import java.io.Serializable;
import java.util.List;

import com.bureau.xerox.domain.Parametro;

/**
 * Capa de servicio usa la interfaz ParametroDAO
 * @author jquedena
 *
 */
public interface ParametroService extends Serializable {

	/**
	 * Ejecuta el m\u00E9todo 
	 * @param idParametro
	 * @return Un p\u00E1rametros, nulo si no existe
	 */
    Parametro obtener(long idParametro);

    /**
     * Ejecuta el m\u00E9todo listarHijos
     * @param idParametroPadre
     * @return Lista de p\u00E1rametros
     */
    List<Parametro> listarHijos(long idParametroPadre);

    /**
     * Ejecuta el m\u00E9todo listarHijosOrdPorEntero
     * @param idParametroPadre
     * @return Lista de p\u00E1rametros
     */
    List<Parametro> listarHijosOrdPorEntero(long idParametroPadre);

    /**
     * Ejecuta el m\u00E9todo listarPadres
     * @return Lista de p\u00E1rametros
     */
    List<Parametro> listarPadres();

    /**
     * Ejecuta el m\u00E9todo listarPadresPermitenHijos
     * @return Lista de p\u00E1rametros
     */
    List<Parametro> listarPadresPermitenHijos();

    /**
     * Ejecuta el m\u00E9todo listarPadresPermitenHijos
     * @param idTipo
     * @param idPadre
     * @param nombre
     * @param idEstado
     * @return Lista de p\u00E1rametros
     */
    List<Parametro> listar(long idTipo, long idPadre, String nombre, String idEstado);

    /**
     * Ejecuta el m\u00E9todo codigoExiste
     * @param codigo
     * @param idTipo
     * @param idParametro
     * @param idPadre
     * @return verdadero si existe
     */
    boolean codigoExiste(String codigo, long idTipo, long idParametro, long idPadre);

    /**
     * Ejecuta el m\u00E9todo nombreExiste
     * @param nombre
     * @param idTipo
     * @param idParametro
     * @param idPadre
     * @return verdadero si existe
     */
    boolean nombreExiste(String nombre, long idTipo, long idParametro, long idPadre);

    /**
     * Ejecuta el m\u00E9todo insertar
     * @param objParametro
     */
    void insertar(Parametro objParametro);

    /**
     * Ejecuta el m\u00E9todo modificar
     * @param objParametro
     */
    void modificar(Parametro objParametro);

    /**
     * Ejecuta el m\u00E9todo listarParametrosPorTipo
     * @param idParametria
     * @param idTipo
     * @return Lista de p\u00E1rametros
     */
    List<Parametro> listarParametrosPorTipo(Long idParametria, Long idTipo);

    /**
     * Ejecuta el m\u00E9todo obtenerParametroPorCodigo
     * @param idPadre
     * @param codigo
     * @return Un p\u00E1rametros, nulo si no existe
     */
    Parametro obtenerParametroPorCodigo(Long idPadre, String codigo);
}