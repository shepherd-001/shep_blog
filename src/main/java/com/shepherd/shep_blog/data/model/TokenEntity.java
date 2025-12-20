package com.shepherd.shep_blog.data.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.shepherd.shep_blog.data.model.enums.TokenType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS,
        include = JsonTypeInfo.As.PROPERTY,
        property = "@class"
)
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TokenEntity {
    String email;

    @Enumerated(EnumType.STRING)
    TokenType tokenType;

    @Builder.Default
    Instant createdAt = Instant.now();
}