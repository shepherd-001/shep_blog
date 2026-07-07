package com.shepherd.shep_blog.data.model;

import com.shepherd.shep_blog.common.baseEntities.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(indexes = @Index(name = "idx_tag_name", columnList = "name"))
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Tag extends BaseEntity {
    private String name;
    private String slug;
}