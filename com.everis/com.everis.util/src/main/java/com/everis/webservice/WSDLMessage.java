package com.everis.webservice;

public class WSDLMessage {

    private String name;
    private String part;
    private WSDLElement element;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public WSDLElement getElement() {
        return element;
    }

    public void setElement(WSDLElement element) {
        this.element = element;
    }
}
