package com.everis.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class MailAuthenticator extends Authenticator {

    private Message message;

    public MailAuthenticator(Message message) {
        super();
        this.message = message;
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(message.getHeader().getUserFrom(), message.getHeader().getPasswordFrom());
    }
}
