package com.shepherd.shep_blog.data.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private final String message;
    private final T data;
    private final boolean success;
    private final int statusCode;
    private final String path;
    private final Instant timeStamp;

    public static <T> ApiResponse<T> success(String message, T data, HttpStatus status) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .statusCode(status.value())
                .timeStamp(Instant.now())
                .build();
    }

    public static <T> ApiResponse<T> success(String message, HttpStatus status) {
        return ApiResponse.<T>builder()
                .message(message)
                .success(true)
                .statusCode(status.value())
                .timeStamp(Instant.now())
                .build();
    }

    public static <T> ApiResponse<T> success(T data, HttpStatus status) {
        return ApiResponse.<T>builder()
                .data(data)
                .success(true)
                .statusCode(status.value())
                .timeStamp(Instant.now())
                .build();
    }

    public static <T> ApiResponse<T> error(String message, T data, HttpServletRequest request, HttpStatus status) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .data(data)
                .statusCode(status.value())
                .timeStamp(Instant.now())
                .path(request.getRequestURI())
                .build();
    }

    public static <T> ApiResponse<T> error(String message, HttpServletRequest request, HttpStatus status) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .statusCode(status.value())
                .timeStamp(Instant.now())
                .path(request.getRequestURI())
                .build();
    }

    public static <T> ApiResponse<T> error(T data, HttpServletRequest request, HttpStatus status) {
        return ApiResponse.<T>builder()
                .success(false)
                .data(data)
                .statusCode(status.value())
                .timeStamp(Instant.now())
                .path(request.getRequestURI())
                .build();
    }
}
