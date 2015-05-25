package com.bureau.xerox.service;

import org.springframework.batch.core.JobExecution;

/**
 * Capa de servicio para la gesti\u00F3n de las notificaciones
 * @author jquedena
 *
 */
public interface NotificacionService {

	/**
	 * Notifica los resultados obtenidos de la ejecuci\u00F3n de una tarea
	 * @param jobExecution, instancia ejecutada
	 * @return verdadero si se envio la notificaci\u00F3n
	 */
    boolean notificar(JobExecution jobExecution);
}
