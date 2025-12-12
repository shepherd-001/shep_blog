package com.shepherd.shep_blog.data.repository;

import com.shepherd.shep_blog.data.model.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvitationRepository extends JpaRepository<Invitation, String> {
}
