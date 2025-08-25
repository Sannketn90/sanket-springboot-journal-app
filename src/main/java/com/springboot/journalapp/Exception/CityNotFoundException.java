package com.springboot.journalapp.Exception;

public class CityNotFoundException extends RuntimeException {
    public CityNotFoundException(String s) {
        super(s);
    }
}

