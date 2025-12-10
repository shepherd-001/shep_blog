package com.shepherd.shep_blog.utils;

public final class ValidationMessage {
    public static final String BLANK_FIRST_NAME = "First name is required";
    public static final String BLANK_LAST_NAME = "Last name is required";
    public static final String BLANK_USER_NAME = "User name is required";
    public static final String BLANK_EMAIL = "Email address is required";
    public static final String BLANK_PASSWORD = "Password is required";
    public static final String BLANK_TOKEN = "Token is required";
    public static final String BLANK_PHONE_NUMBER = "Phone number is required";
    public static final String BLANK_WEBSITE_ADDRESS = "Website address is required";


    public static final String INVALID_FIRST_NAME = "Invalid first name";
    public static final String INVALID_LAST_NAME = "Invalid last name";
    public static final String INVALID_USER_NAME = "Usernames must be 3–20 characters and can contain letters, numbers, or underscores";
    public static final String INVALID_PASSWORD = "Password must be 8–20 characters with at least one uppercase, one lowercase, one digit, and one special character";
    public static final String INVALID_EMAIL = "Invalid email address";
    public static final String INVALID_PHONE_NUMBER = "Phone number is invalid";
    public static final String INVALID_GENDER = "Invalid gender. Allowed values: MALE, FEMALE";

    public static final String NULL_GENDER = "Gender is required";

    private ValidationMessage() {
        throw new UnsupportedOperationException(ErrorMessage.NON_INSTANTIABLE_UTILITY_CLASS);
    }
}
