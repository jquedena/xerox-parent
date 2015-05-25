package com.bureau.xerox.batch.tasklet;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.bureau.xerox.domain.Parametro;
import com.bureau.xerox.enums.Configuracion;
import com.bureau.xerox.service.ParametroService;
import com.bureau.xerox.util.Constantes;
import com.everis.util.ArchivoUtil;

public class VerifyFileFromParameterTasklet extends AbstractTasklet implements InitializingBean {

    private ParametroService parametroService;

    @Override
    protected void doExecute(StepContribution contribution, ChunkContext chunkContext) throws TaskletException {
        File origen;
        String noArchivos = "";
        Parametro param = parametroService.obtener(Configuracion.PB_DIRECTORIO_ENTRADA.getKey());
        List<Parametro> params = parametroService.listarHijos(Constantes.Parametro.LISTA_ARCHIVOS);
        String directorio = ArchivoUtil.completeFileSeparator(param.getTexto());

        for (Parametro p : params) {
            if (p.getBooleano().compareTo(new Character('S')) == 0) {
                origen = new File(directorio + p.getTexto());
                contribution.incrementReadCount();
                if (origen.exists() && origen.isFile()) {
                    contribution.incrementWriteCount(1);
                } else {
                    noArchivos += p.getTexto() + ",";
                }
            }
        }

        setExitStatus(ExitStatus.COMPLETED);

        if (!noArchivos.isEmpty()) {
            setExitStatus(new ExitStatus("PARTITIAL"));
            throw new TaskletException(new FileNotFoundException("Los siguientes archivos no existen [" + noArchivos.substring(0, noArchivos.length() - 1) + "]"));
        }
    }

    public ParametroService getParametroService() {
        return parametroService;
    }

    public void setParametroService(ParametroService parametroService) {
        this.parametroService = parametroService;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(parametroService, "Debe asignar el servicio para la gesti\u00F3n de parametros");
    }

}
