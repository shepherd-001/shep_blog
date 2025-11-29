package com.shepherd.shep_blog.utils;

public final class ErrorMessage {
    public static final String NON_INSTANTIABLE_UTILITY_CLASS = "Utility class cannot be instantiated";
    public static final String INVALID_CURRENT_PASSWORD =  "Invalid current password";
    public static final String SAME_OLD_AND_NEW_PASSWORD =  "You cannot reuse a recent password. Please choose a new one.";
    public static final String MISMATCH_PASSWORD = "Passwords do not match";

    public static final String USER_EMAIL_NOT_FOUND = "User with the provided email not found";

    private ErrorMessage() {
        throw new UnsupportedOperationException(NON_INSTANTIABLE_UTILITY_CLASS);
    }
}
