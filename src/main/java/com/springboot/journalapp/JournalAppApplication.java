package com.springboot.journalapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class JournalAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(JournalAppApplication.class, args);
    }


}
