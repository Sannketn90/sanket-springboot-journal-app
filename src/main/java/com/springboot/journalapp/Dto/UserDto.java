package com.springboot.journalapp.Dto;

import com.springboot.journalapp.Entity.Journal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {


    private Long id;


    private String username;


    private String email;


    private String password;


    private List<Journal> journalList = new ArrayList<>();


    private String roles;
}
