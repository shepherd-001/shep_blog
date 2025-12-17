package com.shepherd.shep_blog.data.dto;

import com.shepherd.shep_blog.data.dto.response.UserResponse;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AdminResponse {
    private UserResponse user;
}
