package com.shepherd.shep_blog.data.model;

import com.shepherd.shep_blog.common.baseEntities.AuditableEntity;
import com.shepherd.shep_blog.data.model.enums.InvitationStatus;
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
public class Invitation extends AuditableEntity {
    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private InvitationStatus status;

    private Instant respondedAt;
}