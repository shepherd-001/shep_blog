package com.shepherd.shep_blog.controllers;

import com.shepherd.shep_blog.data.dto.request.InviteAdminRequest;
import com.shepherd.shep_blog.data.dto.response.ApiResponse;
import com.shepherd.shep_blog.data.model.Admin;
import com.shepherd.shep_blog.services.admin.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {
    private final AdminService adminService;

    @PostMapping("/invite")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity inviteAdmin(@RequestBody @Valid InviteAdminRequest inviteAdminRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse
                .success(adminService.inviteAdmin(inviteAdminRequest)));
    }
}