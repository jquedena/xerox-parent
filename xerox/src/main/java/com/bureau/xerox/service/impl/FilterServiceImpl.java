package com.bureau.xerox.service.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.everis.core.security.CurrentUser;
import com.everis.core.service.FilterService;

@Service("filterService")
public class FilterServiceImpl implements FilterService {

	private static final Logger LOGGER = Logger.getLogger(FilterServiceImpl.class);
	
	@Override
	public CurrentUser loadUser(String codigoUsuario) {
		CurrentUser currentUser = new CurrentUser();		
		currentUser.setCodigoRegistro("-------");
		currentUser.setNombreCompleto("Administrador");
		currentUser.setCodigoCargo("---");
		LOGGER.info(currentUser.getNombreCompleto());		
		
		return currentUser;
	}

}
