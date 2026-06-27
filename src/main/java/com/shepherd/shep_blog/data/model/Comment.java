package com.shepherd.shep_blog.data.model;

import com.shepherd.shep_blog.common.baseEntities.AuditableEntity;
import com.shepherd.shep_blog.data.model.enums.ModerationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(indexes = {
        @Index(name = "idx_comment_post", columnList = "post_id"),
        @Index(name = "idx_comment_reader", columnList = "reader_id")
})
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Comment extends AuditableEntity {
    @Column(columnDefinition = "text")
    private String content;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reader_id")
    private Reader reader;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;
    @OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private Set<Comment> replies;
    @Enumerated(EnumType.STRING)
    private ModerationStatus moderationStatus = ModerationStatus.PENDING;
}