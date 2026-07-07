package com.shepherd.shep_blog.controllers;

import com.shepherd.shep_blog.data.dto.request.InviteAdminRequest;
import com.shepherd.shep_blog.common.response.ApiResponse;
import com.shepherd.shep_blog.services.super_admin.SuperAdminService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/super-admin")
@Validated
public class SuperAdminController {
    private final SuperAdminService superAdminService;

    @PostMapping("/invite")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> inviteAdmin(@RequestBody @Valid InviteAdminRequest inviteAdminRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse
                .of("Admins invited successfully", superAdminService.inviteAdmin(inviteAdminRequest)));
    }

    @PutMapping("/invite/cancel/{inviteId}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> cancelAdminInvite(@PathVariable
            @NotBlank(message = "Invitation ID is required")
            String inviteId) {
        return ResponseEntity.ok(ApiResponse.of("Invitation cancelled successfully",
                                               UUID inviteId) {
        return ResponseEntity.ok(ApiResponse.success("Invitation cancelled successfully",
                        superAdminService.cancelInvitation(inviteId)));
    }
}