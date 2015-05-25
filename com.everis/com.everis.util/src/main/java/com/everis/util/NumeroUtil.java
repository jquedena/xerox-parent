package com.everis.util;


import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import org.apache.log4j.Logger;

import com.everis.enums.FormatoNumero;

/**
 * NumeroUtil contiene metodos genericos para manejo de valores numericos
 *
 * @author jquedena
 * @version 1.0
 */
public class NumeroUtil {

    private static final Logger log = Logger.getLogger(NumeroUtil.class);
    public static final BigDecimal ONE_NEGATIVE = new BigDecimal(-1);
    public static final BigDecimal ONE_HUNDRED = new BigDecimal(100);

    public NumeroUtil() {
        super();
    }

    /**
     * Retorna un numero dado en el formato indicado
     *
     * @param valor   {@link Number}
     * @param formato {@link FormatoNumero}
     * @return {@link String}, Si el numero no se formatear devuelve ""
     */
    public static String format(Number valor, String formato) {
        String result = "";
        try {
            DecimalFormatSymbols symbol = new DecimalFormatSymbols(Locale.ENGLISH);
            DecimalFormat formatter = new DecimalFormat(formato, symbol);
            result = formatter.format(valor);
        } catch (Exception e) {
            log.error("NumeroUtil:format", e);
        }
        return result;
    }
    
    /**
     * Retorna un numero dado en el formato indicado
     *
     * @param valor   {@link Number}
     * @param formato {@link FormatoNumero}
     * @return {@link String}, Si el numero no se formatear devuelve ""
     */
    public static String format(Number valor, FormatoNumero formato) {
        return format(valor, formato.toString());
    }

    /**
     * Retorna un numero dado en el formato #0.00
     *
     * @param valor {@link Number}
     * @return {@link String}, Si el numero no se formatear devuelve ""
     */
    public static String format(Number valor) {
        return NumeroUtil.format(valor, FormatoNumero.ES_PE_2DEC);
    }

    /**
     * Suma los numeros enviados
     *
     * @param valores {@link Number}
     * @return {@Number}, si algunos de los valores es {@code null} no se considera
     */
    public static Number add(Number... valores) {
        double s = 0;

        for (Number n : valores) {
            if (n != null) {
                s += n.doubleValue();
            }
        }

        return s;
    }

    /**
     * Multipicla los numeros enviados
     *
     * @param valores {@link Number}
     * @return {@Number}, si algunos de los valores es {@code null} no se considera
     */
    public static Number multiply(Number... valores) {
        double s = 0;

        for (Number n : valores) {
            if (n != null) {
                s *= n.doubleValue();
            }
        }

        return s;
    }
}
