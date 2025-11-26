package com.shepherd.shep_blog.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shepherd.shep_blog.data.dto.response.ApiResponse;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;


@Component
@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;
    private static final String ERROR_MESSAGE = "Authentication required. Please log in";


    @Override
    public void commence(
            @Nonnull HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            @Nonnull AuthenticationException authException) throws IOException {

        String requestURI = request.getRequestURI();
        if(response.isCommitted()){
            log.warn("Response already committed for unauthorized request: {}", requestURI);
            return;
        }

        log.info("Unauthorized access attempt: {}", requestURI);
        prepareUnauthorizedResponse(request, response);
    }


    private void prepareUnauthorizedResponse(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ApiResponse<?> errorResponse = ApiResponse.error(ERROR_MESSAGE, request, HttpStatus.UNAUTHORIZED);
        try (PrintWriter writer = response.getWriter()) {
            writer.write(objectMapper.writeValueAsString(errorResponse));
        }
    }
}
