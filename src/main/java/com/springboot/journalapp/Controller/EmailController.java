package com.springboot.journalapp.Controller;

import com.springboot.journalapp.Dto.EmailRequest;
import com.springboot.journalapp.Service.EmailService;
import com.springboot.journalapp.api.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<ApiResponse<String>> sendEmail(@RequestBody EmailRequest emailRequest) {

        emailService.sendSimpleEmail(emailRequest.getTo(), emailRequest.getSubject(), emailRequest.getBody());
        return ResponseEntity.ok(ApiResponse.success("Email sent successfully", "SUCCESS"));


    }
}
