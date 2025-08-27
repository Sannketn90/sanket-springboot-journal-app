package com.springboot.journalapp.Service;

import com.springboot.journalapp.Entity.User;
import com.springboot.journalapp.Exception.GoogleAuthException;
import com.springboot.journalapp.Repository.UserRepository;
import com.springboot.journalapp.Utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleAuthService {

    @Value("${security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Value("${security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;

    private final RestTemplate restTemplate;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtil;

    public String handleGoogleCallback(String code) throws GoogleAuthException {
        log.info("Handling Google OAuth2 callback with code: {}", code);

        try {
            //  Exchange code for tokens
            String tokenEndpoint = "https://oauth2.googleapis.com/token";

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("code", code);
            params.add("client_id", clientId);
            params.add("client_secret", clientSecret);
            params.add("redirect_uri", redirectUri);
            params.add("grant_type", "authorization_code");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

            ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(tokenEndpoint, request, Map.class);

            if (tokenResponse.getStatusCode() != HttpStatus.OK || tokenResponse.getBody() == null) {
                throw new GoogleAuthException("Failed to get token from Google");
            }

            String idToken = (String) tokenResponse.getBody().get("id_token");
            if (idToken == null || idToken.isBlank()) {
                throw new GoogleAuthException("id_token missing in token response");
            }
            log.debug("Received ID token from Google");

            //  Fetch user info
            String userInfoUrl = "https://oauth2.googleapis.com/tokeninfo?id_token=" + idToken;
            ResponseEntity<Map> userInfoResponse = restTemplate.getForEntity(userInfoUrl, Map.class);

            if (userInfoResponse.getStatusCode() != HttpStatus.OK || userInfoResponse.getBody() == null) {
                throw new GoogleAuthException("Failed to fetch user info from Google");
            }

            Map<String, Object> userInfo = userInfoResponse.getBody();
            String email = (String) userInfo.get("email");
            if (email == null || email.isBlank()) {
                throw new GoogleAuthException("Google account does not have an email address");
            }
            log.info("Google user email: {}", email);

            // 3. Create user if not exists
            User user = userRepository.findByEmail(email).orElseGet(() -> {
                User newUser = new User();
                newUser.setEmail(email);
                newUser.setUsername(email);
                newUser.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
                userRepository.save(newUser);
                log.info("Created new user for email: {}", email);
                return newUser;
            });

            // 4. Generate JWT token
            String jwtToken = jwtUtil.generateToken(email);
            log.info("JWT token generated for user {}", email);
            return jwtToken;

        } catch (RestClientException e) {
            log.error("Error during Google OAuth2 process", e);
            throw new GoogleAuthException("Error during Google OAuth2 process: " + e.getMessage());
        }
    }
}