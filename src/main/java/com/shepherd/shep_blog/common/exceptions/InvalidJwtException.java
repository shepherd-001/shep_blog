package com.shepherd.shep_blog.common.exceptions;

public class InvalidJwtException extends ShepBlogException {
    public InvalidJwtException(String message) {
        super(message);
    }
}
