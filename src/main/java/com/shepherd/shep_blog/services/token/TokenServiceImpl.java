package com.shepherd.shep_blog.services.token;

import com.shepherd.shep_blog.data.model.TokenEntity;
import com.shepherd.shep_blog.data.model.TokenType;
import com.shepherd.shep_blog.exceptions.ShepTokenException;
import com.shepherd.shep_blog.utils.AppUtils;
import com.shepherd.shep_blog.utils.HashUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.shepherd.shep_blog.utils.ErrorMessage.TOKEN_IS_INVALID_OR_EXPIRED;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenServiceImpl implements TokenService{
    private final RedisTemplate<String, Object> redisTemplate;
    @Value("${reset_password_expiration}")
    private long resetPasswordExpiration;
    @Value("${email_confirmation_expiration}")
    private long emailConfirmationExpiration;
    @Value("${admin_invite_expiration}")
    private long adminInviteExpiration;
    @Value("${author_onboarding_expiration}")
    private long authorOnboardingExpiration;


    private long getExpirationTime(TokenType tokenType){
        return switch (tokenType){
            case RESET_PASSWORD -> resetPasswordExpiration;
            case EMAIL_CONFIRMATION -> emailConfirmationExpiration;
            case ADMIN_INVITATION -> adminInviteExpiration;
            case AUTHOR_SIGN_UP ->  authorOnboardingExpiration;
        };
    }

    private String tokenKey(TokenType tokenType, String token){
        return "blog:token:%s:%s".formatted(tokenType.name(), token);
    }

    private String userTokenSetKey(String email, TokenType type){
        return "blog:user:%s:%s".formatted(email, type.name());
    }

    @Override
    public String generateToken(String email, TokenType tokenType) {
        long ttl = getExpirationTime(tokenType);

        // revoke old user tokens
        revokeAllUserTokens(email, tokenType);

        String token = AppUtils.generateToken();
        String hashedToken = HashUtils.sha256(token);

        TokenEntity tokenEntity = TokenEntity.builder()
                .email(email)
                .tokenType(tokenType)
                .build();

        String key = tokenKey(tokenType, hashedToken);

        // save token with ttl
        redisTemplate.opsForValue().set(key, tokenEntity, ttl, TimeUnit.SECONDS);

        // add to user token set
        String setKey = userTokenSetKey(email, tokenType);
        redisTemplate.opsForSet().add(setKey, key);
        redisTemplate.expire(setKey, ttl, TimeUnit.SECONDS);

        log.info("==>>nCreated a new {} token for user {}", tokenType, email);
        return token;
    }

    @Override
    public TokenEntity validateToken(String token, TokenType tokenType, String expectedEmail) {
        String hashedToken = HashUtils.sha256(token);
        String key = tokenKey(tokenType, hashedToken);

        TokenEntity tokenEntity = (TokenEntity) redisTemplate.opsForValue().get(key);
        if(tokenEntity == null){
            throw new ShepTokenException(TOKEN_IS_INVALID_OR_EXPIRED);
        }
        else if(!tokenEntity.getTokenType().equals(tokenType)){
            log.error("==>> Token entity type {} doesn't match token type {}", tokenEntity.getTokenType(), tokenType);
            throw new ShepTokenException(TOKEN_IS_INVALID_OR_EXPIRED);
        }
        else if(!tokenEntity.getEmail().equalsIgnoreCase(expectedEmail.trim())){
            log.error("==>> Invalid email for token");
            throw new ShepTokenException(TOKEN_IS_INVALID_OR_EXPIRED);
        }

        // auto invalidate token after successful use
        redisTemplate.delete(key);
        redisTemplate.opsForSet().remove(userTokenSetKey(tokenEntity.getEmail(), tokenType), key);

        log.info("==>> Token '{}' validated for user {}", tokenType, expectedEmail);
        return tokenEntity;
    }

    @Override
    public void revokeAllUserTokens(String userEmail, TokenType tokenType) {
        String setKey = userTokenSetKey(userEmail, tokenType);
        Set<Object> keys  = redisTemplate.opsForSet().members(setKey);

        if(keys == null || keys.isEmpty()) return;

        log.info("Revoking {} {} tokens for user {}", keys.size(), tokenType.name(), userEmail);

        for(Object keyObj : keys){
            String key = keyObj.toString();
            redisTemplate.delete(key);
        }
        redisTemplate.delete(setKey);
    }
}