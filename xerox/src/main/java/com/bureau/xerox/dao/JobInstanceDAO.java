package com.bureau.xerox.dao;

/**
 * Capa de persistencia permite accesar a los datos de la ultima ejecuci\u00F3n de la tarea
 * @author jquedena
 *
 */
public interface JobInstanceDAO {

	/**
	 * Obtiene el identificador de la \u00FAltima ejecuci\u00F3n de la tarea
	 * @param name, nombre de la tarea
	 * @return Identificador de la \u00FAltima ejecuci\u00F3n de la tarea
	 */
    Long obtenerUltimaInstancia(String name);
}
