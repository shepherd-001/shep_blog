package com.shepherd.shep_blog.services.user;

import com.shepherd.shep_blog.data.dto.request.RegisterAuthorRequest;
import com.shepherd.shep_blog.data.dto.request.RegisterReaderRequest;
import com.shepherd.shep_blog.data.dto.response.AuthorResponse;
import com.shepherd.shep_blog.data.dto.response.RegisterUserResponse;

public interface UserService {
    RegisterUserResponse registerReader(RegisterReaderRequest request);
    AuthorResponse registerAuthor(RegisterAuthorRequest registerAuthorRequest);
}
