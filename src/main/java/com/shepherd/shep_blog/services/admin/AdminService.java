package com.shepherd.shep_blog.services.admin;

import com.shepherd.shep_blog.data.dto.request.InviteAdminRequest;

public interface AdminService {
    void createSuperAdminIfNotExists();
    String inviteAdmin(InviteAdminRequest inviteAdminRequest);
}
