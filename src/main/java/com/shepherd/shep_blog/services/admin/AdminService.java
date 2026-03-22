package com.shepherd.shep_blog.services.admin;

import com.shepherd.shep_blog.data.dto.request.AcceptInviteRequest;
import com.shepherd.shep_blog.data.dto.request.DeclineInviteRequest;
import com.shepherd.shep_blog.data.dto.request.PaginationRequest;
import com.shepherd.shep_blog.data.dto.response.AdminResponse;
import com.shepherd.shep_blog.data.dto.response.InvitationResponse;
import com.shepherd.shep_blog.data.dto.response.PaginationResponse;

public interface AdminService {
    InvitationResponse declineInvitation(DeclineInviteRequest declineInviteRequest);
    InvitationResponse acceptInvitation(AcceptInviteRequest acceptInviteRequest);
    PaginationResponse<AdminResponse> getAllActiveAdmins(PaginationRequest paginationRequest);
}
