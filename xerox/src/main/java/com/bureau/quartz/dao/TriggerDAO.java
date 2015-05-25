package com.bureau.quartz.dao;

import java.util.List;

import com.bureau.quartz.domain.Trigger;

/**
 * Interfaz para acceder a los datos de los trabajos configurados
 * @author jquedena
 *
 */
public interface TriggerDAO {

	/**
	 * Lista los trabajos configurados
	 * @return La lista de trabajos configurados
	 */
    List<Trigger> listar();
}
