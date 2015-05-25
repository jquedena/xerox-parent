package com.bureau.xerox.dao;

import java.util.List;

import com.bureau.xerox.domain.Parametro;
import com.bureau.xerox.enums.Respuesta;
import com.everis.core.enums.Estado;

/**
 * Capa de persistencia permite el acceso y la  manipulaci\u00F3n de la tabla PARAMETRO
 * @author jquedena
 *
 */
public interface ParametroDAO {

	/**
	 * Lista los p\u00E1rametros a partir del PARAMETRO_ID y ESTADO
	 * @param idParametroPadre
	 * @param estado
	 * @return Lista de p\u00E1rametros
	 */
    List<Parametro> listarHijos(long idParametroPadre, Estado estado);

    /**
	 * Lista los p\u00E1rametros a partir del PARAMETRO_ID y ESTADO, ordernados por el campo entero
	 * @param idParametroPadre
	 * @param estado
	 * @return Lista de p\u00E1rametros
	 */
    List<Parametro> listarHijosOrdPorEntero(long idParametroPadre, Estado estado);

    /**
	 * Lista los p\u00E1rametros activos a partir del PARAMETRO_ID
	 * @param idParametroPadre
	 * @return Lista de p\u00E1rametros
	 */
    List<Parametro> listarHijos(long idParametroPadre);

    /**
	 * Lista los p\u00E1rametros activos a partir del PARAMETRO_ID, ordernados por el campo entero
	 * @param idParametroPadre
	 * @return Lista de p\u00E1rametros
	 */
    List<Parametro> listarHijosOrdPorEntero(long idParametroPadre);

    /**
     * Lista de p\u00E1rametros padres a partir del ESTADO y PERMITE_HIJOS
     * @param estado
     * @param permiteHijos
     * @return Lista de p\u00E1rametros
     */
    List<Parametro> listarPadres(Estado estado, Respuesta permiteHijos);

    /**
     * Lista de p\u00E1rametros padres activos
     * @return Lista de p\u00E1rametros
     */
    List<Parametro> listarPadres();

    /**
     * Lista de p\u00E1rametros padres activos que permiten hijos
     * @return Lista de p\u00E1rametros
     */
    List<Parametro> listarPadresPermitenHijos();

    /**
     * Obtiene el p\u00E1rametro a trav\u00E9s de su ID
     * @return Un p\u00E1rametros, nulo si no existe
     */
    Parametro obtener(long idParametro);

    /**
     *  Lista los p\u00E1rametros a partir del TIPO, PARAMETRO_ID, NOMBRE y ESTADO
     * @param idTipo
     * @param idPadre
     * @param nombre
     * @param idEstado
     * @return Lista de p\u00E1rametros
     */
    List<Parametro> listar(long idTipo, long idPadre, String nombre, String idEstado);

    /**
     * Verifica que el c\u00F3digo no este registrado
     * @param codigo
     * @param idTipo
     * @param idParametro
     * @param idPadre
     * @return verdadero si existe
     */
    boolean codigoExiste(String codigo, long idTipo, long idParametro, long idPadre);

    /**
     * Verifica que el nombre no este registrado
     * @param nombre
     * @param idTipo
     * @param idParametro
     * @param idPadre
     * @return verdadero si existe
     */
    boolean nombreExiste(String nombre, long idTipo, long idParametro, long idPadre);

    /**
     * Inserta un p\u00E1rametro
     * @param objParametria
     */
    void insertar(Parametro objParametria);

    /**
     * Modifica un p\u00E1rametro
     * @param objParametria
     */
    void modificar(Parametro objParametria);

    /**
	 * Lista los p\u00E1rametros a partir del PARAMETRO_ID y TIPO
	 * @param idParametria
	 * @param idTipo
	 * @return Lista de p\u00E1rametros
     */
    List<Parametro> listarParametrosPorTipo(Long idParametria, Long idTipo);

    /**
     * Obtiene el p\u00E1rametro a partir de PARAMETRO_ID y CODIGO
     * @return Un p\u00E1rametros, nulo si no existe
     */
    Parametro obtenerParametroPorCodigo(Long idPadre, String codigo);
}