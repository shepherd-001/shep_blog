package com.shepherd.shep_blog.services.superAdmin;

import com.shepherd.shep_blog.data.dto.request.InviteAdminRequest;
import com.shepherd.shep_blog.data.dto.response.InvitationResponse;

public interface SuperAdminService {
    void createSuperAdminIfNotExists();
    String inviteAdmin(InviteAdminRequest inviteAdminRequest);
    InvitationResponse cancelInvitation(String inviteId);
    // accept invitation
    // cancel invitation
}
