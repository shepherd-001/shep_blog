package com.shepherd.shep_blog.data.model;

import lombok.*;

import java.time.Instant;

@Builder
@Getter
//@Value
@NoArgsConstructor
@AllArgsConstructor
public class TokenEntity {
    String email;

    @Builder.Default
    Instant createdAt = Instant.now();
}