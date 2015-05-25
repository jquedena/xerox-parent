package com.everis.webservice;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import com.everis.util.CadenaUtil;
import com.everis.util.xml.DOMReader;
import com.everis.util.xml.DOMReaderException;
import com.everis.util.xml.DOMUtil;
import com.everis.util.xml.filter.DOMFilter;

public class WSDLFilterReader implements DOMFilter {

    private static final Logger LOGGER = Logger.getLogger(WSDLFilterReader.class);
    private String wsdlImport = "(types|schema|import)";
    private String wsdlMessage = "(message)";
    private String wsdlOperation = "(portType|operation)";
    private String[] wsdlInputOutput = new String[]{"(portType|operation|input)", "(portType|operation|output)"};
    private String[] wsdlElement = new String[]{
            "(types|schema|complexType)",
            "(types|schema|element)",
            "(types|schema|element|complexType|sequence)",
            "(types|schema|complexType|sequence|element)",
            "(element|complexType)",
            "(complexType|sequence|element)",
            "(element|complexType|sequence)"};
    private int[] wsdlCountElement = new int[]{4, 4, 7, 6, 2, 4, 5};
    private Map<String, String> imports = new HashMap<String, String>();
    private Map<String, WSDLElement> elements = new HashMap<String, WSDLElement>();
    private Map<String, WSDLMessage> messages = new HashMap<String, WSDLMessage>();
    private Map<String, WSDLOperation> operations = new HashMap<String, WSDLOperation>();
    private WSDLMessage previousMessage;
    private WSDLOperation previousOperation;
    private WSDLElement previousElement;
    
    private WSDLElement createElement(Element element) {
        return DOMUtil.createObject(WSDLElement.class, element, new String[][]{new String[]{"name", "name"}});
    }
    
    private void createAttribute(Element element, WSDLElement object) {
        WSDLElement o = DOMUtil.createObject(WSDLElement.class, element, new String[][]{
            new String[]{"name", "name"},
            new String[]{"type", "type"}});
        if(o != null && object != null) {
            String[] parts = o.getType().split(":");
            if(parts.length > 1) {
                o.setType(parts[1]);
            }
            
            object.getAttributes().add(o);
        }
    }
    
    private WSDLOperation createOperation(Element element, String path) {
        WSDLOperation o = null;
        if(CadenaUtil.match(path, wsdlOperation) == 2) {
            o = DOMUtil.createObject(WSDLOperation.class, element, new String[][]{new String[]{"name", "name"}});
        }
        
        return o;
    }
    
    private WSDLMessage createMessage(Element element) {
        return DOMUtil.createObject(WSDLMessage.class, element, new String[][]{new String[]{"name", "name"}});
    }
    
    private void createMessagePart(Element element, WSDLMessage message) {
        WSDLMessage o = DOMUtil.createObject(WSDLMessage.class, element, new String[][]{
            new String[]{"name", "name"},
            new String[]{"part", "element"}
        });
        
        if(o != null) {
            String[] parts = o.getPart().split(":");
            if(parts.length > 1) {
                message.setElement(elements.get(parts[1]));
            }
        }
    }
    
    private void createOperationMessage(Element element, String path, WSDLOperation operation) {
        String messageName = element.getAttribute("message");
        
        if(messageName != null && !messageName.isEmpty()) {
            String[] parts = messageName.split(":");
            if(parts.length > 1) {
                if(CadenaUtil.match(path, wsdlInputOutput[0]) == 3) {
                    operation.setInput(messages.get(parts[1]));
                } else if(CadenaUtil.match(path, wsdlInputOutput[1]) == 3) {
                    operation.setOutput(messages.get(parts[1]));
                }  
            }
        }
    }
    
    public void proccessReadMessage(Element element, String path, short nivel, int partTags) {
        if(nivel == DOMFilter.NODE && partTags == 2) {
            previousMessage = createMessage(element);
            if(previousMessage != null) {
                messages.put(previousMessage.getName(), previousMessage);
                LOGGER.info("Message: " + previousMessage.getName());
            }
        } else if((nivel == DOMFilter.ELEMENT || partTags == 3) && previousMessage != null) {
            createMessagePart(element, previousMessage);
        }
    }
    
    public void proccessReadOperation(Element element, String path, short nivel) {
        WSDLOperation o;
        if(nivel == DOMFilter.NODE) {
            o = createOperation(element, path);
            if(o != null && !o.getName().isEmpty()) {
                previousOperation = o; 
                operations.put(previousOperation.getName(), previousOperation);
                LOGGER.info("Operation: " + previousOperation.getName());
            } else if(previousOperation != null) {
                createOperationMessage(element, path, previousOperation);
            }
        } else if(nivel == DOMFilter.ELEMENT && previousOperation != null) {
            createOperationMessage(element, path, previousOperation);
        }
    }
    
    public void proccessReadElement(Element element, String path, short nivel) {
        WSDLElement o;
        if(nivel == DOMFilter.NODE) {
            o = createElement(element);
            if(o != null && !o.getName().isEmpty()) {
                previousElement = o;
                elements.put(previousElement.getName(), previousElement);
                LOGGER.info("Element: " + previousElement.getName());
            }
        } else if(nivel == DOMFilter.ELEMENT) {
            createAttribute(element, previousElement);
        }
    }
    
    private void proccessImport(Element element) {
        String schemaLocation = element.getAttribute("schemaLocation");
        if(!schemaLocation.isEmpty() && !imports.containsKey(schemaLocation)) {
            LOGGER.debug("Import: " + schemaLocation);
            try {
                URL url = new URL(schemaLocation);
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                InputStream input = http.getInputStream();
                DOMReader reader = new DOMReader(input, this);
                reader.read();
            } catch (MalformedURLException e) {
                LOGGER.error("No se pudo leer el schemaLocation:" + schemaLocation, e);
            } catch (DOMReaderException e) {
                LOGGER.error("No se pudo leer el schemaLocation:" + schemaLocation, e);
            } catch (IOException e) {
                LOGGER.error("No se pudo leer el schemaLocation:" + schemaLocation, e);
            }
        }
    }
    
    @Override
    public void read(Element element, String path, short nivel) throws DOMReaderException {
        String[] tags = path.split("\\" + DOMReader.getPathSeparator());
        int matches = 0;
        
        if(CadenaUtil.match(path, wsdlOperation) == 2 && (tags.length == 3 || tags.length == 4)){
            proccessReadOperation(element, path, nivel);
        } else if(CadenaUtil.match(path, wsdlMessage) == 1){
            proccessReadMessage(element, path, nivel, tags.length);
        } else if(CadenaUtil.match(path, wsdlImport) == 3 && tags.length == 4) {
            proccessImport(element);
        } else {
            for(int i = 0; i < wsdlElement.length; i++) {
                matches = CadenaUtil.match(path, wsdlElement[i]);
                if(matches == (wsdlCountElement[i] - 1) && tags.length == wsdlCountElement[i]) {
                    proccessReadElement(element, path, nivel);
                    break;
                }
            }
        }
    }

    public Map<String, WSDLOperation> getOperations() {
        return operations;
    }

    public void setOperations(Map<String, WSDLOperation> operations) {
        this.operations = operations;
    }

    public Map<String, WSDLMessage> getMessages() {
        return messages;
    }

    public void setMessages(Map<String, WSDLMessage> messages) {
        this.messages = messages;
    }

    public Map<String, WSDLElement> getElements() {
        return elements;
    }

    public void setElements(Map<String, WSDLElement> elements) {
        this.elements = elements;
    }
}
