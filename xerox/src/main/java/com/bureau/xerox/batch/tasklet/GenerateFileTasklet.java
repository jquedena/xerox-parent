package com.bureau.xerox.batch.tasklet;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;

import com.bureau.xerox.service.ParametroService;
import com.everis.enums.FormatoFecha;
import com.everis.util.FechaUtil;

public class GenerateFileTasklet extends AbstractTasklet {

    private String nombreArchivo;
    private ParametroService parametroService;

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public ParametroService getParametroService() {
        return parametroService;
    }

    public void setParametroService(ParametroService parametroService) {
        this.parametroService = parametroService;
    }

    @Override
    protected void doExecute(StepContribution contribution, ChunkContext chunkContext) throws TaskletException {
        JobExecution jobExecution = chunkContext.getStepContext().getStepExecution().getJobExecution();
        nombreArchivo = nombreArchivo.replaceAll("jobName", jobExecution.getJobInstance().getJobName());
        nombreArchivo = nombreArchivo.replaceAll("jobId", jobExecution.getJobId().toString());
        nombreArchivo = nombreArchivo.replaceAll("createTime", FechaUtil.formatFecha(jobExecution.getCreateTime(), FormatoFecha.DDMMYYYY_HH24MMSS));

        
    }
}
