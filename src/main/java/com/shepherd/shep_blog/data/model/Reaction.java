package com.shepherd.shep_blog.data.model;

import com.shepherd.shep_blog.data.model.baseEntities.BaseEntity;
import com.shepherd.shep_blog.data.model.enums.ReactionType;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(
//        uniqueConstraints = @UniqueConstraint(columnNames = {"reader_id", "post_id"}),
        indexes = @Index(name = "idx_reaction_post", columnList = "post_id"))
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Reaction extends BaseEntity {
    @Enumerated(EnumType.STRING)
    private ReactionType type;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reader_id")
    private Reader reader;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;
    private Instant createdAt = Instant.now();
}