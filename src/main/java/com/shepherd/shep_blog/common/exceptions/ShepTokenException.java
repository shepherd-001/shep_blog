package com.shepherd.shep_blog.common.exceptions;

import com.shepherd.shep_blog.common.ErrorCode;
import org.springframework.http.HttpStatus;

public class ShepTokenException extends BaseApiException{
    public ShepTokenException(String message) {
        super(message, ErrorCode.SHEP_BLOG_ERROR, HttpStatus.BAD_REQUEST);
    }
}
