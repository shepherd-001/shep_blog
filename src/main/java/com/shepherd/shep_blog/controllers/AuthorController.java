package com.shepherd.shep_blog.controllers;

import com.shepherd.shep_blog.data.dto.request.RegisterAuthorRequest;
import com.shepherd.shep_blog.data.dto.response.ApiResponse;
import com.shepherd.shep_blog.services.author.AuthorService;
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
@RequestMapping("/api/v1/author")
public class AuthorController {
//    private final AuthorService authorService;

}