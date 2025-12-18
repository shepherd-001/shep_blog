package com.shepherd.shep_blog.controllers;

import com.shepherd.shep_blog.data.dto.request.PaginationRequest;
import com.shepherd.shep_blog.data.dto.request.RegisterAuthorRequest;
import com.shepherd.shep_blog.data.dto.response.ApiResponse;
import com.shepherd.shep_blog.services.author.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/author")
public class AuthorController {
    private final AuthorService authorService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<?>> registerAuthor(@Valid @RequestBody RegisterAuthorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success("User registered successfully", authorService.registerAuthor(request)));
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'READER')")
    public ResponseEntity<ApiResponse<?>> getAllAuthors(@RequestBody PaginationRequest paginationRequest){
        return ResponseEntity.ok(ApiResponse
                .success(authorService.getAllAuthor(paginationRequest)));
    }
}