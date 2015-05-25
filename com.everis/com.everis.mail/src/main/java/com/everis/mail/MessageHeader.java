package com.everis.mail;

import java.io.Serializable;

public class MessageHeader implements Serializable {

    private static final long serialVersionUID = 1L;
    private String host;
    private String port;
    private String emailFrom = null;
    private String passwordFrom = null;
    private String userFrom = null;
    private String subject;
    private String listTO;
    private String listCC;
    private String listCO;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getEmailFrom() {
        return emailFrom;
    }

    public void setEmailFrom(String emailFrom) {
        this.emailFrom = emailFrom;
    }

    public String getPasswordFrom() {
        return passwordFrom;
    }

    public void setPasswordFrom(String passwordFrom) {
        this.passwordFrom = passwordFrom;
    }

    public String getUserFrom() {
        return userFrom;
    }

    public void setUserFrom(String userFrom) {
        this.userFrom = userFrom;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getListTO() {
        return listTO;
    }

    public void setListTO(String listTO) {
        this.listTO = listTO;
    }

    public String getListCC() {
        return listCC;
    }

    public void setListCC(String listCC) {
        this.listCC = listCC;
    }

    public String getListCO() {
        return listCO;
    }

    public void setListCO(String listCO) {
        this.listCO = listCO;
    }

}
