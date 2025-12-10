package com.shepherd.shep_blog.utils;

import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

public final class LinkBuilder {
    public static String build(String uri, String path, Map<String, ?> queryParams) {
        uri = normalizeBaseUri(uri);
        path = normalizePath(path);
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromUriString(uri)
                .path(path);
        if (queryParams != null) {
            queryParams.forEach(builder::queryParam);
        }
        return builder.toUriString();
    }

    private static String normalizeBaseUri(String url) {
        if (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }
        return url;
    }

    private static String normalizePath(String path) {
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        return path;
    }

    private LinkBuilder() {
        throw new UnsupportedOperationException(ErrorMessage.NON_INSTANTIABLE_UTILITY_CLASS);
    }
}
