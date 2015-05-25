package com.everis.util.xml;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class DOMErrorHandler implements ErrorHandler {

    private static final Logger LOGGER = Logger.getLogger(DOMErrorHandler.class);
    private boolean valid;
    private transient List<Exception> errores;

    public DOMErrorHandler() {
        errores = new ArrayList<Exception>();
        valid = true;
    }

    private void error(SAXParseException exception, String text) {
        valid = false;
        errores.add(exception);
        LOGGER.error(text, exception);
    }
    
    @Override
    public void warning(SAXParseException exception) throws SAXException {
        error(exception, "Verify Warning");
    }

    @Override
    public void fatalError(SAXParseException exception) throws SAXException {
        error(exception, "Verify Fatal Error");
    }

    @Override
    public void error(SAXParseException exception) throws SAXException {
        error(exception, "Verify Error");
    }

    public boolean isValid() {
        return valid;
    }

    public List<Exception> getErrores() {
        return errores;
    }
}
