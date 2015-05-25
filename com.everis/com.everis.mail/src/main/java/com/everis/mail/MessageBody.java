package com.everis.mail;

import java.io.File;
import java.io.Serializable;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

public class MessageBody implements Serializable {

    private static final long serialVersionUID = 1L;
    private static Logger logger = Logger.getLogger(MessageBody.class);
    private String message;
    private MimeMultipart mimeMultipart;

    public MessageBody() {
        mimeMultipart = new MimeMultipart();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
        BodyPart html = new MimeBodyPart();
        try {
            html.setContent(message, "text/html");
            if (mimeMultipart != null) {
                mimeMultipart = new MimeMultipart();
                mimeMultipart.addBodyPart(html);
            }
        } catch (MessagingException e) {
            logger.error("Message:setMessage", e);
        }

    }

    public void setMessageAttachImage(String message, String prefixImage, File... images) {
        this.message = message;
        BodyPart html = new MimeBodyPart();
        BodyPart image = new MimeBodyPart();
        try {
            html.setContent(message, "text/html");

            if (mimeMultipart != null) {
                mimeMultipart = new MimeMultipart();
                mimeMultipart.addBodyPart(html);
                for (int i = 0; i < images.length; i++) {
                    image = new MimeBodyPart();
                    image.setDataHandler(new DataHandler(new FileDataSource(images[i])));
                    image.setHeader("Content-ID", prefixImage + i);
                    mimeMultipart.addBodyPart(image);
                }
            }
        } catch (MessagingException e) {
            logger.error("Message:setMessage", e);
        }

    }

    public MimeMultipart getMimeMultipart() {
        return mimeMultipart;
    }

    public void addFileAttachment(Attachment attachment) {
        BodyPart body = new MimeBodyPart();
        try {
            body.setDataHandler(new DataHandler(new FileDataSource(attachment.getFile())));
            body.setFileName(attachment.getFile().getName());
            if (mimeMultipart != null) {
                mimeMultipart.addBodyPart(body);
            }
        } catch (MessagingException e) {
            logger.error("Message:addFileAttachment", e);
        }
    }
}
