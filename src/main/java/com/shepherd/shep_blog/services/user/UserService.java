package com.shepherd.shep_blog.services.user;

import com.shepherd.shep_blog.data.dto.request.RegisterReaderRequest;
import com.shepherd.shep_blog.data.dto.response.RegisterUserResponse;

public interface UserService {
    RegisterUserResponse registerReader(RegisterReaderRequest request);
    void checkIfUserEmailExists(String email);
    void checkIfUserNameExists(String username);
}
