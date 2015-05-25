package com.bureau.xerox.enums;

public enum Configuracion {

    PB_TIPOS_PARAMETRO(0L),
    PB_TIPO_PADRE(1L),
    PB_TIPO_HIJO(2L),
    PB_HOST_MAIL(8L),
    PB_PORT_MAIL(9L),
    PB_USER_MAIL(10L),
    PB_LIST_SEND(11L),
    PB_PASSWD_MAIL(-1L),
    PB_ROOT_CATEGORY(5L),
    PB_FILE(6L),
    PB_GIN_BANCO (18L),
    PB_DIRECTORIO_SALIDA(23L),
    PB_DIRECTORIO_ENTRADA(22L),
    PB_DIRECTORIO_PROCESADO(499L),
    PB_DIRECTORIO_ERRORES(252L),
    PB_DIRECTORIO_XML(254L),
    PB_ARCHIVO_ACCIONISTA(25L),
    PB_MARCAS_ACCIONISTA(255L),
    PB_ARCHIVO_METADATA(26L),
    PB_CARGAR_CUENTAS(497L),
    PB_GENERAR_ARCHIVO(493L),
    PB_PERSONA_NATURAL(248L),
    PB_ENDPOINT_LDAP(250L),
    PB_FLAG_TEST(251L),
    PB_TIPO_ACCOUNT_HOLDER(229L),
    PB_FATCA_XSD(500L),
    PB_CERTIFICATE_IRS(494L),
    PB_CERTIFICATE_BBVA(495L),
    PB_CERTIFICATE_BBVA_PRIVADA(551L),
    PB_ARCHIVO_RESPUESTA(549L),
    PB_GIN_IRS(550L);


    private Long key;

    Configuracion(Long key) {
        this.key = key;
    }

    public Long getKey() {
        return key;
    }

    public void setKey(Long key) {
        this.key = key;
    }
}
