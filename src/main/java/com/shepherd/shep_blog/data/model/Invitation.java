package com.shepherd.shep_blog.data.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "invitation", indexes = {
        @Index(columnList = "status")
})
public class Invitation extends BaseEntity{
    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private InvitationStatus status;

    private Instant respondedAt;
}