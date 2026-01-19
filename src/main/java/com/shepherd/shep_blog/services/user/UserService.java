package com.shepherd.shep_blog.services.user;

import com.shepherd.shep_blog.data.dto.request.PaginationRequest;
import com.shepherd.shep_blog.data.dto.request.RegisterReaderRequest;
import com.shepherd.shep_blog.data.dto.response.PaginationResponse;
import com.shepherd.shep_blog.data.dto.response.RegisterUserResponse;
import com.shepherd.shep_blog.data.dto.response.UserResponse;
import com.shepherd.shep_blog.data.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserService {
    RegisterUserResponse registerReader(RegisterReaderRequest request);
    void checkIfUserEmailExists(String email);
    void checkIfUserNameExists(String username);
    PaginationResponse<UserResponse> getAllEnabledUser(boolean enabled, PaginationRequest paginationRequest);
    User saveUser(User user);
    boolean existsUserRole(UUID userId, String roleName);
    User getByEmail(String email);
    Optional<User> getByEmailIgnoreCase(String email);
}