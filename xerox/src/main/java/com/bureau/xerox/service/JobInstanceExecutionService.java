package com.bureau.xerox.service;

import java.util.List;

import com.bureau.batch.domain.JobInstanceExecution;
import com.everis.core.service.IDataManipulationService;

/**
 * Capa de servicio usa la interfaz JobInstanceExecutionDAO
 * @author jquedena
 *
 */
public interface JobInstanceExecutionService extends IDataManipulationService<JobInstanceExecution> {

	/**
	 * Ejecuta el m\u00E9todo listar
	 * @param jobInstanceExecution
	 * @return Lista las ejecuciones de las tareas
	 */
	List<JobInstanceExecution> listar(JobInstanceExecution jobInstanceExecution);
}
