package com.bureau.quartz.factory.impl;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.stereotype.Component;

import com.bureau.batch.domain.JobConfig;
import com.bureau.quartz.ExecuteJob;
import com.bureau.quartz.factory.QuartzFactory;
import com.bureau.xerox.domain.Parametro;
import com.bureau.xerox.service.ParametroService;
import com.bureau.xerox.util.Constantes;
import com.everis.util.FechaUtil;

/**
 * F\u00E1brica de trabajos
 * @author jquedena
 *
 */
@Component("quartzFactory")
public class QuartzFactoryImpl implements QuartzFactory {

    private static final Logger LOG = Logger.getLogger(QuartzFactoryImpl.class);
    
    @Resource(name = "quartzScheduler")
    private Scheduler scheduler;
    
    @Resource(name = "parametroService")
    private ParametroService parametroService;
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * com.bbva.quartz.factory.QuartzFactory#createJob(com.bbva.batch.domain.JobConfig)
     */
    @Override
    public void createJob(JobConfig jobConfig) {
    	// ss mm HH dd MM wd *
    	
        String name = jobConfig.getJobName() + "Cron";
        String cronSchedule = "0 " + jobConfig.getMinute() 
        		+ " " + jobConfig.getHour()
        		+ " " + jobConfig.getDayOfMonth()
        		+ " " + jobConfig.getMonth()
        		+ " ? *";
        LOG.error("cronSchedule: [" + cronSchedule + "]");
        
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("jobName", jobConfig.getJobName());
        jobDataMap.put("jobProcessorParameter", jobConfig.getJobProcessorParameter());
        jobDataMap.put("codigoRegistro", jobConfig.getCodigoRegistro());
        JobKey jobKey = new JobKey(jobConfig.getJobName(), GROUP_NAME);
        JobDetail jobDetail = JobBuilder
                .newJob(ExecuteJob.class)
                .withIdentity(jobKey)
                .setJobData(jobDataMap)
                .build();

        TriggerKey triggerKey = TriggerKey.triggerKey(name, GROUP_NAME);
        Trigger cronTrigger = TriggerBuilder
                .newTrigger()
                .withIdentity(triggerKey)
                .startNow()
                .withSchedule(CronScheduleBuilder.cronSchedule(cronSchedule))
                .build();
        
        try {
            scheduler.deleteJob(jobKey);
            scheduler.scheduleJob(jobDetail, cronTrigger);
        } catch (Exception e) {
            LOG.error("No se pudo programar la tarea", e);
        }
    }

    /**
     * Al crearse la instancia, se crear los trabajos de manera din\u00E1mica
     */
    @PostConstruct
    public void init() {
    	JobConfig jobConfig;
    	String hora[];
    	String job[];
    	List<Parametro> procesos = parametroService.listarHijos(Constantes.Parametro.TAREAS_PROGRAMADAS);
        for(Parametro param : procesos) {
        	hora = param.getHora().split(":");
        	job = param.getTexto().split(":");
        	jobConfig = new JobConfig();
        	jobConfig.setJobName(job[0]);
        	jobConfig.setJobProcessorParameter(job[1]);
        	jobConfig.setDayOfMonth(FechaUtil.getDia(param.getFecha()));
        	jobConfig.setMonth(FechaUtil.getMes(param.getFecha()));
        	jobConfig.setYear(FechaUtil.getAnio(param.getFecha()));
        	jobConfig.setHour(Integer.parseInt(hora[0]));
        	jobConfig.setMinute(Integer.parseInt(hora[1]));
            createJob(jobConfig);
        }
    }
}
