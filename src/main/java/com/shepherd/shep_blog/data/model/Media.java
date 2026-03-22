package com.shepherd.shep_blog.data.model;

import com.shepherd.shep_blog.data.model.baseEntities.BaseEntity;
import com.shepherd.shep_blog.data.model.enums.MediaType;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(indexes = @Index(name = "idx_media_post", columnList = "post_id"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Media extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;
    private String url;
    @Enumerated(EnumType.STRING)
    private MediaType type;
    private Integer version = 1;
    private Instant uploadedAt;
    @Column(columnDefinition = "text")
    private String caption;
    @Column(length = 1000)
    private String thumbnailUrl;
}