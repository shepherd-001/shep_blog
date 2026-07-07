package com.shepherd.shep_blog.data.model;

import com.shepherd.shep_blog.common.baseEntities.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Reader extends AuditableEntity {
    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
