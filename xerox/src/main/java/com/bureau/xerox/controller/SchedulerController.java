package com.bureau.xerox.controller;

import javax.annotation.Resource;

import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bureau.batch.service.JobInstanceService;
import com.bureau.quartz.service.TriggerService;
import com.bureau.xerox.model.SchedulerModel;
import com.bureau.xerox.service.SchedulerService;
import com.everis.enums.Resultado;
import com.everis.web.constant.Produces;
import com.everis.web.controller.impl.AbstractSpringControllerImpl;

/**
 * Controlador para el acceso a la tabla de estado de trabajos programados
 * @author jquedena
 *
 */
@Controller("schedulerController")
@Scope("prototype")
@RequestMapping(value = "scheduler")
public class SchedulerController extends AbstractSpringControllerImpl {

    private static final long serialVersionUID = 1L;

    @Resource(name = "schedulerService")
    private SchedulerService schedulerService;

    @Resource(name = "triggerService")
    private TriggerService triggerService;

    @Resource(name = "jobInstanceService")
    private JobInstanceService jobInstanceService;

    @Resource(name = "jobExplorer")
    private JobExplorer jobExplorer;
    
    /**
     * P\u00E1gina de inicio, redirecciona: scheduler/index.jsp
     * @param model
     * @return
     */
    @RequestMapping(value = "index")
    public String index(ModelMap model) {
        model.addAttribute("procesoClass", "ui-state-active-bbva");
        model.addAttribute("administracionClass", "");
        model.addAttribute("consultaClass", "");
        return "scheduler/index";
    }

    /**
     * Lista los trabajos programados
     * Devuelve el objeto SchedulerModel(con los siguientes valores tipoResultado,
     * mensaje, triggerInstances) serializado en formato JSON
     * 
     * @return
     */
    @RequestMapping(value = "listar", method = RequestMethod.POST, produces = Produces.JSON)
    public @ResponseBody String listar() {
        String result;

        try {
            SchedulerModel schedulerModel = new SchedulerModel();
            schedulerModel.setTipoResultado(Resultado.EXITO);
            schedulerModel.setTriggerInstances(triggerService.listar());
            result = this.renderModelJson(schedulerModel);
        } catch (Exception e) {
            result = this.renderErrorSistema(e);
        }

        return result;
    }

    /**
     * Muestra el detalle de la \u00FAltima ejecuci\u00F3n del trabajo
     * Devuelve el objeto SchedulerModel(con los siguientes valores tipoResultado,
     * mensaje, jobInstance, runningJobInstances) serializado en formato JSON
     * 
     * @param jobName
     * @return
     */
    @RequestMapping(value = "detail/{jobName}", method = RequestMethod.POST, produces = Produces.JSON)
    public @ResponseBody String detail(@PathVariable("jobName") String jobName) {
        String result; 
        
        try {
            SchedulerModel schedulerModel = new SchedulerModel();
            Long id = jobInstanceService.obtenerUltimaInstancia(jobName);
            JobInstance jobInstance = jobExplorer.getJobInstance(id);

            schedulerModel.setJobInstance(jobInstance);
            schedulerModel.setTipoResultado(Resultado.EXITO);
            if(id != null) {
                schedulerModel.setRunningJobInstances(jobExplorer.getJobExecutions(jobInstance));
            }

            result = this.renderModelJson(schedulerModel);
        } catch(Exception e) {
            result = this.renderErrorSistema(e);
        }
        
        return result;
    }

    
}