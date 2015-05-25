package com.bureau.xerox.batch.processor;

import org.springframework.batch.core.JobParameters;

/**
 * Configura los par\u00E1metros para la tarea a ejecutar
 * 
 * @author jquedena
 *
 */
public interface JobProcessorParameter {

    /**
     * Configura los par\u00E1metros para el proceso.
     * 
     * @param codigoRegistro, c\u00F3digo de registro de usuario que programo la tarea
     * @return JobParameters, conjunto de parametros para la tarea
     */
    JobParameters processParameters(String codigoRegistro);
}
