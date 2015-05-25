package com.everis.web.enums;

public enum MappingBasic {

    ERROR("error"),
    LOGIN("login"),
    LOGOUT("logout"),
    INDEX("index");

    private String url;

    MappingBasic(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return this.url;
    }
}