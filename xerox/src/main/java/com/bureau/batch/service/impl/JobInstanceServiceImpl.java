package com.bureau.batch.service.impl;

import java.io.Serializable;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bureau.batch.service.JobInstanceService;
import com.bureau.xerox.batch.processor.JobProcessorParameter;
import com.bureau.xerox.dao.JobInstanceDAO;
import com.everis.web.listener.WebServletContextListener;

/*
 * (non-Javadoc)
 * 
 * @see
 * com.bbva.batch.service.JobInstanceService
 */
@Service("jobInstanceService")
public class JobInstanceServiceImpl implements Serializable, JobInstanceService {

	private static final Logger LOG = Logger.getLogger(JobInstanceServiceImpl.class);
    private static final long serialVersionUID = 1L;

    @Resource(name = "jobInstanceDAO")
    private JobInstanceDAO jobInstanceDAO;

    @Resource(name = "jobLauncher")
    private JobLauncher jobLauncher;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.bbva.batch.service.JobInstanceService#obtenerUltimaInstancia(String)
     */
    @Transactional(readOnly = true)
    @Override
    public Long obtenerUltimaInstancia(String name) {
        return jobInstanceDAO.obtenerUltimaInstancia(name);
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * com.bbva.batch.service.JobInstanceService#execute(String, String, String)
     */
	@Override
	public void execute(String jobName, String jobProcessorParameter, String codigoRegistro) {
        Job job = WebServletContextListener.getBean(jobName);
        JobProcessorParameter processorParameter = WebServletContextListener.getBean(jobProcessorParameter);
        JobParameters param = processorParameter.processParameters(codigoRegistro);
        
        JobExecution execution;
        try {
            execution = jobLauncher.run(job, param);
            Long outId = execution.getId();
            LOG.error("Id Proceso: [" + outId + "]");
            LOG.error("Estado del Proceso: " + execution.getStatus());
            if (!execution.getAllFailureExceptions().isEmpty()) {
                LOG.error("Estado del Proceso: " + execution.getAllFailureExceptions());
            }
        } catch (JobExecutionAlreadyRunningException e) {
            LOG.error("", e);
        } catch (JobRestartException e) {
            LOG.error("", e);
        } catch (JobInstanceAlreadyCompleteException e) {
            LOG.error("", e);
        } catch (JobParametersInvalidException e) {
            LOG.error("", e);
        }
	}

}
