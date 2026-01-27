package com.shepherd.shep_blog.data.repository;

import com.shepherd.shep_blog.data.model.Author;
import com.shepherd.shep_blog.data.model.enums.TeamMemberRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuthorRepository extends JpaRepository<Author, UUID> {
    boolean existsByIdAndTeamMembers_Role(UUID authorId, TeamMemberRole role);
}
