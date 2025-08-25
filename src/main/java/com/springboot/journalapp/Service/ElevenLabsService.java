package com.springboot.journalapp.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ElevenLabsService {

    @Value("${elevenlabs.api.key}")
    private String apiKey;

    private static final String API_URL = "https://api.elevenlabs.io/v1/text-to-speech/:voice_id";
    private static final String VOICE_ID = "EXAVITQu4vr4xnSDxMaL";

    private final RestTemplate restTemplate;

    public String textToSpeech(String text) {
        String baseUrl = API_URL.replace(":voice_id", VOICE_ID);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("xi-api-key", apiKey);

        Map<String, Object> voiceSettings = new HashMap<>();
        voiceSettings.put("stability", 0.5);
        voiceSettings.put("similarity_boost", 0.75);

        Map<String, Object> requestPayload = new HashMap<>();
        requestPayload.put("text", text);
        requestPayload.put("voice_settings", voiceSettings);

        try {
            ObjectMapper mapper = new ObjectMapper();
            String requestBody = mapper.writeValueAsString(requestPayload);

            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<byte[]> response = restTemplate.exchange(baseUrl, HttpMethod.POST, entity, byte[].class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                String outputFilePath = "output_" + UUID.randomUUID() + ".mp3";
                try (FileOutputStream fos = new FileOutputStream(outputFilePath)) {
                    fos.write(response.getBody());
                    log.info("Audio content written to file: {}", outputFilePath);
                    return outputFilePath;
                }
            } else {
                log.error("Failed to get audio. Status: {}, Response: {}", response.getStatusCode(), response);
            }
        } catch (Exception e) {
            log.error("Error during text-to-speech conversion", e);
        }

        return null;
    }
}