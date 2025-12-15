package com.shepherd.shep_blog.utils;

public final class ErrorMessage {
    public static final String NON_INSTANTIABLE_UTILITY_CLASS = "Utility class cannot be instantiated";
    public static final String INVALID_CURRENT_PASSWORD =  "Invalid current password";
    public static final String SAME_OLD_AND_NEW_PASSWORD =  "You cannot reuse a recent password. Please choose a new one.";
    public static final String MISMATCH_PASSWORD = "Passwords do not match";

    public static final String USER_EMAIL_NOT_FOUND = "User not found";
    public static final String USER_EMAIL_ALREADY_EXISTS = "User with the provided email already exists";
    public static final String USER_NAME_ALREADY_EXISTS = "The user name provided is taken";

    public static final String INVALID_WEBSITE_ADDRESS = "Website address provided is invalid";
    public static final String INVITATION_NOT_FOUND = "Invitation not found";
    public static final String INVITATION_ALREADY_PROCESSED = "Invitation not found";
    public static final String TOKEN_IS_INVALID_OR_EXPIRED = "Token is invalid or expired";


    private ErrorMessage() {
        throw new UnsupportedOperationException(NON_INSTANTIABLE_UTILITY_CLASS);
    }
}
