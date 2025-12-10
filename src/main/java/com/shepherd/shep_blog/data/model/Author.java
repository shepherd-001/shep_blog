package com.shepherd.shep_blog.data.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Author extends BaseEntity {
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Member> members =  new ArrayList<>();

    private String organizationPhoneNumber;
    private String websiteAddress;

    public void addMember(Member member) {
        members.add(member);
        member.setAuthor(this);
    }
}