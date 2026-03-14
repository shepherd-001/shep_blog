package com.shepherd.shep_blog.controllers;

import com.shepherd.shep_blog.data.dto.request.CreateTeamMemberRequest;
import com.shepherd.shep_blog.data.dto.request.InviteTeamMemberRequest;
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

import java.util.UUID;

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
    public ResponseEntity<ApiResponse<?>> getAllAuthors(@RequestParam(required = false, defaultValue = "1") int page,
                                                        @RequestParam(required = false, defaultValue = "10") int size,
                                                        @RequestParam(required = false) String sortBy,
                                                        @RequestParam(required = false) String sortDirection){
        PaginationRequest paginationRequest = new PaginationRequest(page, size, sortBy, sortDirection);
        return ResponseEntity.ok(ApiResponse
                .success(authorService.getAllAuthor(paginationRequest)));
    }

    @PostMapping("/team-member/invite")
    @PreAuthorize("hasRole('SUPER_AUTHOR')")
    public ResponseEntity<ApiResponse<?>> createTeamMember(@Valid @RequestBody InviteTeamMemberRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse
                .success("Team member invited successfully", authorService.inviteTeamMember(request)));
    }

    @PostMapping("/team-member/create")
    @PreAuthorize("hasRole('SUPER_AUTHOR')")
    public ResponseEntity<ApiResponse<?>> createTeamMember(@Valid @RequestBody CreateTeamMemberRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse
                .success("Team member created successfully", authorService.createTeamMember(request)));
    }

    @PutMapping("/team-member/activate/{authorId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<?>> activateTeamMember(@PathVariable UUID authorId) {
        return ResponseEntity.ok(ApiResponse.success("Team member activated successfully",
                authorService.activateTeamMember(authorId)));
    }
}