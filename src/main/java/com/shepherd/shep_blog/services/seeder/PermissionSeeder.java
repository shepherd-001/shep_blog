package com.shepherd.shep_blog.services.seeder;

import com.shepherd.shep_blog.data.model.Permission;
import com.shepherd.shep_blog.data.repository.PermissionRepository;
import com.shepherd.shep_blog.services.userRoleAndPermission.PermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class PermissionSeeder {
    private final PermissionRepository permissionRepository;
    private final PermissionService permissionService;

    private static final List<String> PERMISSION_NAMES = List.of(
            "CREATE_POST", "EDIT_POST", "DELETE_POST", "VIEW_POST",
            "MANAGE_USERS", "INVITE_ADMIN", "MANAGE_POSTS"
    );

    public void seedPermissions(){
        Set<String> existingPermissions = permissionRepository.findAllNamesAsSet();

        List<Permission> permissions = PERMISSION_NAMES.stream()
                .filter(key -> !existingPermissions.contains(key))
                .map(key -> Permission.builder()
                        .name(key)
                        .build())
                .toList();

        if(!permissions.isEmpty()){
            log.info("Seeding {} new permissions", permissions.size());
            permissionRepository.saveAll(permissions);
            permissionService.clearCache();
        }
    }
}