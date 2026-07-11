package com.shepherd.shep_blog.data.repository;

import com.shepherd.shep_blog.data.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByUsernameIgnoreCase(String username);
    boolean existsByEmailIgnoreCase(String email);
    boolean existsByRoles_Name(String roleName);
    boolean existsByIdAndRoles_Name(UUID userId, String roleName);

    Optional<User> findByEmailIgnoreCase(String email);

    @Query("""
        select distinct u
        from User  u
        left join fetch u.roles r
        left join fetch r.permissions
        where lower(u.email) = lower(:email)
      """)
    Optional<User> findByEmailWithRoleAndPermissions(String email);

    @Query("""
        select u from User u
        where u.enabled = :enabled
        and not exists(
            select 1
            from u.roles r
            where r.name = :exludedRoleName
        )
   """)
    Page<User> findAllEnabledExcludingRole(boolean enabled, String excludedRoleName, Pageable pageable);
}