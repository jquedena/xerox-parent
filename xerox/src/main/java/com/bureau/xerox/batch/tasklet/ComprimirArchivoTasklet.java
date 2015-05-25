package com.bureau.xerox.batch.tasklet;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.bureau.xerox.util.Constantes.TipoOperacion;
import com.everis.enums.FormatoFecha;
import com.everis.util.ArchivoUtil;
import com.everis.util.FechaUtil;

public class ComprimirArchivoTasklet extends AbstractTasklet implements InitializingBean {

    private String operacion;
    private String nombreArchivo;
    private String archivoEntrada;
    private String archivoSalida;

    public String getOperacion() {
        return operacion;
    }

    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }

    public String getArchivoEntrada() {
        return archivoEntrada;
    }

    public void setArchivoEntrada(String archivoEntrada) {
        this.archivoEntrada = archivoEntrada;
    }

    public String getArchivoSalida() {
        return archivoSalida;
    }

    public void setArchivoSalida(String archivoSalida) {
        this.archivoSalida = archivoSalida;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(operacion, "Debe ingresar el tipo de operaci\u00F3n");
        Assert.notNull(archivoEntrada, "Debe ingresar el archivo de entrada");
        Assert.notNull(archivoSalida, "Debe ingresar el archivo de salida");
    }

    @Override
    protected void doExecute(StepContribution contribution, ChunkContext chunkContext) throws TaskletException {
        try {
            if (TipoOperacion.COMPRIMIR.equalsIgnoreCase(operacion)) {
                String[] archivos = archivoEntrada.split(",");
                ArchivoUtil.zip(Arrays.asList(archivos), archivoSalida);
            } else if (TipoOperacion.DESCOMPRIMIR.equalsIgnoreCase(operacion)) {
                ArchivoUtil.unzip(archivoEntrada, archivoSalida);
            } else if (TipoOperacion.DESCOMPRIMIR_FINAL.equalsIgnoreCase(operacion)) {
                JobExecution jobExecution = chunkContext.getStepContext().getStepExecution().getJobExecution();
                nombreArchivo = nombreArchivo.replaceAll("jobName", jobExecution.getJobInstance().getJobName());
                nombreArchivo = nombreArchivo.replaceAll("jobId", jobExecution.getJobId().toString());
                nombreArchivo = nombreArchivo.replaceAll("createTime", FechaUtil.formatFecha(jobExecution.getCreateTime(), FormatoFecha.DDMMYYYY_HH24MMSS));
                
                List<String> archivos = ArchivoUtil.unzip(archivoEntrada, archivoSalida);
                if(archivos.size() == 1) {
                    String archivoProceso = ArchivoUtil.completeFileSeparator(archivoSalida) + archivos.get(0);
                    ArchivoUtil.mover(archivoProceso, nombreArchivo, true);
                } else {
                    throw new TaskletException("No se soporta m\u00E1s de un archivo en la respuesta");
                }
            }
        } catch(IOException e) {
            throw new TaskletException(e);
        } catch (Exception e) {
            throw new TaskletException(e);
        }
        
        contribution.incrementReadCount();
        contribution.incrementWriteCount(1);
        setExitStatus(ExitStatus.COMPLETED);
    }
}
