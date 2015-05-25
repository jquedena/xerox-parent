package com.bureau.quartz;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.bureau.batch.service.JobInstanceService;
import com.everis.web.listener.WebServletContextListener;

public class ExecuteJob extends QuartzJobBean {

    private static final Logger LOG = Logger.getLogger(ExecuteJob.class);
    private String codigoRegisto;
    private String jobName;
    private String jobProcessorParameter;

    @Override
    protected void executeInternal(JobExecutionContext arg) throws JobExecutionException {
    	
    	if(codigoRegisto == null) {
    		codigoRegisto = arg.getJobDetail().getJobDataMap().getString("codigoRegistro");
    	}
    	LOG.info("C\u00F3digo de Registro: " + codigoRegisto);
    	
        LOG.info("ExecuteJob:executeInternal: [Group:" + arg.getJobDetail().getKey().getGroup() + "][Name:" + arg.getJobDetail().getKey().getName() + "]");
        JobInstanceService jobInstanceService = WebServletContextListener.getBean("jobInstanceService");
        jobInstanceService.execute(jobName, jobProcessorParameter, codigoRegisto);
    }

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobProcessorParameter() {
		return jobProcessorParameter;
	}

	public void setJobProcessorParameter(String jobProcessorParameter) {
		this.jobProcessorParameter = jobProcessorParameter;
	}

	public String getCodigoRegisto() {
		return codigoRegisto;
	}

	public void setCodigoRegisto(String codigoRegisto) {
		this.codigoRegisto = codigoRegisto;
	}

}
