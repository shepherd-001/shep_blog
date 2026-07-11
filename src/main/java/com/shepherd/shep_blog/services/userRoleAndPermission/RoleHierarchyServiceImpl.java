package com.shepherd.shep_blog.services.userRoleAndPermission;

import com.shepherd.shep_blog.data.model.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class RoleHierarchyServiceImpl implements RoleHierarchyService{
    @Override
    public Set<UserRole> resolveInheritedRoles(UserRole role) {
        Set<UserRole> visited = new HashSet<>();
        traverse(role, visited);
        return visited;
    }

    private void traverse(UserRole role, Set<UserRole> visited) {
        if(!visited.add(role)){
            return;
        }

        for (UserRole parent: role.getParentRoles()){
            traverse(parent, visited);
        }
    }
}
