package com.springboot.journalapp.Exception;

public class GoogleAuthException extends Throwable {
    public GoogleAuthException(String idTokenMissingInTokenResponse) {
        super(idTokenMissingInTokenResponse);
    }
}
