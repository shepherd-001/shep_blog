package com.shepherd.shep_blog.utils;

public final class ErrorMessage {
    public static final String NON_INSTANTIABLE_UTILITY_CLASS = "Utility class cannot be instantiated";

    private ErrorMessage() {
        throw new UnsupportedOperationException(NON_INSTANTIABLE_UTILITY_CLASS);
    }
}
