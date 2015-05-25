package com.everis.mail;

public enum MailExceptionCodes {

    INVALID_ADDRESS("MAIL:001", "Direccion invalida"),
    INVALID_PARAMETER("MAIL:002", "Parametro invalido"),
    NOT_SEND("MAIL:003", "No se pudo enviar el correo");

    private String code;
    private String description;

    MailExceptionCodes(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
