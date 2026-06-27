package com.shepherd.shep_blog.common.exceptions;

import com.shepherd.shep_blog.common.ErrorCode;
import org.springframework.http.HttpStatus;

public class InvalidJwtException extends BaseApiException {
    public InvalidJwtException(String message) {
        super(message, ErrorCode.SHEP_BLOG_ERROR, HttpStatus.BAD_REQUEST);
    }
}
