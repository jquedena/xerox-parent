package com.everis.mail.thread;

import org.apache.log4j.Logger;

import com.everis.mail.MailException;
import com.everis.mail.Message;
import com.everis.mail.manager.MailManager;

public class MailThread extends Thread {

    private static Logger logger = Logger.getLogger(MailThread.class);
    private MailManager manager;

    public MailThread(MailManager manager) {
        super();
        this.manager = manager;
    }

    @Override
    public void run() {

        if (manager.getHandler() != null) {
            manager.getHandler().init();
        }

        Message ms = null;
        try {
            for (Message m : manager.getMessages()) {
                ms = m;
                manager.getFilterChainMail().send(m);
            }
        } catch (MailException e) {
            logger.error("No se pudo enviar", e);
            if (manager.getHandler() != null) {
                manager.getHandler().error(e, ms);
            }
        }

        if (manager.getHandler() != null) {
            manager.getHandler().end();
        }
    }
}
