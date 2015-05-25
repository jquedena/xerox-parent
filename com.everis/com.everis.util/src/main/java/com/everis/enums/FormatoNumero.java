package com.everis.enums;

public enum FormatoNumero {

    ES_PE_2DEC("#0.00"),
    ES_PE_3DEC("#0.000"),
    ES_PE_4DEC("#0.0000"),
    PORCENTAJE("###.##%");

    private final String formato;

    FormatoNumero(String formato) {
        this.formato = formato;
    }

    @Override
    public String toString() {
        return formato;
    }
}
