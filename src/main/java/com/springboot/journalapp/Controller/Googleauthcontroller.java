package com.springboot.journalapp.Controller;

import com.springboot.journalapp.Exception.GoogleAuthException;
import com.springboot.journalapp.Service.GoogleAuthService;
import com.springboot.journalapp.api.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/google")
@RequiredArgsConstructor
public class Googleauthcontroller {

    private final GoogleAuthService googleAuthService;

    @Operation(summary = "Google OAuth2 Callback", description = "Handle Google OAuth2 callback and return JWT token")
    @GetMapping("/callback")
    public ResponseEntity<ApiResponse<String>> googleCallback(@RequestParam String code) throws GoogleAuthException {
        String token = googleAuthService.handleGoogleCallback(code);
        return ResponseEntity.ok(ApiResponse.success(token, "Google authentication successful"));
    }


}
