package com.shepherd.shep_blog.utils;

import java.security.SecureRandom;
import java.util.Base64;

import static com.shepherd.shep_blog.utils.ErrorMessage.NON_INSTANTIABLE_UTILITY_CLASS;

public final class AppUtils {
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final int TOKEN_LENGTH_BYTES = 32; // 256-bit

    public static String generateOtp(){
        int number = SECURE_RANDOM.nextInt(900_000) + 1000_000; // 100000-999999
        return String.valueOf(number);
    }

    public static String generateToken(){
        byte[] bytes = new byte[TOKEN_LENGTH_BYTES];
        SECURE_RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private AppUtils() {
        throw new UnsupportedOperationException(NON_INSTANTIABLE_UTILITY_CLASS);
    }
}