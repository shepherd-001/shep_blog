package com.shepherd.shep_blog.exceptions;

public class UserAlreadyEnabledException extends ShepBlogException {
    public UserAlreadyEnabledException(String message) {
        super(message);
    }
}
