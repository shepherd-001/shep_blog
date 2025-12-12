package com.shepherd.shep_blog.services.author;


import com.shepherd.shep_blog.data.dto.request.RegisterAuthorRequest;
import com.shepherd.shep_blog.data.dto.response.AuthorResponse;

public interface AuthorService {
    AuthorResponse registerAuthor(RegisterAuthorRequest registerAuthorRequest);

}
