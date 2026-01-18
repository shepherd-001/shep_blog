package com.shepherd.shep_blog.data.repository;

import com.shepherd.shep_blog.data.model.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TeamMemberRepository extends JpaRepository<TeamMember, UUID> {
    boolean existsByAuthorIdAndUserEmailIgnoreCase(UUID authorId, String email);
    boolean existsByAuthorIdAndUserId(UUID authorId, UUID userId);
}