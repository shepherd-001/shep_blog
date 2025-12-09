package com.shepherd.shep_blog.data.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "app_user_id", nullable = false, unique = true)
    private User user;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;
}