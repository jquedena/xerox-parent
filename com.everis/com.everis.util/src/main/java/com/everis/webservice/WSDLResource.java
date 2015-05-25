package com.everis.webservice;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.everis.util.xml.DOMReader;
import com.everis.util.xml.DOMReaderException;

public class WSDLResource {

    private static final Logger LOGGER = Logger.getLogger(WSDLResource.class);
    private Map<String, WSDLElement> elements;
    private Map<String, WSDLMessage> messages;
    private Map<String, WSDLOperation> operations;
    private String targetNamespace;
    private String url;

    public WSDLResource() {
    }
    
    public WSDLResource(File file) {
        try {
            InputStream input = new FileInputStream(file);
            init(input);
        } catch (IOException e) {
            LOGGER.error("No se pudo obtener el InputStream desde el archivo", e);
        }
    }
    
    public WSDLResource(String uri) {
        try {
            this.url = uri;
            URL url = new URL(uri);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            InputStream input = http.getInputStream();
            init(input);
        } catch (IOException e) {
            LOGGER.error("No se pudo obtener el InputStream desde la URL", e);
        }
    }

    public void init(InputStream input) {
        WSDLFilterReader wsdlFilterReader = new WSDLFilterReader();
        try {
            DOMReader reader = new DOMReader(input, wsdlFilterReader);
            reader.read();
            targetNamespace = reader.getFirstElement().getAttributes().getNamedItem("targetNamespace").getNodeValue();
            elements = wsdlFilterReader.getElements();
            messages = wsdlFilterReader.getMessages();
            operations = wsdlFilterReader.getOperations();
        } catch (DOMReaderException e) {
            LOGGER.error("No se pudo leer el InputStream", e);
            elements = new HashMap<String, WSDLElement>();
            messages = new HashMap<String, WSDLMessage>();
            operations = new HashMap<String, WSDLOperation>();
        }
    }
    
    public Map<String, WSDLElement> getElements() {
        return elements;
    }

    public void setElements(Map<String, WSDLElement> elements) {
        this.elements = elements;
    }

    public Map<String, WSDLMessage> getMessages() {
        return messages;
    }

    public void setMessages(Map<String, WSDLMessage> messages) {
        this.messages = messages;
    }

    public Map<String, WSDLOperation> getOperations() {
        return operations;
    }

    public void setOperations(Map<String, WSDLOperation> operations) {
        this.operations = operations;
    }

    public String getTargetNamespace() {
        return targetNamespace;
    }

    public void setTargetNamespace(String targetNamespace) {
        this.targetNamespace = targetNamespace;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
    public WSDLOperation getOperation(String operationName) {
        return operations.get(operationName);
    }
}
