package com.shepherd.shep_blog.data.model;

import com.shepherd.shep_blog.common.baseEntities.AuditableEntity;
import com.shepherd.shep_blog.data.model.enums.PostStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.Set;

@Entity
@Table(indexes = {
        @Index(name = "idx_post_slug", columnList = "slug", unique = true),
        @Index(name = "idx_post_author", columnList = "author_id"),
        @Index(name = "idx_post_publishedAt", columnList = "published_at")
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Post extends AuditableEntity {
    private String title;
    @Column(unique = true, length = 400)
    private String slug;
    @Column(columnDefinition = "text")
    private String content;
    @Enumerated(EnumType.STRING)
    private PostStatus status;
    private Instant publishedAt;
    private Instant scheduledAt;
    private Long viewCount = 0L;
    private Long commentsCount = 0L;
    private Long reactionCount = 0L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Author author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Media> media;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<Comment> comments;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<Reaction> reactions;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "post_tag",
        joinColumns = @JoinColumn(name = "post_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags;
}