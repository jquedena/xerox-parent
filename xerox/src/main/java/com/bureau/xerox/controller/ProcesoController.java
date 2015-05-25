package com.bureau.xerox.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.bureau.xerox.domain.Parametro;
import com.bureau.xerox.enums.Configuracion;
import com.bureau.xerox.model.ProcesoModel;
import com.bureau.xerox.service.JobInstanceExecutionService;
import com.bureau.xerox.service.LogService;
import com.bureau.xerox.service.ParametroService;
import com.everis.enums.Resultado;
import com.everis.util.ArchivoUtil;
import com.everis.web.constant.Produces;
import com.everis.web.constant.Session;
import com.everis.web.controller.impl.AbstractSpringControllerImpl;

/**
 * Controlador para el acceso a la tabla de procesos
 * 
 * @author jquedena
 *
 */
@Controller("procesoController")
@Scope("prototype")
@RequestMapping(value = "process")
@SessionAttributes({ Session.CURRENT_USER })
public class ProcesoController extends AbstractSpringControllerImpl {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(ProcesoController.class);

    @Resource(name = "logService")
    private LogService logService;

    @Resource(name = "parametroService")
    private ParametroService parametroService;

    @Resource(name = "jobInstanceExecutionService")
    private JobInstanceExecutionService jobInstanceExecutionService;

    @Resource(name = "jobExplorer")
    private JobExplorer jobExplorer;

    /**
     * P\u00E1gina de inicio, redirecciona: process/process.jsp
     * 
     * @param model
     * @return
     */
    @RequestMapping(value = "index")
    public String index(ModelMap model) {
        model.addAttribute("procesoClass", "ui-state-active-bbva");
        model.addAttribute("administracionClass", "");
        model.addAttribute("consultaClass", "");
        return "process/process";
    }

    /**
     * Lista los procesos ejecutados
     * Devuelve el objeto ProcesoModel(con los siguientes valores tipoResultado,
     * mensaje, procesos) serializado en formato JSON
     * 
     * @param model
     * @param result
     * @return
     */
    @RequestMapping(value = "list", produces = Produces.JSON)
    public @ResponseBody String list(@ModelAttribute("procesoModel") ProcesoModel model, BindingResult result) {
        String json = "";
        try {
            if (!result.hasErrors()) {
                ProcesoModel procesoModel = new ProcesoModel();
                procesoModel.setTipoResultado(Resultado.EXITO);
                procesoModel.setProcesos(jobInstanceExecutionService.listar(model.getProceso()));

                json = this.renderModelJson(procesoModel);
            } else {
                json = this.renderErrorSistema(result.getAllErrors());
            }
        } catch (Exception e) {
            json = this.renderErrorSistema(e);
        }

        return json;
    }

    /**
     * Permite la descarga del Log (Text)
     * 
     * @param fileName
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "download/text/{fileName}")
    public void downloadLogText(@PathVariable("fileName") String fileName, HttpServletResponse response) throws IOException {
        byte[] bytes = new byte[0];
        String fileNameTemp = fileName + ".log";
        String fileNamePart[] = fileNameTemp.split("_");
        Long idProceso = Long.parseLong(fileNamePart[1]);

        try {
            bytes = logService.generarLogFile(idProceso).toByteArray();
        } catch (Exception e) {
            logger.error(fileNameTemp, e);
        }

        renderBytes(response, bytes, Produces.BIN, fileNameTemp);
    }

    /**
     * Permite la descarga del Log (html)
     * Escribe directamente en el objeto HttpServletResponse
     * 
     * @param fileName
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "download/html/{fileName}")
    public void downloadLog(@PathVariable("fileName") String fileName, HttpServletResponse response) throws IOException {
        Parametro param = parametroService.obtener(Configuracion.PB_DIRECTORIO_ERRORES.getKey());
        String archivo = ArchivoUtil.completeFileSeparator(param.getTexto()) + fileName + ".html";

        renderInputStream(response, archivo, fileName, Produces.HTML);
    }

    @RequestMapping(value = "download/xmlInput/{fileName}")
    public @ResponseBody byte[] downloadInput(@PathVariable("fileName") String jobName) {
        byte[] bytes = new byte[0];
        return bytes;
    }

    /**
     * Permite la descarga de los archivos xml generados
     * Escribe directamente en el objeto HttpServletResponse
     * 
     * @param fileName
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "download/xmlOutput/{fileName}")
    public void downloadOutput(@PathVariable("fileName") String fileName, HttpServletResponse response) throws IOException {
        Parametro param = parametroService.obtener(Configuracion.PB_DIRECTORIO_XML.getKey());
        String archivo = ArchivoUtil.completeFileSeparator(param.getTexto()) + fileName + ".xml";

        renderInputStream(response, archivo, fileName, Produces.XML);
    }

    /**
     * Permite la descarga de los archivos zip generados
     * Escribe directamente en el objeto HttpServletResponse
     * 
     * @param fileName
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "download/zipOutput/{fileName}")
    public void downloadzipOutput(@PathVariable("fileName") String fileName, HttpServletResponse response) throws IOException {
        String parts[] = fileName.split("_");
        JobExecution jobExecution = jobExplorer.getJobExecution(Long.parseLong(parts[1]));
        String fileZip = jobExecution.getJobParameters().getString("archivoFinal");
        parts = fileZip.split("\\/");
        renderInputStream(response, fileZip, parts[parts.length - 1].replaceAll(".zip", ""), Produces.ZIP);
    }
}
