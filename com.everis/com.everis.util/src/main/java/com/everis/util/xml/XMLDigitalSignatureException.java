package com.everis.util.xml;

import java.util.ArrayList;
import java.util.List;

public class XMLDigitalSignatureException extends Exception {

    private static final long serialVersionUID = 1L;
    private transient List<Exception> errores;

    public XMLDigitalSignatureException(String message, Throwable cause) {
        super(message, cause);
    }

    public XMLDigitalSignatureException(String message) {
        super(message);
    }

    public XMLDigitalSignatureException(Throwable cause) {
        super(cause);
    }

    public List<Exception> getErrores() {
        if(errores == null) {
            errores = new ArrayList<Exception>();
        }
        return errores;
    }

    public void setErrores(List<Exception> errores) {
        this.errores = errores;
    }

}
