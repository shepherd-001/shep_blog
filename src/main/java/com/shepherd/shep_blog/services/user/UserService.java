package com.shepherd.shep_blog.services.user;

import com.shepherd.shep_blog.data.dto.request.PaginationRequest;
import com.shepherd.shep_blog.data.dto.request.RegisterReaderRequest;
import com.shepherd.shep_blog.data.dto.response.PaginationResponse;
import com.shepherd.shep_blog.data.dto.response.RegisterUserResponse;
import com.shepherd.shep_blog.data.dto.response.UserResponse;
import com.shepherd.shep_blog.data.model.User;

public interface UserService {
    RegisterUserResponse registerReader(RegisterReaderRequest request);
    void checkIfUserEmailExists(String email);
    void checkIfUserNameExists(String username);
    PaginationResponse<UserResponse> getAllEnabledUser(boolean enabled, PaginationRequest paginationRequest);
    User saveUser(User user);
}