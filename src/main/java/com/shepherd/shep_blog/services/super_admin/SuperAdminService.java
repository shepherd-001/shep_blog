package com.shepherd.shep_blog.services.super_admin;

import com.shepherd.shep_blog.data.dto.request.InviteAdminRequest;
import com.shepherd.shep_blog.data.dto.response.InvitationResponse;

import java.util.List;

public interface SuperAdminService {
    void createSuperAdminIfNotExists();
    List<InvitationResponse> inviteAdmin(InviteAdminRequest inviteAdminRequest);
    InvitationResponse cancelInvitation(String inviteId);
}
