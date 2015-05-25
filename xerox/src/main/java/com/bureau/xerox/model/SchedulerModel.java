package com.bureau.xerox.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;

import com.bureau.quartz.domain.Trigger;
import com.everis.web.model.BaseModel;

public class SchedulerModel extends BaseModel {

    private static final long serialVersionUID = 1L;
    private List<JobExecution> runningJobInstances;
    private List<Trigger> triggerInstances;
    private String cronTrigger;
    private JobInstance jobInstance;
    private String time;

    public List<JobExecution> getRunningJobInstances() {
        if (runningJobInstances == null) {
            runningJobInstances = new ArrayList<JobExecution>();
        }
        return runningJobInstances;
    }

    public void setRunningJobInstances(List<JobExecution> runningJobInstances) {
        this.runningJobInstances = runningJobInstances;
    }

    public List<Trigger> getTriggerInstances() {
        return triggerInstances;
    }

    public void setTriggerInstances(List<Trigger> triggerInstances) {
        this.triggerInstances = triggerInstances;
    }

    public String getCronTrigger() {
        return cronTrigger;
    }

    public void setCronTrigger(String cronTrigger) {
        this.cronTrigger = cronTrigger;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public JobInstance getJobInstance() {
        return jobInstance;
    }

    public void setJobInstance(JobInstance jobInstance) {
        this.jobInstance = jobInstance;
    }
}
