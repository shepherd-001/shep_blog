package com.shepherd.shep_blog.data.repository;

import com.shepherd.shep_blog.data.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface PermissionRepository extends JpaRepository<Permission, UUID> {
    Optional<Permission> findByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCase(String name);
    @Query("select p.name from Permission p")
    Set<String> findAllNamesAsSet();
}