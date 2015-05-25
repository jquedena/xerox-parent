package com.bureau.batch.monitoring;

import org.springframework.batch.core.JobExecution;

/**
 * Permite realizar la notificaci\u00F3n de los mensajes, dentro del contexto de
 * ejecuci\u00F3n de Spring Batch
 * 
 * @author jquedena
 *
 */
public interface JobMonitoringNotifier {

    /**
     * Realiza la notificaci\u00F3n de la ejecuci\u00F3n de la tarea
     * 
     * @param jobExecution, objeto con los datos de la ejecuci\u00F3n de la tarea en proceso.
     */
    void notify(JobExecution jobExecution);
}
