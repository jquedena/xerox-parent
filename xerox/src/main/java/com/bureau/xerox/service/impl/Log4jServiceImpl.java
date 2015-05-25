package com.bureau.xerox.service.impl;

import java.io.PrintWriter;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.bureau.xerox.enums.Configuracion;
import com.bureau.xerox.service.ParametroService;
import com.everis.core.service.Log4jService;

@Service("log4jService")
public class Log4jServiceImpl implements Log4jService {

    private static final Logger LOG = Logger.getLogger(Log4jServiceImpl.class);

    @Resource(name = "parametroService")
    private ParametroService parametroService;

    @Override
    public String obtener(String key) {
        String value = "";

        try {
            if (Log4jService.ROOT_CATEGORY.equalsIgnoreCase(key)) {
                value = parametroService.obtener(Configuracion.PB_ROOT_CATEGORY.getKey()).getTexto();
            } else if (Log4jService.FILE.equalsIgnoreCase(key)) {
                value = parametroService.obtener(Configuracion.PB_FILE.getKey()).getTexto();
            }
        } catch (Exception e) {
            LOG.error("Error al obtener el valor de: [" + key + "]");
            value = "";
        }

        return value;
    }

    @Override
    public void test(PrintWriter out) {
        out.write("No implementado...");
    }

	@Override
	public void afterSetProperties(Properties properties) {
	}

}
