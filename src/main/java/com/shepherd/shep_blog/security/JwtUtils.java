package com.shepherd.shep_blog.security;

import com.shepherd.shep_blog.exceptions.InvalidJwtException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtUtils {
    private final SecretKey signingKey;
    private JwtParser jwtParser;
    @Value("${jwt.access_expiration}")
    private long accessTokenExpiration;
    @Value("${jwt.refresh_expiration}")
    private long refreshTokenExpiration;
    @Value("${jwt.issuer}")
    private String issuer;
    private static final String TOKEN_TYPE = "tokenType";


    @PostConstruct
    void init() {
        this.jwtParser = Jwts.parser()
                .verifyWith(signingKey)
                .requireIssuer(issuer)
                .build();
    }

    public String extractUsername(String jwtToken){
        return extractClaim(jwtToken, Claims::getSubject);
    }

    private <T> T extractClaim(String jwtToken, Function<Claims, T> resolver) {
        return resolver.apply(extractAllClaims(jwtToken));
    }

    private Claims extractAllClaims(String jwtToken) {
        try{
            return jwtParser.parseSignedClaims(jwtToken).getPayload();
        }catch (JwtException ex){
            log.error(ex.getMessage());
            throw new InvalidJwtException("Invalid or expired token");
        }
    }

    public String generateAccessToken(String email){
        Map<String, Object> claims = Map.of(TOKEN_TYPE, "access");
        return buildJwtToken(claims, email, accessTokenExpiration);
    }

    public String generateRefreshToken(String email){
        Map<String, Object> claims = Map.of(TOKEN_TYPE, "refresh");
        return buildJwtToken(claims, email, refreshTokenExpiration);
    }

    private String buildJwtToken(Map<String, Object> claims, String email, long tokenExpiration){
        Instant now = Instant.now();
        return Jwts.builder()
                .issuer(issuer)
                .id(UUID.randomUUID().toString()) // jwtId
                .issuedAt(Date.from(now))
                .claims(claims)
                .subject(email)
                .expiration(Date.from(now.plusSeconds(tokenExpiration)))
                .signWith(signingKey)
                .compact();
    }

    public Instant getExpiration(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getExpiration().toInstant();
    }

    public boolean isValidToken(String token, String email) {
        Claims claims = extractAllClaims(token);
        Instant expiration = getExpiration(token);
        Instant now = Instant.now();

        String subject = claims.getSubject();
        return subject != null
                && subject.equalsIgnoreCase(email)
                && expiration.isAfter(now);
    }

    public String getJwtId(String token){
        return extractAllClaims(token).getId();
    }
}