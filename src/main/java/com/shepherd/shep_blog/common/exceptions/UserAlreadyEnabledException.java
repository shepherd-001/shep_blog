package com.shepherd.shep_blog.common.exceptions;

import com.shepherd.shep_blog.common.ErrorCode;
import org.springframework.http.HttpStatus;

public class UserAlreadyEnabledException extends BaseApiException {
    public UserAlreadyEnabledException(String message) {
        super(message, ErrorCode.CONFLICT, HttpStatus.CONFLICT);
    }
}
