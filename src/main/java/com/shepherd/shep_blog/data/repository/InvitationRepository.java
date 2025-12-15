package com.shepherd.shep_blog.data.repository;

import com.shepherd.shep_blog.data.model.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvitationRepository extends JpaRepository<Invitation, String> {
    Optional<Invitation> findByUserEmailEqualsIgnoreCase(String email);
}
