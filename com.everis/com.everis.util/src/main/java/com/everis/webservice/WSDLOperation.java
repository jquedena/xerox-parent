package com.everis.webservice;

public class WSDLOperation {

    private String name;
    private WSDLMessage input;
    private WSDLMessage output;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public WSDLMessage getInput() {
        return input;
    }

    public void setInput(WSDLMessage input) {
        this.input = input;
    }

    public WSDLMessage getOutput() {
        return output;
    }

    public void setOutput(WSDLMessage output) {
        this.output = output;
    }

}
