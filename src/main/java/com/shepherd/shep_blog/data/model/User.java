package com.shepherd.shep_blog.data.model;

import com.shepherd.shep_blog.data.model.enums.Gender;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(indexes = {
        @Index(name = "idx_userName", columnList = "userName"),
        @Index(name = "idx_email", columnList = "email"),
        @Index(name = "idx_createdAt", columnList = "createdAt")
})
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String userName;
    @Column(unique = true)
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    private UserRole role;

    private boolean enabled;
    private boolean emailVerified;
}
