package com.shepherd.shep_blog.data.repository;

import com.shepherd.shep_blog.data.model.UserRole;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRoleRepository extends JpaRepository<UserRole, UUID> {
    @EntityGraph(attributePaths = "permissions")
    Optional<UserRole> findByNameIgnoreCase(String name);

    boolean existsByNameEqualsIgnoreCase(String name);

    @EntityGraph(attributePaths = {
            "permissions",
            "parentRoles"
    })
    Optional<UserRole> findWithPermissionsById(UUID id);
}