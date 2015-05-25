package com.everis.mail.handler;

import com.everis.mail.MailException;
import com.everis.mail.Message;

public interface HandlerMail {

    void init();

    void error(MailException exception, Message message);

    void end();
}
