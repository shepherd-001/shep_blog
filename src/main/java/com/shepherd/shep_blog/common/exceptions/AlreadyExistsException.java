package com.shepherd.shep_blog.common.exceptions;


import com.shepherd.shep_blog.common.ErrorCode;
import org.springframework.http.HttpStatus;

public class AlreadyExistsException extends BaseApiException {
    public AlreadyExistsException(String message) {
        super(message,
                ErrorCode.RESOURCE_ALREADY_EXISTS,
                HttpStatus.CONFLICT);
    }}
