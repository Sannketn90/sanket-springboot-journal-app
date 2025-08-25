package com.springboot.journalapp.Exception;

public class JournalAccessDeniedException extends RuntimeException {
    public JournalAccessDeniedException(String message) {
        super(message);
    }
}
