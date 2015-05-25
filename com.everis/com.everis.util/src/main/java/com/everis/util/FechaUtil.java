package com.everis.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.everis.enums.FormatoFecha;

/**
 * {@code FechaUtil}, contiene metodos genericos para manejo de fechas
 *
 * @author jquedena
 * @version 1.0
 */
public class FechaUtil {

    private static final Logger LOG = Logger.getLogger(FechaUtil.class);
    private static final long MILLSECS_PER_DAY = 24 * 60 * 60 * 1000;
    private static final String TIME12HOURS_PATTERN = "(1[012]|[1-9]):[0-5][0-9](\\s)?(?i)(am|pm)";
    private static final String TIME24HOURS_PATTERN = "([01]?[0-9]|2[0-3]):[0-5][0-9]";
    private static final String DATE_PATTERN = "(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/((19|20)\\d\\d)";

    public FechaUtil() {
        super();
    }

    /**
     * @return {@link Date}, fecha actual del sistema.
     */
    public static Date getAhora() {
        return new Date();
    }

    /**
     * @return {@link Calendar}, fecha actual del sistema.
     */
    public static Calendar getAhoraCalendar() {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(FechaUtil.getAhora());
        return calendar;
    }

    /**
     * @return {@link String}, fecha actual en el formato dd/MM/yyyy.
     */
    public static String getAhoraString() {
        return FechaUtil.formatFecha(FechaUtil.getAhora());
    }

    /**
     * @param formato {@link FormatoFecha}
     * @return {@link String}, fecha actual en el formato especificado.
     */
    public static String getAhoraString(FormatoFecha formato) {
        return FechaUtil.formatFecha(FechaUtil.getAhora(), formato);
    }

    /**
     * @param fecha   {@link Date}
     * @param formato {@link FormatoFecha}
     * @return {@link String}, Devuelve la fecha en el formato especificado.<br/>Si fecha es {@code null} devuelve "".
     */
    public static String formatFecha(Date fecha, FormatoFecha formato) {
        return FechaUtil.formatFecha(fecha, formato.toString());
    }

    /**
     * @param fecha   {@link Date}
     * @param formato {@link String}
     * @return {@link String}, Devuelve la fecha en el formato especificado.<br/>Si fecha es {@code null} devuelve "".
     */
    public static String formatFecha(Date fecha, String formato) {
        String result = "";
        if (fecha != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(formato);
            result = dateFormat.format(fecha);
        }

        return result;
    }

    /**
     * @param fecha {@link Date}
     * @return {@link String}, Devuelve la fecha en el formato dd/MM/yyyy.<br/>Si fecha es {@code null} devuelve "".
     */
    public static String formatFecha(Date fecha) {
        return FechaUtil.formatFecha(fecha, FormatoFecha.DDMMYYYY_WITH_SEPARATOR);
    }

    /**
     * Convierte un {@code String} en {@code Date}, segun el formato especificado
     *
     * @param fecha   {@link String}
     * @param formato {@link String}
     * @return {@link Date}, Si no se puede convertir a {@code Date} devuelve {@code null}
     */
    public static Date parseFecha(String fecha, String formato) {
        Date result = null;
        SimpleDateFormat format = new SimpleDateFormat(formato);

        try {
            if (fecha != null && fecha.length() > 0) {
                result = format.parse(fecha);
            }
        } catch (Exception e) {
            LOG.debug("FechaUtil.parseFecha", e);
        }

        return result;
    }

    /**
     * Convierte un {@code String} en {@code Date}, segun el formato especificado
     *
     * @param fecha   {@link String}
     * @param formato {@link FormatoFecha}
     * @return {@link Date}, Si no se puede convertir a {@code Date} devuelve {@code null}
     */
    public static Date parseFecha(String fecha, FormatoFecha formato) {
        return FechaUtil.parseFecha(fecha, formato.toString());
    }

