package com.shepherd.shep_blog.data.repository;

import com.shepherd.shep_blog.data.model.Reader;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReaderRepository extends JpaRepository<Reader, String> {
}
