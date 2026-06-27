package com.shepherd.shep_blog.data.model;

import com.shepherd.shep_blog.common.baseEntities.BaseEntity;
import com.shepherd.shep_blog.data.model.enums.TeamMemberRole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TeamMember extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private TeamMemberRole role;

    @Enumerated(EnumType.STRING)
    private TeamMemberStatus status;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;
}