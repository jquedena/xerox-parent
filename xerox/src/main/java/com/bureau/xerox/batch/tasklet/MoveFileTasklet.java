package com.bureau.xerox.batch.tasklet;

import java.io.File;
import java.io.IOException;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.everis.enums.FormatoFecha;
import com.everis.util.ArchivoUtil;
import com.everis.util.FechaUtil;

public class MoveFileTasklet extends AbstractTasklet implements InitializingBean {

    private String archivoOrigen;
    private String directorioDestino;

    @Override
    protected void doExecute(StepContribution contribution, ChunkContext chunkContext) throws TaskletException {
        File origen = new File(archivoOrigen);
        File destino = new File(ArchivoUtil.completeFileSeparator(directorioDestino) + origen.getName() + FechaUtil.getAhoraString(FormatoFecha.DDMMYYYY_HH24MMSS));
        
        
        try {
            ArchivoUtil.mover(origen, destino, false);
        } catch (IOException e) {
            throw new TaskletException(e);
        }

        contribution.incrementWriteCount(1);
        setExitStatus(ExitStatus.COMPLETED);
    }

    public String getArchivoOrigen() {
        return archivoOrigen;
    }

    public void setArchivoOrigen(String archivoOrigen) {
        this.archivoOrigen = archivoOrigen;
    }

    public String getDirectorioDestino() {
        return directorioDestino;
    }

    public void setDirectorioDestino(String directorioDestino) {
        this.directorioDestino = directorioDestino;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(archivoOrigen, "Debe ingresar el archivo de origen");
        Assert.notNull(directorioDestino, "Debe ingresar el directorio de destino");
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("MoveFileTasklet [archivoOrigen=");
        builder.append(archivoOrigen);
        builder.append(", directorioDestino=");
        builder.append(directorioDestino);
        builder.append("]");
        return builder.toString();
    }
}
