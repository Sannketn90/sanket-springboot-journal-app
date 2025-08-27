package com.springboot.journalapp.Controller;

import com.springboot.journalapp.Service.ElevenLabsService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/text-to-speech")
@RequiredArgsConstructor
public class TextToSpeechController {

    private final ElevenLabsService elevenLabsService;

    @Operation(summary = "Convert Text To Speech")
    @PostMapping
    public ResponseEntity<String> convertTextToSpeech(@RequestParam String text) {
        String filePath = elevenLabsService.textToSpeech(text);
        if (filePath != null) {
            return ResponseEntity.ok("Audio file saved at: " + filePath);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to generate audio");
        }

    }


}



