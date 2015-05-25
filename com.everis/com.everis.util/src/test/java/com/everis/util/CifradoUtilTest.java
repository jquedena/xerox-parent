package com.everis.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.junit4.AbstractJUnit4Test;

import com.everis.enums.CifradoMetodo;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CifradoUtilTest extends AbstractJUnit4Test {

    @Before
    public void beforeSetup() {
        this.setup();
    }

    @Test
    public void _01encrypt() {
        try {
            String key = CifradoUtil.encrypt("/mnt/compartido/fatca/load/procesado/FATCA_OECD_demo_firmado.xml", "/mnt/compartido/fatca/load/procesado/FATCA_OECD_demo_firmado.xml.encrypt", CifradoMetodo.AES_128_ECB_PKCS5Padding);
            CifradoUtil.decrypt("/mnt/compartido/fatca/load/procesado/FATCA_OECD_demo_firmado.xml.encrypt", "/mnt/compartido/fatca/load/procesado/FATCA_OECD_demo_firmado_decrypt.xml", key, CifradoMetodo.AES_128_ECB_PKCS5Padding);
        } catch (Exception e) {
            LOGGER.error(e);
            Assert.fail(e.getMessage());
        }
    }
}
