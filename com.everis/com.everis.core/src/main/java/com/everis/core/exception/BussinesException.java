package com.everis.core.exception;

public class BussinesException extends Exception {

    private static final long serialVersionUID = 1L;

    public BussinesException() {
        super();
    }

    public BussinesException(String message, Throwable cause) {
        super(message, cause);
    }

    public BussinesException(String message) {
        super(message);
    }

    public BussinesException(Throwable cause) {
        super(cause);
    }

}
