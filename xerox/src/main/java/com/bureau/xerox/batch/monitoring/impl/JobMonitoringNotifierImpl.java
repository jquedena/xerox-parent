package com.bureau.xerox.batch.monitoring.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.stereotype.Component;

import com.bureau.batch.monitoring.JobMonitoringNotifier;
import com.bureau.xerox.batch.tasklet.TaskletException;
import com.bureau.xerox.domain.Log;
import com.bureau.xerox.domain.Parametro;
import com.bureau.xerox.enums.Configuracion;
import com.bureau.xerox.service.LogService;
import com.bureau.xerox.service.NotificacionService;
import com.bureau.xerox.service.ParametroService;
import com.everis.enums.FormatoFecha;
import com.everis.util.ArchivoUtil;
import com.everis.util.FechaUtil;
import com.everis.util.Log4jUtil;

/**
 * Permite realizar la notificaci\u00F3n de los mensajes v\u00EDa correo
 * electr\u00F3nico y archivo de log, dentro del contexto de ejecuci\u00F3n de
 * Spring Batch
 * 
 * @author jquedena
 *
 */
@Component("jobMonitoringNotifier")
public class JobMonitoringNotifierImpl implements JobMonitoringNotifier {

    private Logger logger;

    @Resource(name = "notificacionService")
    private NotificacionService notificacionService;

    @Resource(name = "logService")
    private LogService logService;

    @Resource(name = "parametroService")
    private ParametroService parametroService;

    /**
     * Realiza la notificaci\u00F3n v\u00EDa correo electr\u00F3nico y archivo
     * de log
     * 
     * @param jobExecution
     *            , trabajo que se ejecuta
     */
    @Override
    public void notify(JobExecution jobExecution) {
        Parametro param = parametroService.obtener(Configuracion.PB_DIRECTORIO_ERRORES.getKey());
        String fileName = ArchivoUtil.completeFileSeparator(param.getTexto()) + jobExecution.getJobInstance().getJobName() + "_" + jobExecution.getJobId() + "_"
                + FechaUtil.formatFecha(jobExecution.getCreateTime(), FormatoFecha.DDMMYYYY_HH24MMSS) + ".html";
        logger = Log4jUtil.createLoggerHTML(this.getClass().getCanonicalName(), fileName, "INFO");
        logger.error(jobExecution.getJobInstance().getJobName() + ": " + jobExecution.getExitStatus().getExitCode());

        notificacionService.notificar(jobExecution);

        if (!jobExecution.getExitStatus().getExitCode().equalsIgnoreCase("COMPLETED")) {
            Log log = new Log();
            log.setIdProceso(jobExecution.getJobId());
            log.setEstado("1");

            if (jobExecution.getFailureExceptions().isEmpty()) {
                for (StepExecution stepExecution : jobExecution.getStepExecutions()) {
                    printThrowableLog(stepExecution.getStepName(), stepExecution.getFailureExceptions());
                }
            } else {
                printThrowableLog(jobExecution.getJobInstance().getJobName(), jobExecution.getFailureExceptions());
            }

            log.setMensaje("Ocurri\u00F3 un error al ejecutar el proceso, por favor revisar el archivo de trazas del proceso: " + fileName);
            log.setUsuarioCreacion(jobExecution.getJobParameters().getString("codigoRegistro"));
            log.setFechaCreacion(new Timestamp(new Date().getTime()));
            logService.insertar(log);
        }
    }

    private void printThrowableLog(String text, List<Throwable> errores) {
        for (Throwable e : errores) {
            logger.error(text, e);
            if (e instanceof TaskletException) {
                TaskletException ex = (TaskletException) e;
                printExceptionLog(text, ex.getErrores());
            }
        }
    }

    private void printExceptionLog(String text, List<Exception> errores) {
        for (Exception e : errores) {
            logger.error(text, e);
        }
    }
}
