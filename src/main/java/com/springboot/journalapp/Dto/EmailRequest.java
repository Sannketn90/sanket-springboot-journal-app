package com.springboot.journalapp.Dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class EmailRequest {

    @NotBlank(message = "To address cannot be blank")
    private String to;
    @NotBlank(message = "Subject cannot be blank")
    private String subject;
    @NotBlank(message = "Body cannot be blank")
    private String body;
}
