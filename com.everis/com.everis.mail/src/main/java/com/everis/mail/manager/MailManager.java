package com.everis.mail.manager;

import com.everis.mail.Message;
import com.everis.mail.filter.FilterChainMail;
import com.everis.mail.handler.HandlerMail;
import com.everis.mail.thread.MailThread;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MailManager {

    private List<Message> messages;
    private HandlerMail handler;
    private FilterChainMail filterChainMail;

    public MailManager() {
        this.filterChainMail = null;
        this.handler = null;
        this.messages = new ArrayList<Message>();
    }

    public MailManager(FilterChainMail filterChainMail) {
        this.filterChainMail = filterChainMail;
        this.handler = null;
        this.messages = new ArrayList<Message>();
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public HandlerMail getHandler() {
        return handler;
    }

    public void setHandler(HandlerMail handler) {
        this.handler = handler;
    }

    public void setFilterChainMail(FilterChainMail filterChainMail) {
        this.filterChainMail = filterChainMail;
    }

    public FilterChainMail getFilterChainMail() {
        return filterChainMail;
    }

    public boolean add(Message e) {
        return messages.add(e);
    }

    public boolean addAll(Collection<? extends Message> c) {
        return messages.addAll(c);
    }

    public void clear() {
        messages.clear();
    }

    public void send() {
        MailThread mailThread = new MailThread(this);
        mailThread.start();
    }
}
