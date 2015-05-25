package com.bureau.xerox.service;

import org.quartz.SchedulerException;

import com.bureau.batch.domain.JobConfig;

/**
 * Capa de servicio para la gesti\u00F3n de las tareas programadas
 * @author jquedena
 *
 */
public interface SchedulerService {

	/**
	 * Elimina todas las tareas programadas
	 */
    void deleteAll();
    
    /**
     * Elimina una tarea
     * @param jobConfig
     * @throws SchedulerException
     */
    void delete(JobConfig jobConfig) throws SchedulerException;
    
    /**
     * Replanifica la ejecuci\u00F3n de una tarea
     * @param jobConfig
     * @throws SchedulerException
     */
    void rescheduler(JobConfig jobConfig) throws SchedulerException;
}
