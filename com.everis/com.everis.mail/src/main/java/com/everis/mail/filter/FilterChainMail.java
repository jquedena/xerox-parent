package com.everis.mail.filter;

import com.everis.mail.Mail;
import com.everis.mail.MailException;
import com.everis.mail.Message;

import java.util.ArrayList;
import java.util.List;

public class FilterChainMail {

    private Mail target;
    private List<FilterMail> filters;

    public FilterChainMail() {
        filters = new ArrayList<FilterMail>();
        target = new Mail();
    }

    public void send(Message message) throws MailException {
        for (FilterMail filter : filters) {
            filter.sendBefore(message);
        }

        target.setMessage(message);
        try {
            target.send();
        } catch (Exception e) {
            for (FilterMail filter : filters) {
                filter.sendError(message, e);
            }
        }

        for (FilterMail filter : filters) {
            filter.sendAfter(message);
        }
    }
}
