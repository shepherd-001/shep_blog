package com.shepherd.shep_blog.common.exceptions;

import com.shepherd.shep_blog.common.ErrorCode;
import org.springframework.http.HttpStatus;

public class ShepBlogException extends BaseApiException {
    public ShepBlogException(String message) {
        super(message, ErrorCode.SHEP_BLOG_ERROR, HttpStatus.BAD_REQUEST);
    }

    public ShepBlogException(String message, Throwable cause) {
        super(message, ErrorCode.SHEP_BLOG_ERROR, HttpStatus.BAD_REQUEST, cause);
    }
}
