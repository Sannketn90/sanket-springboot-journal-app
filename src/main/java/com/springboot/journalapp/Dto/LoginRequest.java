package com.springboot.journalapp.Dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class LoginRequest {

    @NotBlank(message = "Username is required")
    private String username;
    @NotBlank(message = "Password is required")
    private String password;
}
