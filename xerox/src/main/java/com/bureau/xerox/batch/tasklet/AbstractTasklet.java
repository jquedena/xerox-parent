package com.bureau.xerox.batch.tasklet;

import org.apache.log4j.Logger;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

/**
 * Clase que se hereda para implementar la l\u00F3gica de negocio del paso,
 * dentro del contexto de Spring Batch
 * 
 * @author jquedena
 *
 */
public abstract class AbstractTasklet implements Tasklet {

    private static final Logger LOG = Logger.getLogger(AbstractTasklet.class);
    private ExitStatus exitStatus;

    public ExitStatus getExitStatus() {
        return exitStatus;
    }

    public void setExitStatus(ExitStatus exitStatus) {
        this.exitStatus = exitStatus;
    }

    /**
     * En el contexto actual, pasa los atributos compartidos y propios de la
     * tarea para la ejecuci\u00F3n de la misma.
     * Retorna: 
     * - RepeatStatus.FINISHED si ha terminado 
     * - RepeatStatus.CONTINUABLE si la ejecuci\u00F3n de la tarea continua, 
     * en caso de fallo produce una excepci\u00F3n
     * 
     * @param contribution, atributos propios del paso que se ejecuta
     * @param chunkContext, atributos compartidos entre los pasos de la tarea, no se reinician
     * @return RepeatStatus, indica si el proceso continua
     * @throws Exception
     */
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        RepeatStatus result = RepeatStatus.FINISHED;
        StepExecution execution = chunkContext.getStepContext().getStepExecution();
        exitStatus = ExitStatus.FAILED;

        try {
            doExecute(contribution, chunkContext);
        } catch (TaskletException e) {
            LOG.error(this.toString(), e);
            execution.addFailureException(e);
            execution.getJobExecution().addFailureException(e);
            exitStatus.addExitDescription(e);
        }

        contribution.setExitStatus(exitStatus);
        execution.setExitStatus(exitStatus);
        execution.getJobExecution().setExitStatus(exitStatus);

        return result;
    }

    /**
     * Invocado desde el metodo <b>execute</b>
     * 
     * @param contribution
     * @param chunkContext
     * @throws Exception
     */
    protected abstract void doExecute(StepContribution contribution, ChunkContext chunkContext) throws TaskletException;

}
