package com.everis.enums;

public enum FormatoFecha {

    YYYYMMDD_HH24MMSS("yyyyMMdd-HHmmss"),
    YYYYMMDD_HH24MMSS_WITH_SEPARATOR("yyyy/MM/dd HH:mm:ss"),
    YYYYMMDD("yyyyMMdd"),
    YYYYMMDD_WITH_SEPARATOR("yyyy/MM/dd"),
    DDMMYYYY_HH24MMSS("ddMMyyyy-HHmmss"),
    DDMMYYYY_HH24MMSS_WITH_SEPARATOR("dd/MM/yyyy HH:mm:ss"),
    DDMMYYYY("ddMMyyyy"),
    DDMMYYYY_WITH_SEPARATOR("dd/MM/yyyy"),
    DDMMYYYY_HH24MM_WITH_SEPARATOR("dd/MM/yyyy HH:mm"),
    TIME12_WITH_SEPARATOR("hh:mm"),
    TIME24_WITH_SEPARATOR("HH:mm");

    private final String formato;

    FormatoFecha(String formato) {
        this.formato = formato;
    }

    @Override
    public String toString() {
        return formato;
    }
}
