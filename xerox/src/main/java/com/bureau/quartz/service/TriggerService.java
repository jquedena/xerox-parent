package com.bureau.quartz.service;

import java.util.List;

import com.bureau.quartz.domain.Trigger;

/**
 * Servicio para listar las trabajos configurados v\u00EDa Quartz
 * @author jquedena
 *
 */
public interface TriggerService {

	/**
	 * Lista los trabajos configurados
	 * @return Lista de trabajos configurados 
	 */
    List<Trigger> listar();
}
