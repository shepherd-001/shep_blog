package com.shepherd.shep_blog.data.repository;

import com.shepherd.shep_blog.data.model.TeamMember;
import com.shepherd.shep_blog.data.model.enums.TeamMemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TeamMemberRepository extends JpaRepository<TeamMember, UUID> {
    boolean existsByAuthorIdAndUserEmailIgnoreCase(UUID authorId, String email);
    boolean existsByAuthorIdAndUserId(UUID authorId, UUID userId);
    boolean existsByAuthorIdAndUserIdAndStatus(UUID authorId, UUID userId, TeamMemberStatus status);
    Optional<TeamMember> findByAuthor_IdAndUser_Id(UUID authorId, UUID userId);
}