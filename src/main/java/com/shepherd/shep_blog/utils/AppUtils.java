package com.shepherd.shep_blog.utils;

import org.springframework.data.domain.Sort;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.SecureRandom;
import java.util.Base64;

import static com.shepherd.shep_blog.utils.ErrorMessage.NON_INSTANTIABLE_UTILITY_CLASS;

public final class AppUtils {
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final int TOKEN_LENGTH_BYTES = 32; // 256-bit
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int MAX_PAGE_SIZE = 100;
    public static final String DEFAULT_SORT_FIELD = "createdAt";
    public static final Sort.Direction DEFAULT_SORT_DIRECTION = Sort.Direction.DESC;

//    public static String generateOtp(){
//        int number = SECURE_RANDOM.nextInt(900_000) + 1000_000; // 100000-999999
//        return String.valueOf(number);
//    }

    public static String generateToken(){
        byte[] bytes = new byte[TOKEN_LENGTH_BYTES];
        SECURE_RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    public static boolean isValidUri(String url) {
        try {
            URI uri = new URI(url.trim());
            if (uri.getScheme() == null || !(uri.getScheme().equals("http") || uri.getScheme().equals("https"))) {
                return false;
            }
            return uri.getHost() != null;
        } catch (URISyntaxException e) {
            return false;
        }
    }

    private AppUtils() {
        throw new UnsupportedOperationException(NON_INSTANTIABLE_UTILITY_CLASS);
    }
}
