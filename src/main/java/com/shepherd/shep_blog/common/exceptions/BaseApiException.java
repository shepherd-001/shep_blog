package com.shepherd.shep_blog.common.exceptions;

import com.shepherd.shep_blog.common.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class BaseApiException extends RuntimeException {
    private final ErrorCode errorCode;
    private final HttpStatus status;

    public BaseApiException(String message, ErrorCode errorCode, HttpStatus status){
        super(message);
        this.errorCode = errorCode;
        this.status = status;
    }

    public BaseApiException(String message, ErrorCode errorCode, HttpStatus status, Throwable cause){
        super(message, cause);
        this.errorCode = errorCode;
        this.status = status;
    }
}