package com.bureau.xerox.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Service;

import com.bureau.batch.domain.JobConfig;
import com.bureau.quartz.domain.Trigger;
import com.bureau.quartz.factory.QuartzFactory;
import com.bureau.quartz.service.TriggerService;
import com.bureau.xerox.service.SchedulerService;

@Service("schedulerService")
public class SchedulerServiceImpl implements SchedulerService {

    private static final Logger LOG = Logger.getLogger(SchedulerServiceImpl.class);

    @Resource(name = "quartzScheduler")
    private Scheduler scheduler;

    @Resource(name = "triggerService")
    private TriggerService triggerService;

    @Resource(name = "quartzFactory")
    private QuartzFactory quartzFactory;
    
    @PostConstruct
    public void init() {

    }

    @Override
    public void deleteAll() {
        List<Trigger> triggers = triggerService.listar();
        for(Trigger t : triggers) {
            try {
                JobKey jobKey = JobKey.jobKey(t.getJobName(), t.getJobGroup());
                scheduler.deleteJob(jobKey);
            } catch (SchedulerException e) {
                LOG.error("No se pudo eliminar el Job: [" + t.getJobName() + "]", e);
            }
        }
    }

    @Override
    public void delete(JobConfig jobConfig) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(jobConfig.getJobName(), QuartzFactory.GROUP_NAME);
        scheduler.deleteJob(jobKey);
    }

    @Override
    public void rescheduler(JobConfig jobConfig) throws SchedulerException {
        delete(jobConfig);
        quartzFactory.createJob(jobConfig);
    }
    
     
}