    /**
     * Convierte un {@code String} en {@code Date}, segun el formato dd/MM/yyyy
     *
     * @param fecha {@link String}
     * @return {@link Date}, Si no se puede convertir a {@code Date} devuelve {@code null}
     */
    public static Date parseFecha(String fecha) {
        return FechaUtil.parseFecha(fecha, FormatoFecha.DDMMYYYY_WITH_SEPARATOR);
    }

    /**
     * Devuelve el anio de una fecha dada
     *
     * @return {@link int}, anio actual
     */
    public static int getAnio() {
        return FechaUtil.getAhoraCalendar().get(Calendar.YEAR);
    }

    /**
     * Devuelve el anio de una fecha dada
     *
     * @return {@link int}, anio actual
     */
    public static String getAnioString() {
        return String.valueOf(FechaUtil.getAnio());
    }

    /**
     * Devuelve el anio de una fecha dada
     *
     * @param fecha {@link Date}
     * @return {@link int}, anio de una fecha dada
     */
    public static int getAnio(Date fecha) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);
        return cal.get(Calendar.YEAR);
    }

    /**
     * Devuelve el anio de una fecha dada en el formato especificado
     *
     * @param fecha {@link Date}
     * @return {@link String}, anio de una fecha dada
     */
    public static String getAnioString(Date fecha) {
        return String.valueOf(FechaUtil.getAnio(fecha));
    }

    /**
     * Devuelve el anio de una fecha dada en el formato especificado
     *
     * @param fecha   {@link String}
     * @param formato {@link FormatoFecha}
     * @return {@link String}, anio de una fecha dada
     */
    public static String getAnioString(String fecha, FormatoFecha formato) {
        return FechaUtil.getAnioString(FechaUtil.parseFecha(fecha, formato));
    }

    /**
     * Devuelve el mes actual
     *
     * @return {@link int}
     */
    public static int getMes() {
        return FechaUtil.getAhoraCalendar().get(Calendar.MONTH) + 1;
    }

    /**
     * Devuelve el mes de una fecha dada
     *
     * @return {@link int}
     */
    public static int getMes(Date fecha) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);
        return cal.get(Calendar.MONTH) + 1;
    }

    /**
     * Devuelve el mes actual
     *
     * @return {@link String}
     */
    public static String getMesString() {
        return StringUtils.leftPad(String.valueOf(FechaUtil.getMes()), 2, '0');
    }

    /**
     * Devuelve el mes actual de una fecha dada
     *
     * @return {@link String}
     */
    public static String getMesString(Date fecha) {
        return StringUtils.leftPad(String.valueOf(FechaUtil.getMes(fecha)), 2, '0');
    }

    /**
     * Devuelve el mes actual de una fecha dada en el formato especificado
     *
     * @return {@link String}
     */
    public static String getMesString(String fecha, FormatoFecha formato) {
        return StringUtils.leftPad(String.valueOf(FechaUtil.getMes(FechaUtil.parseFecha(fecha, formato))), 2, '0');
    }

    /**
     * Devuelve el dia actual
     *
     * @return {@link int}
     */
    public static int getDia() {
        return FechaUtil.getAhoraCalendar().get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Devuelve el dia la fecha recibida
     *
     * @return {@link int}
     */
    public static int getDia(Date fecha) {
    	Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);
        return cal.get(Calendar.DAY_OF_MONTH);
    }
    
    /**
     * Devuelve el dia actual
     *
     * @return {@link String}
     */
    public static String getDiaString() {
        return StringUtils.rightPad(String.valueOf(FechaUtil.getDia()), 2, '0');
    }

    /**
     * Devuelve la semana del mes actual
     *
     * @return {@link int}
     */
    public static int getSemana() {
        return FechaUtil.getAhoraCalendar().get(Calendar.WEEK_OF_MONTH);
    }

    /**
     * Compara dos fechas:
     *
     * @param firstDate {@link Date}
     * @param endDate   {@link Date}
     * @return {@link int}<br/>
     * Si la primera fecha es menor retorna 1 <br/>
     * Si las dos fechas son iguales retorna 0 <br/>
     * Si la segunda fecha es menor retorna -1
     */
    public static int compareDate(Date firstDate, Date endDate) {
        Calendar calendar = new GregorianCalendar();

        calendar.setTime(firstDate);
        Calendar calendarFirst = new GregorianCalendar(
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DATE));

        calendar.setTime(endDate);
        Calendar calendarEnd = new GregorianCalendar(
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DATE));

        if (calendarFirst.getTime().getTime() < calendarEnd.getTime().getTime()) {
            return 1;
        } else if (calendarFirst.getTime().getTime() == calendarEnd.getTime().getTime()) {
            return 0;
        } else {
            return -1;
        }
    }

    /**
     * Suma un tiempo determinado a una fecha dada
     *
     * @param dateOriginal {@link Date}
     * @param type         {@link Integer}
     * @param value        {@link Integer}
     * @return {@link Date}
     */
    public static Date add(Date dateOriginal, Integer type, Integer value) {
        Date date;
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateOriginal);
        cal.add(type, value);

        date = cal.getTime();

        return date;
    }

    /**
     * Obtiene los d�as transcurridos entre dos fechas dadas
     *
     * @param fechaInicial {@link Date}
     * @param fechaFinal   {@link Date}
     * @return {@link long}
     */
    public static long diff(Date fechaInicial, Date fechaFinal) {
        return (fechaFinal.getTime() - fechaInicial.getTime()) / MILLSECS_PER_DAY;
    }

    /**
     * Obtiene el ultimo d�a del mes y anio indicado
     *
     * @param mes  {@link Number}
     * @param anho {@link Number}
     * @return {@link Date}
     */
    public static Date ultimoDiaMes(Number mes, Number anho) {
        Calendar pCal = Calendar.getInstance();
        pCal.set(GregorianCalendar.YEAR, anho.intValue());
        pCal.set(GregorianCalendar.MONTH, mes.intValue());
        pCal.set(GregorianCalendar.DATE, 1);

        return FechaUtil.add(pCal.getTime(), GregorianCalendar.DATE, -1);
    }

    /**
     * Obtiene el ultimo d�a del mes seg�n la fecha indicada
     *
     * @param fecha {@link Date}
     * @return {@link Date}
     */
    public static Date ultimoDiaMes(Date fecha) {
        Calendar pCal = Calendar.getInstance();
        pCal.set(GregorianCalendar.YEAR, FechaUtil.getAnio(fecha));
        pCal.set(GregorianCalendar.MONTH, FechaUtil.getMes(fecha));
        pCal.set(GregorianCalendar.DATE, 1);

        return FechaUtil.add(pCal.getTime(), GregorianCalendar.DATE, -1);
    }

    /**
     * Valida si una cadena tiene el formato de hh:mm am/pm
     *
     * @param time {@link String}, cadena que representa la hora
     * @return {@code true} si es un formato valido, {@code false} si es un formato invalido
     */
    public static boolean validateTime12Hours(String time) {
        Pattern pattern = Pattern.compile(TIME12HOURS_PATTERN);
        Matcher matcher = pattern.matcher(time);
        return matcher.matches();
    }

    /**
     * Valida si una cadena tiene el formato de HH:mm
     *
     * @param time {@link String}, cadena que representa la hora
     * @return {@code true} si es un formato valido, {@code false} si es un formato invalido
     */
    public static boolean validateTime24Hours(String time) {
        Pattern pattern = Pattern.compile(TIME24HOURS_PATTERN);
        Matcher matcher = pattern.matcher(time);
        return matcher.matches();
    }

    /**
     * Valida si una cadena tiene el formato de dd/MM/yyyy
     *
     * @param date {@link String}, cadena que representa la fecha
     * @return {@code true} si es un formato valido, {@code false} si es un formato invalido
     */
    public static boolean validateDate(String date) {
        Pattern pattern = Pattern.compile(DATE_PATTERN);
        Matcher matcher = pattern.matcher(date);
        return matcher.matches();
    }
}
