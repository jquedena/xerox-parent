package com.bureau.xerox.dao;

import java.util.List;

import com.bureau.batch.domain.JobInstanceExecution;
import com.everis.core.dao.IHibernateDAO;

/**
 * Capa de persistencia permite accesar a los datos de la ejecuci\u00F3n de las tareas
 * @author jquedena
 *
 */
public interface JobInstanceExecutionDAO extends IHibernateDAO<JobInstanceExecution> {

	/**
	 * Lista las ejecuciones de las tareas
	 * @param jobInstanceExecution, objeto que contiene los datos de los filtros
	 * @return Lista las ejecuciones de las tareas
	 */
	List<JobInstanceExecution> listar(JobInstanceExecution jobInstanceExecution);
}
