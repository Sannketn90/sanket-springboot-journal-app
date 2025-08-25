package com.springboot.journalapp.Service;

import com.springboot.journalapp.Dto.SignupRequest;
import com.springboot.journalapp.Dto.UserDto;
import com.springboot.journalapp.Entity.User;
import com.springboot.journalapp.Exception.DuplicateResourceException;
import com.springboot.journalapp.Mapper.UserMapper;
import com.springboot.journalapp.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;


    public SignupRequest createUser(SignupRequest userDto) {
        log.info("Creating new user: {}", userDto.getUsername());

        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new DuplicateResourceException("Username already exists");
        }
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }

        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userDto.setRoles("ADMIN");

        User savedUser = userRepository.save(userMapper.toEntity(userDto));
        log.info("User created successfully: {}", savedUser.getUsername());
        return userMapper.toDto(savedUser);
    }

    public List<UserDto> getAllUsers() {
        log.info("Fetching all users from the database");

        List<User> users = userRepository.findAll();
        log.debug("Number of users found: {}", users.size());
        List<UserDto> userDtos = new ArrayList<>();

        for (User user : users) {
            userDtos.add(userMapper.touser(user));
        }
        log.info("Returning {} users", userDtos.size());
        return userDtos;
    }

}
