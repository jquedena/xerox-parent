package com.bureau.xerox.service;

import java.io.ByteArrayOutputStream;

import com.bureau.xerox.domain.Log;
import com.everis.core.service.IDataManipulationService;

/**
 * Capa de servicio usa la interfaz LogDAO
 * @author jquedena
 *
 */
public interface LogService  extends IDataManipulationService<Log> {
	
	/**
	 * Ejecuta el m\u00E9todo  listarLogPorProceso,
	 * con la lista obtenida genera un archivo de texto plano
	 * @param idProceso
	 * @return Archivo expresado en bytes
	 * @throws Exception
	 */
	ByteArrayOutputStream generarLogFile(Long idProceso) throws Exception;
}
