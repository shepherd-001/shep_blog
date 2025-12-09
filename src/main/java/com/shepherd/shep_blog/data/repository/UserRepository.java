package com.shepherd.shep_blog.data.repository;

import com.shepherd.shep_blog.data.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByUserNameEqualsIgnoreCase(String userName);
    boolean existsByEmailEqualsIgnoreCase(String email);
    boolean existsByRoleName(String roleName);

    @Query("""
        select u from User  u
        join fetch u.role r
        left join fetch r.permissions
        where lower(u.email) = lower(:email)
       """)
    Optional<User> findByEmailWithRoleAndPermissions(@Param("email") String email);

    Optional<User> findByEmailEqualsIgnoreCase(String email);
}