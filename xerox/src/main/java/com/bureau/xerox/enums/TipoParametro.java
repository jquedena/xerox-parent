package com.bureau.xerox.enums;

public enum TipoParametro {

    TIPOS_PARAMETRO(0L),
    PARAMETRO_PADRE(1L),
    PARAMETRO_HIJO(2L),
    PARAMETRO_OPCION(59L),
    PARAMETRO_ACCION(60L);

    private long id;

    TipoParametro(long id) {
        this.id = id;
    }

    public long toLong() {
        return id;
    }
}
