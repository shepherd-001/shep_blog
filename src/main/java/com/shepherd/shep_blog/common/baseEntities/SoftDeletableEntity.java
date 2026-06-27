package com.shepherd.shep_blog.common.baseEntities;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class SoftDeletableEntity extends AuditableEntity{
    @Column(nullable = false)
    private boolean deleted = false;

    private Instant deletedAt;

    private UUID deletedBy;
}