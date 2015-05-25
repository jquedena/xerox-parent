package com.everis.util;

import org.apache.log4j.Logger;

/**
 * <b>TraceError</b>
 * <p/>
 * Clase para formatear las excepciones generadas
 */
public final class TrazaUtil {
    private static final Logger LOG = Logger.getLogger(TrazaUtil.class);

    private TrazaUtil() {
    }

    /**
     * Transoforma la excepcion a formato HTML
     *
     * @param e Excepcion lanzada
     * @return {@link String}, Excepcion en una cadena
     */
    public static String mostrarMensajeHTML(Exception e) {
        StackTraceElement[] stackTraceElement = null;
        StringBuilder trace = new StringBuilder();

        try {
            trace.append("Error: <b>");
            trace.append(e.getMessage());
            trace.append("</b><br/>Traza:<br/>");
            stackTraceElement = e.getStackTrace();

            if (stackTraceElement != null && stackTraceElement.length > 0) {
                for (StackTraceElement elem : stackTraceElement) {
                    trace.append(elem);
                    trace.append("<br/>");
                }
            }
        } catch (Exception ex) {
            trace = new StringBuilder(e.getMessage());
            LOG.error(e);
        }

        return trace.toString();
    }

    /**
     * Transoforma la excepcion a formato text
     *
     * @param e Excepcion lanzada
     * @return {@link String}, Excepcion en una cadena
     */
    public static String mostrarMensajeText(Exception e) {
        StackTraceElement[] stackTraceElement = null;
        StringBuilder trace = new StringBuilder();

        try {
            trace.append("Error: \n");
            trace.append(e.getMessage());
            trace.append("\nTraza:\n");
            stackTraceElement = e.getStackTrace();

            if (stackTraceElement != null && stackTraceElement.length > 0) {
                for (StackTraceElement elem : stackTraceElement) {
                    trace.append(elem);
                    trace.append("\n");
                }
            }
        } catch (Exception ex) {
            trace = new StringBuilder(e.getMessage());
            LOG.error(e);
        }

        return trace.toString();
    }
}
