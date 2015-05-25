package com.everis.web.controller.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.validation.ObjectError;

import com.everis.enums.FormatoFecha;
import com.everis.enums.Resultado;
import com.everis.util.FechaUtil;
import com.everis.util.TrazaUtil;
import com.everis.web.constant.Produces;
import com.everis.web.controller.AbstractController;
import com.everis.web.model.BaseModel;

import flexjson.JSONSerializer;

public abstract class AbstractSpringControllerImpl implements AbstractController, Serializable {

    private static final long serialVersionUID = 1L;
    private Logger LOG = Logger.getLogger(AbstractSpringControllerImpl.class);

    public String getMensaje(String key) throws IOException {
        Properties properties = new Properties();
        InputStream is = this.getClass().getResourceAsStream("/messages.properties");
        properties.load(is);
        return properties.getProperty(key);
    }

    public String renderModelJson(Object object) {
        return renderModelJsonDeepExclude(object, new String[] { "*.class" });
    }

    public String renderModelJsonDeepExclude(Object object, String[] exclude) {
        String json = "";
        try {
            json = new JSONSerializer().exclude(exclude).deepSerialize(object);
        } catch (Exception e) {
            LOG.error("GenericController:renderModelJsonDeepExclude", e);
            StringBuilder sb = new StringBuilder();
            sb.append("{\"mensaje\": \"");
            sb.append(TrazaUtil.mostrarMensajeHTML(e));
            sb.append("\", \"tipoResultado\": \"ERROR_SISTEMA\"}");
            json = sb.toString();
        }
        LOG.info(json);
        return json;
    }

    public String renderModelJsonDeepInclude(Object object, String[] include) {
        String json = "";
        try {
            json = new JSONSerializer().include(include).serialize(object);
        } catch (Exception e) {
            LOG.error("GenericController:renderModelJsonDeepExclude", e);
            StringBuilder sb = new StringBuilder();
            sb.append("{\"mensaje\": \"");
            sb.append(TrazaUtil.mostrarMensajeHTML(e));
            sb.append("\", \"tipoResultado\": \"ERROR_SISTEMA\"}");
            json = sb.toString();
        }
        LOG.info(json);
        return json;
    }

    public String renderErrorSistema(Exception ex) {
        LOG.error("renderErrorSistema", ex);
        BaseModel baseModel = new BaseModel();
        baseModel.setMensaje(TrazaUtil.mostrarMensajeHTML(ex));
        baseModel.setTipoResultado(Resultado.ERROR_SISTEMA);
        return this.renderModelJson(baseModel);
    }

    public void renderErrorSistema(Exception ex, HttpServletResponse response) throws IOException {
        this.renderBytes(response, TrazaUtil.mostrarMensajeHTML(ex).getBytes(), Produces.HTML, "error-" + FechaUtil.formatFecha(new Date(), FormatoFecha.DDMMYYYY_HH24MMSS) + ".html");
    }

    public String renderErrorSistema(List<ObjectError> ex) {
        BaseModel baseModel = new BaseModel();

        StringBuilder sb = new StringBuilder();
        for (ObjectError e : ex) {
            LOG.error("renderErrorSistema [ObjectName:" + e.getObjectName() + ", DefaultMessage:" + e.getDefaultMessage() + "]");
            sb.append("ObjectName: ");
            sb.append(e.getObjectName());
            sb.append(" - DefaultMessage: ");
            sb.append(e.getDefaultMessage());
            sb.append("<br/>");
        }

        baseModel.setMensaje(sb.toString());
        baseModel.setTipoResultado(Resultado.ERROR_SISTEMA);
        return this.renderModelJson(baseModel);
    }

    @Override
    public void render(String rendered) throws IOException {
    }

    public void renderBytes(HttpServletResponse response, byte[] bytes, Produces produces, String fileName) throws IOException {
        response.setContentType(produces.getTipo());
        response.setContentLength(bytes.length);
        response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", fileName));

        OutputStream outStream = response.getOutputStream();
        outStream.write(bytes);
        outStream.close();
    }

    public void renderInputStream(HttpServletResponse response, String fileName, String fileNameDownload, Produces produces) throws IOException {
        byte[] bytes = new byte[0];

        File file = new File(fileName);
        if (file.exists()) {
            try {
                FileInputStream fin = new FileInputStream(file);
                bytes = new byte[(int) file.length()];
                fin.read(bytes);
                fin.close();
                this.renderBytes(response, bytes, produces, fileNameDownload + "." + produces.getExtension());
            } catch (FileNotFoundException e) {
                this.renderErrorSistema(e, response);
            } catch (IOException e) {
                this.renderErrorSistema(e, response);
            }
        } else {
            this.renderBytes(response, ("El archivo " + fileName + ", no existe.").getBytes(), Produces.TXT, "error-" + FechaUtil.formatFecha(new Date(), FormatoFecha.DDMMYYYY_HH24MMSS) + ".html");
        }
    }
}
