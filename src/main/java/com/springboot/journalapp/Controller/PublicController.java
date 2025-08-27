package com.springboot.journalapp.Controller;

import com.springboot.journalapp.Dto.LoginRequest;
import com.springboot.journalapp.Dto.LoginResponseDTO;
import com.springboot.journalapp.Dto.SignupRequest;
import com.springboot.journalapp.Dto.SignupResponseDTO;
import com.springboot.journalapp.Service.UserService;
import com.springboot.journalapp.api.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Public APIs", description = "Sign-up and Login")
public class PublicController {


    private final UserService service;


    @Operation(summary = "Sign up a new user", description = "Allows users to register a new account")
    @PostMapping("/user/signup")
    public ResponseEntity<ApiResponse<SignupResponseDTO>> signUp(@Valid @RequestBody SignupRequest userDto) {
        return ResponseEntity.ok(ApiResponse.success(service.saveNewUser(userDto), "User registered successfully"));
    }

    @Operation(summary = "Login user", description = "Allows users to log in to their account")
    @PostMapping("/user/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(ApiResponse.success(service.loginUser(loginRequest), "User logged in successfully"));
    }
}