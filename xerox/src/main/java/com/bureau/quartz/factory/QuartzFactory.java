package com.bureau.quartz.factory;

import com.bureau.batch.domain.JobConfig;

/**
 * F\u00E1brica de trabajos
 * @author jquedena
 *
 */
public interface QuartzFactory {

	/**
	 * Grupo por defecto para crear los trabajos
	 */
	public final String GROUP_NAME = "FATCA";
	
	/**
	 * Crea las instancias Quartz para la ejecuci\u00F3n del trabajo seg\u00FAn la programaci\u00F3n
	 * @param jobBatch, trabajo a crear
	 */
    void createJob(JobConfig jobBatch);

}