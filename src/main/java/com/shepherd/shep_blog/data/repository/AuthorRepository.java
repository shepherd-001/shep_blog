package com.shepherd.shep_blog.data.repository;

import com.shepherd.shep_blog.data.model.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AuthorRepository extends JpaRepository<Author, String> {
//    Page<Author> get(Pageable pageable);
}
