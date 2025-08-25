package com.springboot.journalapp.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupResponseDTO {

    private Long id;
    private String username;
}
