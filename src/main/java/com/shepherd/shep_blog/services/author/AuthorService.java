package com.shepherd.shep_blog.services.author;


import com.shepherd.shep_blog.data.dto.request.CreateTeamMemberRequest;
import com.shepherd.shep_blog.data.dto.request.InviteTeamMemberRequest;
import com.shepherd.shep_blog.data.dto.request.PaginationRequest;
import com.shepherd.shep_blog.data.dto.request.RegisterAuthorRequest;
import com.shepherd.shep_blog.data.dto.response.AuthorResponse;
import com.shepherd.shep_blog.data.dto.response.PaginationResponse;
import com.shepherd.shep_blog.data.dto.response.TeamMemberResponse;

import java.util.UUID;

public interface AuthorService {
    AuthorResponse registerAuthor(RegisterAuthorRequest registerAuthorRequest);
    PaginationResponse<AuthorResponse> getAllAuthor(PaginationRequest paginationRequest);
    TeamMemberResponse createTeamMember(CreateTeamMemberRequest createTeamMemberRequest);
    TeamMemberResponse inviteTeamMember(InviteTeamMemberRequest inviteTeamMemberRequest);
    TeamMemberResponse activateTeamMember(UUID authorId);
}
