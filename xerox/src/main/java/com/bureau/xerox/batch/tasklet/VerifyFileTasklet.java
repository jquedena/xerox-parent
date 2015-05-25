package com.bureau.xerox.batch.tasklet;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

public class VerifyFileTasklet extends AbstractTasklet implements InitializingBean {

    private String archivoOrigen;
    private List<String> archivos;

    @Override
    protected void doExecute(StepContribution contribution, ChunkContext chunkContext) throws TaskletException {
        File origen;
        if (archivoOrigen != null) {
            origen = new File(archivoOrigen);
            if (origen.exists() && origen.isFile()) {
                contribution.incrementReadCount();
                contribution.incrementWriteCount(1);
                setExitStatus(ExitStatus.COMPLETED);
            } else {
                throw new TaskletException(new FileNotFoundException("No existe el archivo [" + origen + "]"));
            }
        } else {
            String noArchivos = "";
            for (String archivo : archivos) {
                origen = new File(archivoOrigen);
                contribution.incrementReadCount();
                if (origen.exists() && origen.isFile()) {
                    contribution.incrementWriteCount(1);
                } else {
                    noArchivos += archivo + ",";
                }
            }

            setExitStatus(ExitStatus.COMPLETED);

            if (!noArchivos.isEmpty()) {
                setExitStatus(new ExitStatus("PARTITIAL"));
                throw new TaskletException(new FileNotFoundException("Los siguientes archivos no existen [" + noArchivos.substring(0, noArchivos.length() - 1) + "]"));
            }
        }
    }

    public String getArchivoOrigen() {
        return archivoOrigen;
    }

    public void setArchivoOrigen(String archivoOrigen) {
        this.archivoOrigen = archivoOrigen;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (archivoOrigen == null) {
            Assert.notNull(archivos, "Debe ingresar el archivo de origen");
        } else if (archivos == null) {
            Assert.notNull(archivoOrigen, "Debe ingresar el archivo de origen");
        }
    }

    public List<String> getArchivos() {
        return archivos;
    }

    public void setArchivos(List<String> archivos) {
        this.archivos = archivos;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("VerifyFileTasklet [");
        if (archivoOrigen != null) {
            builder.append("archivoOrigen=");
            builder.append(archivoOrigen);
            builder.append(", ");
        }
        if (archivos != null) {
            builder.append("archivos=");
            builder.append(archivos);
        }
        builder.append("]");
        return builder.toString();
    }

}
