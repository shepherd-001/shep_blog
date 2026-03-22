package com.shepherd.shep_blog.data.model;

import com.shepherd.shep_blog.data.model.baseEntities.AuditableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Author extends AuditableEntity {
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