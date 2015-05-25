package com.bureau.batch.service.impl;

import javax.annotation.Resource;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4Test;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bureau.batch.service.JobInstanceService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JobInstanceServiceImplTest extends AbstractJUnit4Test {

    @Resource(name = "jobInstanceService")
    private JobInstanceService jobInstanceService;

    @Test
    public void _01executeProcesoCargarAccionistas() {
        jobInstanceService.execute("procesoCargarAccionistas", "paramProcesoCargarAccionistas", "P017239");
    }

    @Test
    public void _02executeProcesoCargaArchivoCuentas() {
        jobInstanceService.execute("procesoCargaArchivoCuentas", "paramProcesoCargaArchivoCuentas", "P017239");
    }

    @Test
    public void _03executeProcesoGeneracionArchivo() {
        jobInstanceService.execute("procesoGeneracionArchivo", "paramProcesoGenerarArchivo", "P017239");
    }

    @Test
    public void _04executeProcesoDescompresionArchivo() {
        jobInstanceService.execute("procesoDescompresionArchivo", "paramProcesoDescomprimirArchivo", "P017239");
    }

}
