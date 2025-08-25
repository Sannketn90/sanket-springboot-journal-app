package com.springboot.journalapp.Mapper;

import com.springboot.journalapp.Dto.SignupRequest;
import com.springboot.journalapp.Dto.SignupResponseDTO;
import com.springboot.journalapp.Dto.UserDto;
import com.springboot.journalapp.Entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    SignupRequest toDto(User user);

    User toEntity(SignupRequest request);

    @Mapping(target = "id", source = "id")
    SignupResponseDTO toResponseDto(User user);

    UserDto touser(User user);


}
