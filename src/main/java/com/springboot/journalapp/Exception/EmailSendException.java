package com.springboot.journalapp.Exception;

public class EmailSendException extends RuntimeException {
    public EmailSendException(String message) {
        super(message);
    }
}
