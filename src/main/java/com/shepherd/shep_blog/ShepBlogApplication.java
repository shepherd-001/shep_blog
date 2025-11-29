package com.shepherd.shep_blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableJpaAuditing(auditorAwareRef = "auditAware")
public class ShepBlogApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShepBlogApplication.class, args);
	}

}
