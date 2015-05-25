package com.bureau.xerox.model;

import java.util.List;

import com.bureau.batch.domain.JobInstanceExecution;
import com.everis.web.model.BaseModel;

public class ProcesoModel extends BaseModel {

    private static final long serialVersionUID = 1L;
    private List<JobInstanceExecution> procesos;
    private JobInstanceExecution proceso;

    public List<JobInstanceExecution> getProcesos() {
        return procesos;
    }

    public void setProcesos(List<JobInstanceExecution> procesos) {
        this.procesos = procesos;
    }

    public JobInstanceExecution getProceso() {
        return proceso;
    }

    public void setProceso(JobInstanceExecution proceso) {
        this.proceso = proceso;
    }

}
