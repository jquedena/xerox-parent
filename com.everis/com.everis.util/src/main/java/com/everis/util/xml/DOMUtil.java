package com.everis.util.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class DOMUtil {

    private static final Logger LOGGER = Logger.getLogger(DOMUtil.class);
    private static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
    private static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";
    private static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";

    /**
     * 
     * @param clazz
     * @param element
     * @param values
     * @return T
     */
    @SuppressWarnings("unchecked")
    public static <T> T createObject(Class<?> clazz, Element element, String[][] values) {
        Object o = null;
        int i;

        try {
            o = clazz.newInstance();

            for (i = 0; i < values.length; i++) {
                BeanUtilsBean.getInstance().setProperty(o, values[i][0], element.getAttribute(values[i][1]));
            }
        } catch (InstantiationException e) {
            LOGGER.error("", e);
        } catch (IllegalAccessException e) {
            LOGGER.error("", e);
        } catch (InvocationTargetException e) {
            LOGGER.error("", e);
        }

        return (o == null ? null : (T) o);
    }

    /**
     * Serializa un objeto Document en un archivo
     * 
     * @param doc
     * @param file
     * @throws TransformerException
     * @throws IOException
     * @throws Exception
     */
    public static void write(Document doc, File file) throws TransformerException, IOException {
        FileOutputStream f = new FileOutputStream(file);
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();

        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.transform(new DOMSource(doc), new StreamResult(f));

        f.close();
    }

    /**
     * Lee un Document desde un archivo
     * 
     * @param file
     * @return
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     * @throws Exception
     */
    public static Document open(File file) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;

        factory.setNamespaceAware(true);
        builder = factory.newDocumentBuilder();

        return builder.parse(file);
    }

    public static boolean verifyXMLWithXSD(String fileName, String fileNameXSD) throws XMLDigitalSignatureException {
        boolean valid = false;

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        dbf.setValidating(true);

        try {

            // Configure
            dbf.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
            dbf.setAttribute(JAXP_SCHEMA_SOURCE, new File(fileNameXSD));

            // Parser
            DOMErrorHandler errorHandler = new DOMErrorHandler();
            DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
            documentBuilder.setErrorHandler(errorHandler);

            Document parse = documentBuilder.parse(new File(fileName));
            LOGGER.error(parse.getBaseURI());

            valid = errorHandler.isValid();
            
            if(!valid) {
                XMLDigitalSignatureException ex = new XMLDigitalSignatureException("Archivo XML Inv\u00E1lido");
                ex.setErrores(errorHandler.getErrores());
                throw ex;
            }
            
        } catch (NullPointerException e) {
            throw new XMLDigitalSignatureException(e);
        } catch (ParserConfigurationException e) {
            throw new XMLDigitalSignatureException(e);
        } catch (SAXException e) {
            throw new XMLDigitalSignatureException(e);
        } catch (IOException e) {
            throw new XMLDigitalSignatureException(e);
        }

        return valid;
    }
}
