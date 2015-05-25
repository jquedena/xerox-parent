package com.everis.mail;

import com.everis.constant.CaracterEspecial;

public class MailException extends Exception {

    private static final long serialVersionUID = 1L;
    private MailExceptionCodes error;
    private Message message;

    public MailException(Throwable cause, MailExceptionCodes error, Message message) {
        super(cause);
        this.error = error;
        this.message = message;
    }

    @Override
    public String getMessage() {
        StringBuilder sb = new StringBuilder();

        sb.append("Codigo de Error:");
        sb.append(error.getCode());
        sb.append(CaracterEspecial.NEW_LINE);
        sb.append("Descripcion:");
        sb.append(error.getDescription());
        sb.append(CaracterEspecial.NEW_LINE);

        sb.append("Parametros:");
        if (message != null) {
            sb.append(CaracterEspecial.NEW_LINE);

            sb.append(CaracterEspecial.TAB);
            sb.append("To: ");
            sb.append(message.getHeader().getListTO());
            sb.append(CaracterEspecial.NEW_LINE);

            sb.append(CaracterEspecial.TAB);
            sb.append("CC: ");
            sb.append(message.getHeader().getListCC());
            sb.append(CaracterEspecial.NEW_LINE);

            sb.append(CaracterEspecial.TAB);
            sb.append("CO: ");
            sb.append(message.getHeader().getListCO());
            sb.append(CaracterEspecial.NEW_LINE);

            sb.append(CaracterEspecial.TAB);
            sb.append("Host: ");
            sb.append(message.getHeader().getHost());
            sb.append(CaracterEspecial.NEW_LINE);

            sb.append(CaracterEspecial.TAB);
            sb.append("Port: ");
            sb.append(message.getHeader().getPort());
        } else {
            sb.append("No se ha configurado el mensaje");
        }

        return sb.toString();
    }
}
