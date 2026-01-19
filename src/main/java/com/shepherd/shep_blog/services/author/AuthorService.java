package com.shepherd.shep_blog.services.author;


import com.shepherd.shep_blog.data.dto.request.AddTeamMemberRequest;
import com.shepherd.shep_blog.data.dto.request.PaginationRequest;
import com.shepherd.shep_blog.data.dto.request.RegisterAuthorRequest;
import com.shepherd.shep_blog.data.dto.response.AuthorResponse;
import com.shepherd.shep_blog.data.dto.response.PaginationResponse;

public interface AuthorService {
    AuthorResponse registerAuthor(RegisterAuthorRequest registerAuthorRequest);
    PaginationResponse<AuthorResponse> getAllAuthor(PaginationRequest paginationRequest);
    AuthorResponse addTeamMember(AddTeamMemberRequest addTeamMemberRequest);
}
