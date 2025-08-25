package com.springboot.journalapp.Service;

import com.springboot.journalapp.Dto.LoginRequest;
import com.springboot.journalapp.Dto.LoginResponseDTO;
import com.springboot.journalapp.Dto.SignupRequest;
import com.springboot.journalapp.Dto.SignupResponseDTO;
import com.springboot.journalapp.Entity.User;
import com.springboot.journalapp.Exception.DuplicateResourceException;
import com.springboot.journalapp.Exception.ResourceNotFoundException;
import com.springboot.journalapp.Mapper.UserMapper;
import com.springboot.journalapp.Repository.UserRepository;
import com.springboot.journalapp.Utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsImpl userDetailsImpl;
    private final JwtUtils jwtUtils;
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;


    public SignupResponseDTO saveNewUser(SignupRequest userDto) {
        log.info("Saving new user: {}", userDto.getUsername());

        if (repository.findByusername(userDto.getUsername()) != null) {
            throw new DuplicateResourceException("Username already exists");
        }
        if (repository.existsByEmail(userDto.getEmail())
        ) {
            throw new DuplicateResourceException("Email already exists");
        }

        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userDto.setRoles("USER");


        User savedUser = repository.save(userMapper.toEntity(userDto));
        log.info("User created successfully: {}", savedUser.getUsername());
        return userMapper.toResponseDto(savedUser);
    }

    public LoginResponseDTO loginUser(LoginRequest loginRequest) {
        log.info("Login attempt for username: {}", loginRequest.getUsername());

        if (loginRequest.getUsername() == null || loginRequest.getPassword() == null) {
            log.error("Username or password is null");
            throw new BadCredentialsException("Username and password must not be null");
        }
        if (loginRequest.getUsername().isBlank() || loginRequest.getPassword().isBlank()) {
            log.error("Username or password is blank");
            throw new BadCredentialsException("Username and password must not be blank");
        }

        UserDetails userDetails = userDetailsImpl.loadUserByUsername(loginRequest.getUsername());
        log.debug("Loaded user details for username: {}", loginRequest.getUsername());

        if (userDetails == null) {
            log.error("User not found: {}", loginRequest.getUsername());
            throw new UsernameNotFoundException("User not found: " + loginRequest.getUsername());
        }

        try {

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );


            String token = jwtUtils.generateToken(userDetails.getUsername());

            log.info("Login successful for username: {}", loginRequest.getUsername());

            if (token == null) {
                log.error("Token generation failed for username: {}", loginRequest.getUsername());
                throw new BadCredentialsException("Token generation failed");
            }

            return new LoginResponseDTO(token, 3600L, loginRequest.getUsername());

        } catch (AuthenticationException e) {
            log.error("Login failed for username: {}", loginRequest.getUsername());
            throw new BadCredentialsException("Invalid username or password: " + e.getMessage());
        }
    }


    public SignupRequest updateUser(SignupRequest userDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        log.info("Updating user details for: {}", username);

        User existingUser = repository.findByusername(username);
        if (existingUser == null) {
            throw new ResourceNotFoundException("User not found for update: " + username);
        }

        existingUser.setUsername(userDto.getUsername());
        existingUser.setEmail(userDto.getEmail());

        if (userDto.getPassword() != null &&
                !userDto.getPassword().isBlank() &&
                !passwordEncoder.matches(userDto.getPassword(), existingUser.getPassword())) {
            existingUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }
        User updateUser = repository.save(existingUser);
        log.info("User details updated successfully for: {}", updateUser.getUsername());
        return userMapper.toDto(updateUser);
    }

    public Boolean deleteUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        log.info("Deleting user: {}", username);

        User existingUser = repository.findByusername(username);
        if (existingUser == null) {
            throw new ResourceNotFoundException("User not found for deletion: " + username);
        }

        repository.delete(existingUser);
        log.info("User deleted successfully: {}", username);
        return true;
    }

    public User findByusername(String username) {
        log.debug("Finding user by username: {}", username);
        return repository.findByusername(username);
    }

}

