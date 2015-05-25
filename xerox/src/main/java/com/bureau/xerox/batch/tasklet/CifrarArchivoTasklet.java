package com.bureau.xerox.batch.tasklet;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;

import org.apache.log4j.Logger;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.bureau.xerox.util.Constantes.TipoOperacion;
import com.everis.enums.CifradoMetodo;
import com.everis.util.CifradoException;
import com.everis.util.CifradoUtil;

public class CifrarArchivoTasklet extends AbstractTasklet implements InitializingBean {

    private static Logger LOG = Logger.getLogger(CifradoUtil.class);

    private String operacion;
    private String archivoEntrada;
    private String archivoEntradaLlave;
    private String archivoSalida;
    private String archivoSalidaLlave;

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

    public String getArchivoEntradaLlave() {
        return archivoEntradaLlave;
    }

    public void setArchivoEntradaLlave(String archivoEntradaLlave) {
        this.archivoEntradaLlave = archivoEntradaLlave;
    }

    public String getArchivoSalidaLlave() {
        return archivoSalidaLlave;
    }

    public void setArchivoSalidaLlave(String archivoSalidaLlave) {
        this.archivoSalidaLlave = archivoSalidaLlave;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(operacion, "Debe ingresar el tipo de operaci\u00F3n");
        Assert.notNull(archivoEntrada, "Debe ingresar el archivo de entrada");
        Assert.notNull(archivoSalida, "Debe ingresar el archivo de salida");
        Assert.notNull(archivoEntradaLlave, "Debe ingresar el archivo de entrada donde se encuentra la llave del cifrado");
        Assert.notNull(archivoSalidaLlave, "Debe ingresar el archivo de salida donde se guardara la llave del cifrado");
    }

    @Override
    protected void doExecute(StepContribution contribution, ChunkContext chunkContext) throws TaskletException {
        String password = "";

        try {
            // TODO: Cambiar por AES_256_ECB_PKCS5Padding
            if (TipoOperacion.CIFRAR.equalsIgnoreCase(operacion)) {
                password = CifradoUtil.encryptZip(archivoEntrada, archivoSalida, CifradoMetodo.AES_128_ECB_PKCS7Padding);
    
                X509Certificate cert = CifradoUtil.getCertificate(archivoEntradaLlave);
                LOG.info(cert.getPublicKey());
    
                CifradoUtil.encryptString(password, archivoSalidaLlave, cert.getPublicKey(), CifradoMetodo.RSA_2048_ECB_PKCS1Padding);
            } else if (TipoOperacion.DESCIFRAR.equalsIgnoreCase(operacion)) {
                PrivateKey privateKey = CifradoUtil.getPrivateKeyRSA(archivoEntradaLlave);
                password = CifradoUtil.decryptString(archivoSalidaLlave, privateKey, CifradoMetodo.RSA_2048_ECB_PKCS1Padding);
    
                CifradoUtil.decryptZip(archivoEntrada, archivoSalida, password, CifradoMetodo.AES_128_ECB_PKCS7Padding);
            }
        } catch(CifradoException e) {
            throw new TaskletException(e);
        }
        
        contribution.incrementReadCount();
        contribution.incrementWriteCount(1);
        setExitStatus(ExitStatus.COMPLETED);
    }
}
