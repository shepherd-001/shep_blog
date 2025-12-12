package com.shepherd.shep_blog.data.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class AuthorResponse {
    private String authorId;
    private List<UserResponse> members;
    private String organizationPhoneNumber;
    private String websiteAddress;
}
