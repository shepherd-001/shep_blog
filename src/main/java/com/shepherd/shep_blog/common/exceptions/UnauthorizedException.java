package com.shepherd.shep_blog.common.exceptions;

import com.shepherd.shep_blog.common.ErrorCode;
import org.springframework.http.HttpStatus;

public class UnauthorizedException extends BaseApiException {
    public UnauthorizedException(String message) {
        super(message, ErrorCode.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
    }
}
