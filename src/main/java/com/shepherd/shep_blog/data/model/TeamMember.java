package com.shepherd.shep_blog.data.model;

import com.shepherd.shep_blog.data.model.enums.TeamMemberRole;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TeamMember {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private TeamMemberRole role;

    private boolean isRevoked = false;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;
}