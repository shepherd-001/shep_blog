package com.shepherd.shep_blog.services.admin;

import com.shepherd.shep_blog.data.dto.request.AcceptInviteRequest;
import com.shepherd.shep_blog.data.dto.request.DeclineInviteRequest;
import com.shepherd.shep_blog.data.dto.response.InvitationResponse;

public interface AdminService {
    InvitationResponse declineInvitation(DeclineInviteRequest declineInviteRequest);
    InvitationResponse acceptInvitation(AcceptInviteRequest acceptInviteRequest);
}
