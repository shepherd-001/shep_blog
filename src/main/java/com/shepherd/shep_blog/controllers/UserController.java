package com.shepherd.shep_blog.controllers;

import com.shepherd.shep_blog.data.dto.request.PaginationRequest;
import com.shepherd.shep_blog.data.dto.request.RegisterReaderRequest;
import com.shepherd.shep_blog.data.dto.response.ApiResponse;
import com.shepherd.shep_blog.services.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/signup/reader")
    public ResponseEntity<ApiResponse<?>> registerReader(@Valid @RequestBody RegisterReaderRequest registerReaderRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success("User registered successfully", userService.registerReader(registerReaderRequest)));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<?>> getAllEnabledUsers(@RequestParam boolean enabled,
                                                             @RequestBody PaginationRequest paginationRequest) {
        return ResponseEntity.ok(ApiResponse
                .success(userService.getAllEnabledUser(enabled, paginationRequest)));
    }
}
