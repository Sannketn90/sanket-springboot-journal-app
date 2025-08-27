package com.springboot.journalapp.Controller;

import com.springboot.journalapp.Dto.SignupRequest;
import com.springboot.journalapp.Service.UserService;
import com.springboot.journalapp.api.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/user")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "User APIs", description = "Update-Delete and Get Weather")
public class UserController {


    private final UserService service;

    @Operation(summary = "Get current user details", description = "Fetches the details of the currently logged-in user")
    @PutMapping("/update")
    public ResponseEntity<ApiResponse<SignupRequest>> updateUser(@Valid @RequestBody SignupRequest userDto) {
        SignupRequest updatedUser = service.updateUser(userDto);
        return ResponseEntity.ok(ApiResponse.success(updatedUser, "User updated successfully"));
    }

    @Operation(summary = "Delete current user", description = "Deletes the currently logged-in user account")
    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<String>> deleteUser() {
        boolean isDeleted = service.deleteUser();
        if (isDeleted) {

            SecurityContextHolder.clearContext();
            return ResponseEntity.ok(ApiResponse.success("User deleted successfully. Please log out and log in again for changes to take effect.", null));
        }
        return ResponseEntity.ok(ApiResponse.success(null, "Failed to delete user"));
    }
}