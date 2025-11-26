package com.shepherd.shep_blog.data.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(indexes = {
        @Index(name = "idx_email", columnList = "email"),
        @Index(name = "idx_createdAt", columnList = "createdAt")
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Role role;
//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "role_id", nullable = false)
//    private UserRole role;

    @Column(name = "enabled")
    private boolean isEnabled;
    private boolean isEmailVerified;
    @CreatedDate
    private Instant createdAt;
}
