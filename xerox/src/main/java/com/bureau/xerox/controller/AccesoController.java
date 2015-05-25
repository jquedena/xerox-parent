package com.bureau.xerox.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.everis.web.controller.impl.AbstractSpringControllerImpl;

/**
 * Controlador que permite redirigir a las p\u00E1ginas de bienvenida, acceso no
 * autorizado o cierre de sesi\u00F3n
 * 
 * @author jquedena
 *
 */
@Controller("accesoController")
@Scope("prototype")
@RequestMapping(value = { "", "scheduler", "upload", "param", "cliente", "process", "period" })
public class AccesoController extends AbstractSpringControllerImpl {

    private static final long serialVersionUID = 1L;

    /**
     * P\u00E1gina de bienvenida
     * 
     * @return Redirige a la p\u00E1gina de bienvenida
     */
    @RequestMapping(value = "welcome")
    public String welcome() {
        return "common/welcome";
    }

    /**
     * P\u00E1gina de ingreso al sistema - No se Usa
     * 
     * @return Redirige a la p\u00E1gina de ingreso al sistema
     */
    @RequestMapping(value = "signIn")
    public String signIn() {
        return "common/sign-in";
    }

    /**
     * P\u00E1gina de cierre de sesi\u00F3n
     * 
     * @return Redirige a la p\u00E1gina de cierre de sesi\u00F3n
     */
    @RequestMapping(value = "signOut")
    public String signOut() {
        return "common/sign-out";
    }

    /**
     * P\u00E1gina de acceso no autorizado
     * 
     * @return Redirige a la p\u00E1gina de acceso no autorizado
     */
    @RequestMapping(value = "notAuthorized")
    public String notAuthorized() {
        return "common/not-authorized";
    }
}
