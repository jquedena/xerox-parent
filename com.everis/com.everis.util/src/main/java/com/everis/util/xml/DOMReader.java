package com.everis.util.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.everis.util.xml.filter.DOMFilter;

public class DOMReader implements DOMFilter {

    private static final Logger LOGGER = Logger.getLogger(DOMReader.class);
    private static String pathSeparator = "$";
    private Document document;
    private Node firstElement;
    private List<DOMFilter> filters;

    public static String getPathSeparator() {
        return pathSeparator;
    }
    
    public static void setPathSeparator(String pathSeparator) {
        DOMReader.pathSeparator = pathSeparator;
    }

    public DOMReader(InputStream input) throws DOMReaderException {
        super();
        document = obtenerDocumento(input);
        this.filters = new ArrayList<DOMFilter>();
    }
    
    public DOMReader(InputStream input, List<DOMFilter> filters) throws DOMReaderException {
        this(input);
        this.filters = filters;
    }

    public DOMReader(InputStream input, DOMFilter filter) throws DOMReaderException {
        this(input);
        this.filters.add(filter);
    }
    
    public List<DOMFilter> getFilters() {
        return filters;
    }

    public void setFilters(List<DOMFilter> filters) {
        this.filters = filters;
    }

    private Document obtenerDocumento(InputStream io) throws DOMReaderException {
        Document document = null;
        try {
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(io);
        } catch (SAXException e) {
            throw new DOMReaderException("", e);
        } catch (IOException e) {
            throw new DOMReaderException("", e);
        } catch (ParserConfigurationException e) {
            throw new DOMReaderException("", e);
        }

        return document;
    }

    private void obtenerElementos(Node node, String path) throws DOMReaderException {
        Node item;
        short type;
        String pathLocal;
        for (int j = 0; j < node.getChildNodes().getLength(); j++) {
            item = node.getChildNodes().item(j);
            type = (item.getChildNodes().getLength() > 0 ? DOMFilter.NODE : DOMFilter.ELEMENT);
            pathLocal = path + item.getNodeName();
            if (item.getNodeType() == Node.ELEMENT_NODE) {
                LOGGER.debug(pathLocal + " --> [Type: " + type + "]");
                read((Element) item, pathLocal, type);            
                if (type == DOMFilter.NODE) {
                    obtenerElementos(item, pathLocal + pathSeparator);
                }
            }
        }
    }

    public void read(Element element, String path, short nivel) throws DOMReaderException {
        for (DOMFilter filter : filters) {
            filter.read(element, path, nivel);
        }
    }
    
    public void read() throws DOMReaderException {
        firstElement = document.getFirstChild();
        while(firstElement.getNodeType() != Node.ELEMENT_NODE) {
            firstElement = firstElement.getNextSibling();
        }

        obtenerElementos(firstElement, pathSeparator);
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public Node getFirstElement() {
        return firstElement;
    }

    public void setFirstElement(Node firstElement) {
        this.firstElement = firstElement;
    }
}
