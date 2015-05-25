package com.bureau.xerox.batch.tasklet;

import java.util.ArrayList;
import java.util.List;

public class TaskletException extends Exception {

    private static final long serialVersionUID = 1L;
    private transient List<Exception> errores;

    public TaskletException() {
        super();
    }

    public TaskletException(String message, Throwable cause) {
        super(message, cause);
    }

    public TaskletException(String message) {
        super(message);
    }

    public TaskletException(Throwable cause) {
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
