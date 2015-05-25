package com.everis.webservice;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import org.apache.log4j.Logger;

import com.everis.util.CadenaUtil;

public class SOAPClientSAAJ {

    private static final Logger LOGGER = Logger.getLogger(SOAPClientSAAJ.class);
    private static final String prefix = "saaj";
    private WSDLResource wsdlResource;    
    
    public static final String wsdlDataTypes = "(boolean|date|datetime|double|float|hexBinary|int|string|time)";
    
    public SOAPClientSAAJ() throws UnsupportedOperationException, SOAPException {
        super();
    }
    
    public SOAPClientSAAJ(WSDLResource wsdlResource) throws UnsupportedOperationException, SOAPException {
        this();
        this.wsdlResource = wsdlResource;
    }
    
    public ByteArrayOutputStream executeOperation(String operation) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        WSDLOperation wsdlOperation = wsdlResource.getOperations().get(operation);
        try {
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();
            SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(wsdlOperation), wsdlResource.getUrl());
            soapResponse.writeTo(bos);
            soapConnection.close();
            
            if(LOGGER.isInfoEnabled()) {
                LOGGER.info(new String(bos.toByteArray()));
            }
            LOGGER.info("Request byte size: " + bos.size());
        } catch(SOAPException e) {
            LOGGER.error("Error al generar la peticion SOAP", e);
        } catch (Exception e) {
            LOGGER.error("Error no experado", e);
        }
        
        return bos;
    }
    
    private void setNamespaceDeclaration(SOAPEnvelope soapEnvelope) throws SOAPException {
        soapEnvelope.addNamespaceDeclaration(prefix, wsdlResource.getTargetNamespace());
        soapEnvelope.addNamespaceDeclaration("", "");
    }
    
    private void setElement(SOAPElement soapElement, WSDLElement wsdlElement) throws SOAPException {
        for(WSDLElement attribute : wsdlElement.getAttributes()) {
            if(CadenaUtil.match(attribute.getType(), wsdlDataTypes) == 1) {
                soapElement.addChildElement(attribute.getName()).addTextNode(attribute.getValue());
            } else {
                setElement(soapElement.addChildElement(attribute.getName(), prefix), wsdlElement);
            }
        }
    }
    
    private void setBody(WSDLOperation wsdlOperation, SOAPEnvelope soapEnvelope) throws SOAPException {
        SOAPBody soapBody = soapEnvelope.getBody();
        WSDLElement input = wsdlOperation.getInput().getElement();
        SOAPElement soapElement = soapBody.addChildElement(input.getName(), prefix);
        setElement(soapElement, input);
        LOGGER.info(input.getName());
    }
    
    private SOAPMessage createSOAPRequest(WSDLOperation wsdlOperation) throws SOAPException, IOException {
        String serverURI = wsdlResource.getTargetNamespace();
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();
        SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", serverURI  + wsdlOperation.getName());
        
        setNamespaceDeclaration(soapEnvelope);
        setBody(wsdlOperation, soapEnvelope);
        
        soapMessage.saveChanges();
        
        if(LOGGER.isInfoEnabled()) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            soapMessage.writeTo(bos);
            LOGGER.info("Request SOAP Message:" + new String(bos.toByteArray()).replaceAll("<", "\n\t<"));
        }
        
        return soapMessage;     
    }
}