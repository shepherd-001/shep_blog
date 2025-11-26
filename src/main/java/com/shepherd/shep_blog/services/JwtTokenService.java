package com.shepherd.shep_blog.services;

import com.shepherd.shep_blog.utils.HashUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class JwtTokenService {
    private final RedisTemplate<String, String> redisTemplate;

    private static final String ACCESS_PREFIX = "jwt:access:jti:";
    private static final String REFRESH_PREFIX = "jwt:refresh:hash:";
    private static final String BLACKLIST_PREFIX = "jwt:blacklist:jti:";
    private static final String USER_ACCESS_TOKENS_PREFIX = "jwt:user:access:";
    private static final String USER_REFRESH_TOKENS_PREFIX = "jwt:user:refresh:";

    @Value("${jwt.access_expiration}")
    private long accessTokenTtl;

    @Value("${jwt.refresh_expiration}")
    private long refreshTokenTtl;


    public void storeAccessToken(String jwtId, String userEmail) {
        String key = ACCESS_PREFIX + jwtId;
        String userSetKey = USER_ACCESS_TOKENS_PREFIX + userEmail;

        redisTemplate.opsForValue().set(key, userSetKey, Duration.ofSeconds(accessTokenTtl));
        redisTemplate.opsForSet().add(userSetKey, jwtId);
        redisTemplate.expire(userSetKey, accessTokenTtl, TimeUnit.SECONDS);

    }

    public boolean isAccessTokenValid(String jwtId) {
        String key = ACCESS_PREFIX + jwtId;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key))
                && Boolean.FALSE.equals(redisTemplate.hasKey(BLACKLIST_PREFIX + jwtId));
    }

    public void blacklistAccessToken(String jwtId) {
        String key = BLACKLIST_PREFIX + jwtId;

        Long remainingTtl = redisTemplate.getExpire(ACCESS_PREFIX + jwtId);
        if(remainingTtl == null || remainingTtl <= 0) {
            remainingTtl = accessTokenTtl;
        }
        redisTemplate.opsForValue().set(key, "1", Duration.ofSeconds(remainingTtl));
        redisTemplate.delete(ACCESS_PREFIX + jwtId);
    }

    public void storeRefreshToken(String refreshTokenPlain, String userEmail) {
        String hash = HashUtils.sha256(refreshTokenPlain);
        String key = REFRESH_PREFIX + hash;
        String userSetKey = USER_REFRESH_TOKENS_PREFIX + userEmail;

        redisTemplate.opsForValue().set(key, userEmail, Duration.ofSeconds(refreshTokenTtl));
        redisTemplate.opsForSet().add(userSetKey, hash);
        redisTemplate.expire(userSetKey, refreshTokenTtl, TimeUnit.SECONDS);
    }

    public boolean isRefreshTokenValid(String refreshTokenPlain) {
        String hash = HashUtils.sha256(refreshTokenPlain);
        return Boolean.TRUE.equals(redisTemplate.hasKey(REFRESH_PREFIX + hash));
    }

    public void revokeRefreshToken(String refreshTokenPlain) {
        String hash = HashUtils.sha256(refreshTokenPlain);
        redisTemplate.delete(REFRESH_PREFIX + hash);
    }

    public void revokeAllTokensForUser(String userEmail) {
        Set<String> accessTokens = redisTemplate.opsForSet().members(USER_ACCESS_TOKENS_PREFIX + userEmail);
        if(accessTokens != null){
            for(String jwtId : accessTokens){
                redisTemplate.delete(ACCESS_PREFIX + jwtId);
                redisTemplate.delete(BLACKLIST_PREFIX + jwtId);
            }
        }

        Set<String> refreshTokens = redisTemplate.opsForSet().members(USER_REFRESH_TOKENS_PREFIX + userEmail);
        if(refreshTokens != null){
            for(String jwtId : refreshTokens){
                redisTemplate.delete(REFRESH_PREFIX + jwtId);
            }
        }
        redisTemplate.delete(USER_ACCESS_TOKENS_PREFIX + userEmail);
        redisTemplate.delete(USER_REFRESH_TOKENS_PREFIX + userEmail);
    }
}