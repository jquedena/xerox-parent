package com.bureau.xerox.batch.tasklet;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.bureau.xerox.util.Constantes.TipoOperacion;
import com.everis.enums.FormatoFecha;
import com.everis.util.FechaUtil;
import com.everis.util.xml.XMLDigitalSignature;
import com.everis.util.xml.XMLDigitalSignatureException;

public class FirmarArchivoTasklet extends AbstractTasklet implements InitializingBean {

    private String operacion;
    private String archivoEntrada;
    private String archivoSalida;
    private String certificado;
    private String llaveCertificadoBBVA;

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

    public String getCertificado() {
        return certificado;
    }

    public void setCertificado(String certificado) {
        this.certificado = certificado;
    }

    public String getLlaveCertificadoBBVA() {
        return llaveCertificadoBBVA;
    }

    public void setLlaveCertificadoBBVA(String llaveCertificadoBBVA) {
        this.llaveCertificadoBBVA = llaveCertificadoBBVA;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(operacion, "Debe ingresar el tipo de operaci\u00F3n");
        Assert.notNull(archivoEntrada, "Debe ingresar el archivo de entrada");
        Assert.notNull(archivoSalida, "Debe ingresar el archivo de salida");
        Assert.notNull(certificado, "Debe ingresar el certificado");
        Assert.notNull(llaveCertificadoBBVA, "Debe ingresar la llave del certificado");
    }

    @Override
    protected void doExecute(StepContribution contribution, ChunkContext chunkContext) throws TaskletException {
        JobExecution jobExecution = chunkContext.getStepContext().getStepExecution().getJobExecution();
        archivoEntrada = archivoEntrada.replaceAll("jobName", jobExecution.getJobInstance().getJobName());
        archivoEntrada = archivoEntrada.replaceAll("jobId", jobExecution.getJobId().toString());
        archivoEntrada = archivoEntrada.replaceAll("createTime", FechaUtil.formatFecha(jobExecution.getCreateTime(), FormatoFecha.DDMMYYYY_HH24MMSS));

        try {
            if (TipoOperacion.FIRMAR.equalsIgnoreCase(operacion)) {
                XMLDigitalSignature.createSignatureRSA2048(archivoEntrada, archivoSalida, certificado, llaveCertificadoBBVA);
            } else if (TipoOperacion.VERIFICAR.equalsIgnoreCase(operacion) && !XMLDigitalSignature.verifySignature(archivoEntrada)) {
                throw new XMLDigitalSignatureException("Archivo invalido");
            }
        } catch(XMLDigitalSignatureException e) {
            throw new TaskletException(e);
        }
        
        contribution.incrementReadCount();
        contribution.incrementWriteCount(1);
        setExitStatus(ExitStatus.COMPLETED);
    }
}
