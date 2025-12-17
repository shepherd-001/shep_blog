package com.shepherd.shep_blog.data.repository;

import com.shepherd.shep_blog.data.model.Admin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AdminRepository extends JpaRepository<Admin, String> {
    @Query("select a from Admin a where a.user.enabled is true and a.user.emailVerified is true ")
    Page<Admin> findAllActiveAdmins(Pageable pageable);
}
