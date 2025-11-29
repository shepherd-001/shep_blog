package com.shepherd.shep_blog.data.model;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Builder
@Value
public class TokenEntity {
    String email;

    @Builder.Default
    Instant createdAt = Instant.now();
}