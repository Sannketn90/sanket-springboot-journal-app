package com.springboot.journalapp.Controller;

import com.springboot.journalapp.Dto.SignupRequest;
import com.springboot.journalapp.Dto.UserDto;
import com.springboot.journalapp.Service.AdminService;
import com.springboot.journalapp.api.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "Admin APIs", description = "Create and Read all User by Admin")
public class AdminController {


    private final AdminService adminService;

    @Operation(summary = "Create a new user", description = "Admin can create a new user")
    @PostMapping("/create-user")
    public ResponseEntity<ApiResponse<SignupRequest>> createAdmin(@Valid @RequestBody SignupRequest userDto) {
        return ResponseEntity.ok(ApiResponse.success(adminService.createUser(userDto), "User created successfully"));
    }

    @Operation(summary = "Get all users", description = "Admin can fetch all users")
    @GetMapping("/all-users")
    public ResponseEntity<ApiResponse<List<UserDto>>> getAllUsers() {
        return ResponseEntity.ok(ApiResponse.success(adminService.getAllUsers(), "Users fetched successfully"));
    }
}
