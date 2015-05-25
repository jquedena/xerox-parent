package com.everis.webservice;

import java.util.ArrayList;
import java.util.List;

public class WSDLElement {

    private String name;
    private String type;
    private String value;
    private WSDLElement ref;
    private List<WSDLElement> attributes; 

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public WSDLElement getRef() {
        return ref;
    }

    public void setRef(WSDLElement ref) {
        this.ref = ref;
    }

    public List<WSDLElement> getAttributes() {
        if(attributes == null) {
            attributes = new ArrayList<WSDLElement>();
        }
        return attributes;
    }

    public void setAttributes(List<WSDLElement> attributes) {
        this.attributes = attributes;
    }

    public WSDLElement getAttribute(String name) {
        WSDLElement attribute = null;
        for(WSDLElement find : getAttributes()) {
            if(name.equalsIgnoreCase(find.getName())) {
                attribute = find;
                break;
            }
        }
        
        return attribute;
    }
    
    public void setAttribute(String name, String value) {
        WSDLElement attribute = getAttribute(name);
        if(attribute != null) {
            attribute.setValue(value);
        }
    }
}
