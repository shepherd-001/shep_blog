package com.shepherd.shep_blog.data.repository;

import com.shepherd.shep_blog.data.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, String> {
}
