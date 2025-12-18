package com.shepherd.shep_blog.services.token;


import com.shepherd.shep_blog.data.model.TokenEntity;
import com.shepherd.shep_blog.data.model.enums.TokenType;

public interface TokenService {
    String generateToken(String userEmail, TokenType tokenType);
    TokenEntity validateToken(String token, TokenType tokenType, String expectedEmail);
    void revokeAllUserTokens(String userEmail, TokenType tokenType);
}
