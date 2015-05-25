package com.bureau.batch.service;

/**
 * Servicio para el acceso y control de ejecuci\u00F3n de los trabajos
 * @author jquedena
 *
 */
public interface JobInstanceService {

	/**
	 * Obtiene el identificador de la \u00FAltima ejecuci\u00F3n del trabajo
	 * @param name, nombre del trabajo
	 * @return El identificador de la \u00FAltima instancia ejecutada
	 */
    Long obtenerUltimaInstancia(String name);
    
    /**
     * Ejecuta un trabajo
     * @param jobName, nombre del trabajo
     * @param jobProcessorParameter, nombre del objeto que implementa la interfaz <b>JobProcessorParameter</b>
     * @param codigoRegistro, c\u00F3digo de registro del usuario que realiza la invocaci\u00F3n o programaci\u00F3n
     */
    void execute(String jobName, String jobProcessorParameter, String codigoRegistro);
}
