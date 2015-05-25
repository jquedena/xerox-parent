package com.bureau.xerox.dao;

import java.util.List;

import com.bureau.xerox.domain.Log;
import com.everis.core.dao.IHibernateDAO;

/**
 * Capa de persistencia permite el acceso y la  manipulaci\u00F3n de la tabla LOG
 * @author jquedena
 *
 */
public interface LogDAO extends IHibernateDAO<Log> {
	
	/**
	 * Lista las trazas correspondientes la proceso solicitado
	 * @param idProceso, c\u00F3digo del proceso ejecutado
	 * @return Lista de LOG
	 * @throws Exception
	 */
	List<Log> listarLogPorProceso(Long idProceso) throws Exception;
}
