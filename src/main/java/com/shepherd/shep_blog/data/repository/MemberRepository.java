package com.shepherd.shep_blog.data.repository;

import com.shepherd.shep_blog.data.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, String> {
}
