package com.shepherd.shep_blog.controllers;

import com.shepherd.shep_blog.data.dto.request.AcceptInviteRequest;
import com.shepherd.shep_blog.data.dto.request.DeclineInviteRequest;
import com.shepherd.shep_blog.data.dto.response.ApiResponse;
import com.shepherd.shep_blog.services.admin.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@Validated
public class AdminController {
    private final AdminService adminService;

    @PostMapping("/invite/accept")
    public ResponseEntity<?> acceptInvite(@RequestBody @Valid AcceptInviteRequest acceptInviteRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse
                .success("Invitation accepted successfully", adminService.acceptInvitation(acceptInviteRequest)));
    }

    @PostMapping("/invite/decline")
    public ResponseEntity<?> declineInvite(@RequestBody @Valid DeclineInviteRequest declineInviteRequest) {
        return ResponseEntity.ok(ApiResponse
                .success("Invitation declined successfully", adminService.declineInvitation(declineInviteRequest)));
    }

}