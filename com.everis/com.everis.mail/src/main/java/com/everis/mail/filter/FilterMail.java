package com.everis.mail.filter;

import com.everis.mail.MailException;
import com.everis.mail.Message;

public interface FilterMail {

    void sendBefore(Message message) throws MailException;

    void sendAfter(Message message) throws MailException;

    void sendError(Message message, Exception e) throws MailException;
}
