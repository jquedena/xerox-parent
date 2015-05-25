package com.bureau.xerox.batch.tasklet;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.bureau.xerox.util.Constantes.EstadoProcesoProcedure;
import com.everis.core.exception.BussinesException;
import com.everis.util.DBUtilSpring;

public class ExecuteStoreProcedureTasklet extends AbstractTasklet implements InitializingBean {

    private static final Logger LOG = Logger.getLogger(ExecuteStoreProcedureTasklet.class);
    private String storeProcedure;
    private String dataSourceName;

    @Override
    protected void doExecute(StepContribution contribution, ChunkContext chunkContext) throws TaskletException {
        StepExecution execution = chunkContext.getStepContext().getStepExecution();

        LOG.error("JobID: " + execution.getJobExecutionId());
        LOG.error("StepID: " + execution.getId());

        Map<String, Object> inParams = new HashMap<String, Object>();
        inParams.put("USUARIO", execution.getJobExecution().getJobParameters().getString("codigoRegistro"));
        inParams.put("ID_PROCESO", execution.getJobExecution().getJobInstance().getId());

        Map<String, Integer> outParams = new HashMap<String, Integer>();
        outParams.put("FILAS_LEIDAS", Types.NUMERIC);
        outParams.put("FILAS_ESCRITAS", Types.NUMERIC);
        outParams.put("ESTADO", Types.VARCHAR);
        outParams.put("DESCRIPCION", Types.VARCHAR);

        Map<String, Object> out;
        try {
            out = DBUtilSpring.getInstance().executeProcedure(dataSourceName, storeProcedure, inParams, outParams);
        } catch (BussinesException e) {
            throw new TaskletException(e);
        }

        if (out.get("ESTADO") != null
                && !(EstadoProcesoProcedure.COMPLETED.equalsIgnoreCase(out.get("ESTADO").toString()) || EstadoProcesoProcedure.PARTITIAL.equalsIgnoreCase(out.get("ESTADO").toString()))) {
            throw new TaskletException(String.valueOf(out.get("DESCRIPCION")));
        }

        if (out.get("FILAS_LEIDAS") != null) {
            for (int i = 0; i < Integer.parseInt(out.get("FILAS_LEIDAS").toString()); i++) {
                contribution.incrementReadCount();
            }
        }

        if (out.get("FILAS_ESCRITAS") != null) {
            contribution.incrementWriteCount(Integer.parseInt(out.get("FILAS_ESCRITAS").toString()));
        }

        setExitStatus(new ExitStatus(out.get("DESCRIPCION").toString()));
    }

    public String getStoreProcedure() {
        return storeProcedure;
    }

    public void setStoreProcedure(String storeProcedure) {
        this.storeProcedure = storeProcedure;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(storeProcedure);
        Assert.notNull(dataSourceName);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ExecuteStoreProcedureTasklet [storeProcedure=");
        builder.append(storeProcedure);
        builder.append(", dataSourceName=");
        builder.append(dataSourceName);
        builder.append("]");
        return builder.toString();
    }
}
