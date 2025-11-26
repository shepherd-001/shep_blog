package com.shepherd.shep_blog.security.securityConfig;

import com.shepherd.shep_blog.utils.HashUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class JwtTokenService {
    private final RedisTemplate<String, String> redisTemplate;

    private static final String ACCESS_PREFIX = "jwt:access:jti:";
    private static final String REFRESH_PREFIX = "jwt:refresh:hash:";
    private static final String USER_TOKENS_PREFIX = "jwt:user:tokens:";
    private static final String BLACKLIST_PREFIX = "jwt:blacklist:jti:";

    @Value("${jwt.access_expiration}")
    private long accessTokenTtl;

    @Value("${jwt.refresh_expiration}")
    private long refreshTokenTtl;

    public void storeAccessToken(String jwtId, String userEmail) {
        String key = ACCESS_PREFIX + jwtId;
        redisTemplate.opsForValue().set(key, userEmail, Duration.ofSeconds(accessTokenTtl));
        redisTemplate.opsForSet().add(USER_TOKENS_PREFIX + userEmail, jwtId);
        redisTemplate.expire(USER_TOKENS_PREFIX + userEmail, accessTokenTtl, TimeUnit.SECONDS);
    }

    public boolean isAccessTokenValid(String jwtId) {
        boolean exists = Boolean.TRUE.equals(redisTemplate.hasKey(ACCESS_PREFIX + jwtId));
        boolean blacklisted = Boolean.TRUE.equals(redisTemplate.hasKey(BLACKLIST_PREFIX + jwtId));
        return exists && !blacklisted;
    }

    public void blacklistAccessToken(String jwtId, long ttlSeconds) {
        redisTemplate.opsForValue().set(BLACKLIST_PREFIX + jwtId, "1", ttlSeconds, TimeUnit.SECONDS);
        redisTemplate.delete(ACCESS_PREFIX + jwtId);
    }

    public void storeRefreshToken(String refreshTokenPlain, String userEmail) {
        String hash = HashUtils.sha256(refreshTokenPlain);
        String key = REFRESH_PREFIX + hash;
        redisTemplate.opsForValue().set(key, userEmail, Duration.ofSeconds(refreshTokenTtl));
        redisTemplate.opsForSet().add(USER_TOKENS_PREFIX + userEmail, hash);
        redisTemplate.expire(USER_TOKENS_PREFIX + userEmail, refreshTokenTtl, TimeUnit.SECONDS);
    }

    public boolean isRefreshTokenValid(String refreshTokenPlain) {
        String hash = HashUtils.sha256(refreshTokenPlain);
        return redisTemplate.hasKey(REFRESH_PREFIX + hash);
    }

    public void revokeRefreshToken(String refreshTokenPlain) {
        String hash = HashUtils.sha256(refreshTokenPlain);
        redisTemplate.delete(REFRESH_PREFIX + hash);
    }

    public void revokeAllTokensForUser(String userEmail) {
        var jwtIds = redisTemplate.opsForSet().members(USER_TOKENS_PREFIX + userEmail);
        if (jwtIds != null) {
            for (Object jti : jwtIds) {
                String key = jti.toString();
                redisTemplate.delete(ACCESS_PREFIX + key);
                redisTemplate.delete(REFRESH_PREFIX + key);
                redisTemplate.delete(BLACKLIST_PREFIX + key);
            }
        }
        redisTemplate.delete(USER_TOKENS_PREFIX + userEmail);
    }

    public String getUserIdFromAccessToken(String jwtId) {
        Object userId = redisTemplate.opsForValue().get(ACCESS_PREFIX + jwtId);
        return userId != null ? userId.toString() : null;
    }
}