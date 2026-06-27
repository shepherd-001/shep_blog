package com.shepherd.shep_blog.data.model;

import com.shepherd.shep_blog.common.baseEntities.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(indexes = @Index(name = "idx_category_slug", columnList = "slug"))
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Category extends BaseEntity {
    private String name;
    private String slug;
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;
}