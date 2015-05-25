package com.bureau.xerox.service.impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.stereotype.Service;

import com.bureau.xerox.domain.Parametro;
import com.bureau.xerox.enums.Configuracion;
import com.bureau.xerox.service.NotificacionService;
import com.bureau.xerox.service.ParametroService;
import com.everis.mail.MailException;
import com.everis.mail.Message;
import com.everis.mail.MessageBody;
import com.everis.mail.MessageHeader;
import com.everis.mail.filter.FilterChainMail;
import com.everis.mail.manager.MailManager;
import com.everis.util.TrazaUtil;

@Service("notificacionService")
public class NotificacionServiceImpl implements NotificacionService {

    private Logger LOG = Logger.getLogger(NotificacionServiceImpl.class);
    private MailManager mailManager;

    @Resource(name = "parametroService")
    private ParametroService parametroService;

    public NotificacionServiceImpl() {
        mailManager = new MailManager();
        mailManager.setFilterChainMail(new FilterChainMail());
    }

    @Override
    public boolean notificar(JobExecution jobExecution) {
        Parametro[] params = new Parametro[5];

        params[0] = parametroService.obtener(Configuracion.PB_HOST_MAIL.getKey());
        params[1] = parametroService.obtener(Configuracion.PB_PORT_MAIL.getKey());
        params[2] = parametroService.obtener(Configuracion.PB_USER_MAIL.getKey());
        params[3] = parametroService.obtener(Configuracion.PB_PASSWD_MAIL.getKey());
        params[4] = parametroService.obtener(Configuracion.PB_LIST_SEND.getKey());

        MessageHeader header = new MessageHeader();
        header.setHost(params[0].getTexto());
        header.setPort(params[1].getTexto());
        header.setUserFrom(params[2].getTexto());
        header.setEmailFrom(params[2].getTexto());
        if (params[3] != null) {
            header.setPasswordFrom(params[3].getTexto());
        } else {
            header.setPasswordFrom("");
        }
        header.setListTO(params[4].getTexto());
        
        MessageBody body = new MessageBody();
        StringBuilder sb = new StringBuilder();
        
        if(!jobExecution.getExitStatus().getExitCode().equalsIgnoreCase("COMPLETED")) {
        	header.setSubject("FATCA - Notificaci\u00F3n de Error: " + jobExecution.getJobInstance().getJobName());
        	sb.append("Ocurri\u00F3 un error al ejecutar el proceso:<br><br>");
	        if(jobExecution.getFailureExceptions().isEmpty()) {
				for (StepExecution stepExecution : jobExecution.getStepExecutions()) {
					for (Throwable e : stepExecution.getFailureExceptions()) {
			            sb.append(TrazaUtil.mostrarMensajeHTML(new Exception(e)));
			            sb.append("<br>");
			        }	
				}
			} else {
				for (Throwable e : jobExecution.getFailureExceptions()) {
		            sb.append(TrazaUtil.mostrarMensajeHTML(new Exception(e)));
		            sb.append("<br>");
		        }	
			}
        } else {
        	header.setSubject("FATCA - Notificaci\u00F3n de Ejecuci\u00F3n: " + jobExecution.getJobInstance().getJobName());
        	sb.append("Por favor verifique el detalle de la ejecuci\u00F3n del proceso en la opci\u00F3n: \"Reporte de Procesos\"<br><br>");
        }

        sb.append("Por favor no responder a este correo.");
        body.setMessage(sb.toString());
        LOG.info(body.getMessage());

        Message message = new Message();
        message.setHeader(header);
        message.setBody(body);

        try {
        	mailManager.getFilterChainMail().send(message);
        } catch (MailException e) {
            LOG.error("No se pudo enviar", e);
        }

        return false;
    }

}
