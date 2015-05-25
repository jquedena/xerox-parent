package com.everis.util.xml;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.junit4.AbstractJUnit4Test;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class XMLDigitalSignatureTest extends AbstractJUnit4Test {

    @Before
    public void beforeSetup() {
        this.setup();
    }

    @Test
    public void _01createSignature() {
        try {
            XMLDigitalSignature.createSignatureRSA2048("/mnt/compartido/fatca/load/procesoGeneracionArchivo_1_16032015-060858.xml", "/mnt/compartido/fatca/load/procesado/FATCA_OECD_demo_firmado.xml",
                    "", "");
        } catch (XMLDigitalSignatureException e) {
            LOGGER.error("", e);
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void _02verifySignature() {
        try {
            XMLDigitalSignature.verifySignature("/mnt/compartido/fatca/load/procesado/FATCA_OECD_demo_firmado.xml");
        } catch (XMLDigitalSignatureException e) {
            LOGGER.error("", e);
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void _03verifySignatureKO() {
        try {
            XMLDigitalSignature.verifySignature("/mnt/compartido/fatca/load/procesado/FATCA_OECD_demo_firmado_KO.xml");
        } catch (XMLDigitalSignatureException e) {
            LOGGER.error("", e);
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void _04verifyFile() {
        try {
            DOMUtil.verifyXMLWithXSD("/mnt/compartido/fatca/out/xml/procesoGeneracionArchivo_71_18032015-122906.xml", "/mnt/compartido/fatca/load/FatcaXML_v1.1.xsd");
        } catch (XMLDigitalSignatureException e) {
            LOGGER.error("", e);
        }
    }
}
