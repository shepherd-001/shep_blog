package com.shepherd.shep_blog.data.model;

import com.shepherd.shep_blog.common.baseEntities.AuditableEntity;
import com.shepherd.shep_blog.common.exceptions.ShepBlogException;
import com.shepherd.shep_blog.data.model.enums.UserRoleType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "roles",
        indexes = {
                @Index(name = "idx_role_name", columnList = "name")
        }
//        uniqueConstraints = @UniqueConstraint(name = "uk_roles_name", columnNames = "name")
)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRole extends AuditableEntity {
    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @Column(length = 255)
    private String description;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "role_permissions",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> permissions = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRoleType type = UserRoleType.CUSTOM;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "role_hierarchy",
            joinColumns = @JoinColumn(name = "child_role_id"),
            inverseJoinColumns = @JoinColumn(name = "parent_role_id")
    )
    private final Set<UserRole> parentRoles = new HashSet<>();


    public void addParentRole(UserRole parentRole) {

        Objects.requireNonNull(parentRole);

        if (this.equals(parentRole)) {
            throw new ShepBlogException(
                    "A role cannot inherit itself."
            );
        }

        parentRoles.add(parentRole);
    }
}