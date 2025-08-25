package com.springboot.journalapp.Exception;

public class ExternalApiException extends RuntimeException {
    public ExternalApiException(String message) {
        super(message);
    }
}
