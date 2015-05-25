package com.everis.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.everis.core.security.CurrentUser;
import com.everis.core.service.FilterService;
import com.everis.web.constant.Session;
import com.everis.web.listener.WebServletContextListener;
import com.grupobbva.bc.per.tele.ldap.directorio.IILDPeUsuario;
import com.grupobbva.bc.per.tele.seguridad.ServiciosSeguridadBbva;

public class SessionFilter implements Filter {

    private static final Logger LOG = Logger.getLogger(SessionFilter.class);
    private static String NOT_AUTHORIZED;
    private static String SIGN_IN;
    private static String SIGN_OUT;
    private FilterService filterService = null;
    
    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
        SIGN_IN = config.getInitParameter("SIGN_IN");
        SIGN_OUT = config.getInitParameter("SIGN_OUT");
        NOT_AUTHORIZED = config.getInitParameter("NOT_AUTHORIZED");
        
        try {
        	filterService = WebServletContextListener.getBean(config.getInitParameter("FILTER_SERVICE"));
        } catch(Exception e) {
        	LOG.error("FilterService no implementado", e);
        }
    }

    public String loadUser(HttpServletRequest request) {
        CurrentUser currentUser = new CurrentUser();
        String result = request.getRequestURI();
        ServiciosSeguridadBbva ssBbva = null;
        IILDPeUsuario user = null;
        String codigoUsuario = "";

        try {
            ssBbva = new ServiciosSeguridadBbva(request);
            if (ssBbva != null) {
                ssBbva.obtener_ID();
            }

            if (ssBbva.getUsuario() != null) {           	
                codigoUsuario = ssBbva.getUsuario().toUpperCase();
                
                if(filterService != null) {
                	currentUser = filterService.loadUser(codigoUsuario);
                } else {
	                try {
	                    user = IILDPeUsuario.recuperarUsuario(codigoUsuario);
	                } catch (Exception ex) {
	                    LOG.debug("Error al recuperar el ID del usuario", ex);
	                }
	
	                if (user == null) {
	                    currentUser.setNombreCompleto("Anonimo");
	                } else {
	                    currentUser.setCodigoRegistro(codigoUsuario);
	                    currentUser.setNombreCompleto(user.getNombreCompleto());
	                    currentUser.setCodigoCargo(user.getCargo().getCodigo());
	                    currentUser.setIdRol(-1L);
	                }
                }
            } else {
            	currentUser.setCodigoRegistro("-------");
                currentUser.setNombreCompleto("Anonimo");
                currentUser.setCodigoCargo("---");
            }

            request.getSession().setAttribute(Session.CURRENT_USER, currentUser);
        } catch (Exception e) {
            LOG.error("Error al inciar la sesion", e);
            result = NOT_AUTHORIZED;
        }

        return result;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        String url = request.getRequestURI();
        String tmp[] = request.getRequestURL().toString().split(request.getContextPath());
        String path = tmp[0] + request.getContextPath() + "/";

        if (url.indexOf(NOT_AUTHORIZED) != -1 && (url.substring(url.length() - NOT_AUTHORIZED.length(), url.length()).equalsIgnoreCase(NOT_AUTHORIZED) || url.indexOf(";jsessionid=") > -1)) {
            chain.doFilter(req, resp);
        } else if (SIGN_IN != null && url.indexOf(SIGN_IN) != -1 && (url.substring(url.length() - SIGN_IN.length(), url.length()).equalsIgnoreCase(SIGN_IN) || url.indexOf(";jsessionid=") > -1)) {
            chain.doFilter(req, resp);
        } else if (SIGN_OUT != null && url.indexOf(SIGN_OUT) != -1 && url.substring(url.length() - SIGN_OUT.length(), url.length()).equalsIgnoreCase(SIGN_OUT)) {
            request.getSession().invalidate();
            LOG.error("Forward [" + path + SIGN_OUT + "]");
            request.getRequestDispatcher(SIGN_OUT).forward(req, resp);
        } else {
            if (request.getSession().getAttribute(Session.CURRENT_USER) != null) {
                chain.doFilter(req, resp);
            } else {
                if (NOT_AUTHORIZED.equalsIgnoreCase(loadUser(request))) {
                    LOG.error("Forward [" + path + NOT_AUTHORIZED + "]");
                    request.getRequestDispatcher(NOT_AUTHORIZED).forward(req, resp);
                } else {
                    chain.doFilter(req, resp);
                }
            }
        }
    }
}