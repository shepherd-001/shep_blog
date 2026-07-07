package com.shepherd.shep_blog.common.exceptions;

import com.shepherd.shep_blog.common.ErrorCode;
import org.springframework.http.HttpStatus;

public class UserNotVerifiedException extends BaseApiException {
    public UserNotVerifiedException(String message) {
        super(message, ErrorCode.VALIDATION_ERROR, HttpStatus.UNAUTHORIZED);
    }
}
