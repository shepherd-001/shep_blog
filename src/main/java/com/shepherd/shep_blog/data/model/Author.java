package com.shepherd.shep_blog.data.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Author extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<TeamMember> teamMembers =  new ArrayList<>();

    private String organizationPhoneNumber;
    private String websiteAddress;
    private String approvedBy;

    public void addMember(TeamMember teamMember) {
        teamMembers.add(teamMember);
        teamMember.setAuthor(this);
    }
}