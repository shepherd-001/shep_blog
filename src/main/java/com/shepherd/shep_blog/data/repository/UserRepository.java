package com.shepherd.shep_blog.data.repository;

import com.shepherd.shep_blog.data.model.Role;
import com.shepherd.shep_blog.data.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmailEqualsIgnoreCase(String email);
//    boolean existsByRoleName(String roleName);
    boolean existsByRole(Role role);

    // this is to fetch the user role eagerly only during authentication
//    @Query("SELECT u FROM User u JOIN FETCH u.role WHERE u.email = :email")
//    Optional<User> findByEmailEqualsIgnoreCaseWithRole(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmailEqualsIgnoreCaseWithRole(@Param("email") String email);
//    Optional<User> findByEmailEqualsIgnoreCase(String email);
}
