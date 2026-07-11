package com.shepherd.shep_blog.services.userRoleAndPermission;

import com.shepherd.shep_blog.data.model.UserRole;

import java.util.Set;

public interface RoleHierarchyService {
    Set<UserRole> resolveInheritedRoles(UserRole role);
}
