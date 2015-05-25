package com.bureau.xerox.thread;

import com.bureau.batch.service.JobInstanceService;

/**
 * Hilo para la ejecuci\u00F3n en background del proceso de carga de archivos de
 * accionistas
 * 
 * @author jquedena
 *
 */
public class JobExecuteThread extends Thread {

    private JobInstanceService jobInstanceService;
    private String codigoRegisto;
    private String jobName;
    private String jobParameters;
    
    
    
    public JobInstanceService getJobInstanceService() {
        return jobInstanceService;
    }

    public void setJobInstanceService(JobInstanceService jobInstanceService) {
        this.jobInstanceService = jobInstanceService;
    }

    public String getCodigoRegisto() {
        return codigoRegisto;
    }

    public void setCodigoRegisto(String codigoRegisto) {
        this.codigoRegisto = codigoRegisto;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobParameters() {
        return jobParameters;
    }

    public void setJobParameters(String jobParameters) {
        this.jobParameters = jobParameters;
    }

    /**
     * Recibe el servicio que ejecuta la tarea y el c\u00F3digo de registro que
     * invoca el proceso
     * 
     */
    public JobExecuteThread() {
        super();
    }

    /**
     * Ejecuta en background el proceso
     */
    @Override
    public void run() {
        jobInstanceService.execute(jobName, jobParameters, codigoRegisto);
    }

}
