package com.shepherd.shep_blog.controllers;

import com.shepherd.shep_blog.data.dto.request.RegisterReaderRequest;
import com.shepherd.shep_blog.data.dto.response.ApiResponse;
import com.shepherd.shep_blog.services.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
