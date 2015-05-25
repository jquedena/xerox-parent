package com.everis.util.xml.filter;

import org.w3c.dom.Element;

import com.everis.util.xml.DOMReaderException;

public interface DOMFilter {

    public static final short NODE = 1;
    public static final short ELEMENT = 2;
    void read(Element element, String path, short nivel) throws DOMReaderException;
}
