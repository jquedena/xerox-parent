package com.everis.mail;

import java.io.File;

public class Attachment {

    private String contentType;
    private File file;

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setFile(String file) {
        this.file = new File(file);
    }
}
