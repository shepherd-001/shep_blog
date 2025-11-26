package com.shepherd.shep_blog.exceptions;

public class ShepBlogException extends RuntimeException {
    public ShepBlogException(String message) {
        super(message);
    }

    public ShepBlogException(String message, Throwable cause) {
        super(message, cause);
    }
}
