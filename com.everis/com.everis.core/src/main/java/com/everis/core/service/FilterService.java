package com.everis.core.service;

import com.everis.core.security.CurrentUser;

public interface FilterService {

    CurrentUser loadUser(String codigoUsuario);
}
