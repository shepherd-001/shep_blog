package com.shepherd.shep_blog.services.seeder;

import com.shepherd.shep_blog.data.model.Permission;
import com.shepherd.shep_blog.data.model.UserRole;
import com.shepherd.shep_blog.data.repository.PermissionRepository;
import com.shepherd.shep_blog.data.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.shepherd.shep_blog.utils.RoleUtil.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class RoleSeeder {

    private final UserRoleRepository roleRepository;
    private final PermissionRepository permissionRepository;


    @Transactional
    public void seedRoles() {

        Map<String, Set<String>> rolePermissionMap = Map.of(
                SUPER_ADMIN, Set.of("CREATE_POST", "EDIT_POST", "DELETE_POST", "MANAGE_USERS", "INVITE_ADMIN", "REMOVE_ADMIN"),
                ADMIN, Set.of("CREATE_POST", "EDIT_POST", "DELETE_POST", "MANAGE_USERS"),
                SUPER_AUTHOR, Set.of("CREATE_POST", "EDIT_POST", "DELETE_POST", "ADD_MEMBER", "REMOVE_MEMBER"),
                AUTHOR, Set.of("CREATE_POST", "EDIT_POST", "DELETE_POST"),
                READER, Set.of("VIEW_POST")
        );

        // Preload all permissions from DB to avoid repeated queries
        Map<String, Permission> existingPermissions = permissionRepository.findAll()
                .stream()
                .collect(Collectors.toMap(Permission::getName, p -> p));

        // Create missing permissions first
        rolePermissionMap.values().forEach(permissionNames -> {
            permissionNames.forEach(name -> {
                if (!existingPermissions.containsKey(name)) {
                    Permission saved = permissionRepository.save(Permission.builder()
                                    .name(name)
                            .build());
                    existingPermissions.put(name, saved);
                    log.info("[Seeder] Created permission: {}", name);
                }
            });
        });

        // Now seed roles
        rolePermissionMap.forEach((roleName, permissionNames) -> {

            UserRole role = roleRepository.findByNameEqualsIgnoreCase(roleName).orElse(null);

            Set<Permission> requiredPermissions =
                    permissionNames.stream()
                            .map(existingPermissions::get)
                            .collect(Collectors.toUnmodifiableSet());

            if (role == null) {
                // Create new role
                UserRole newRole = UserRole.builder()
                        .name(roleName)
                        .permissions(new HashSet<>(requiredPermissions))
                        .build();

                roleRepository.save(newRole);
                log.info("[Seeder] Created role: {}", roleName);
                return;
            }

            // Normalize null permissions safely
            Set<Permission> existingPerms =
                    Optional.ofNullable(role.getPermissions()).orElse(Collections.emptySet());

            // Idempotent update — only update if different
            if (!Objects.equals(existingPerms, requiredPermissions)) {
                role.setPermissions(new HashSet<>(requiredPermissions));
                roleRepository.save(role);
                log.info("[Seeder] Updated permissions for role: {}", roleName);
            } else {
                log.info("[Seeder] No changes for role: {}", roleName);
            }
        });
    }
}

//
//
//package com.shepherd.shep_blog.services.seeder;
//
//import com.shepherd.shep_blog.data.model.Permission;
//import com.shepherd.shep_blog.data.model.UserRole;
//import com.shepherd.shep_blog.data.repository.PermissionRepository;
//import com.shepherd.shep_blog.data.repository.UserRoleRepository;
//import com.shepherd.shep_blog.services.userRoleAndPermission.RoleService;
//import com.shepherd.shep_blog.utils.RoleUtil;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.Map;
//import java.util.Objects;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//@Component
//@RequiredArgsConstructor
//@Slf4j
//@Transactional
//public class RoleSeeder {
//    private final UserRoleRepository userRoleRepository;
//    private final PermissionRepository permissionRepository;
//    private final RoleService roleService;
//
//    private static final Map<String, List<String>> ROLE_PERMISSION_MAP = Map.of(
//            RoleUtil.SUPER_ADMIN, List.of(
//                    "superadmin.create", "superadmin.read", "superadmin.update", "superadmin.delete"),
//            RoleUtil.ADMIN, List.of(
//                    "admin.create", "admin.read", "admin.update", "admin.delete"),
//            RoleUtil.AUTHOR, List.of(
//                    "author.create", "author.read", "author.update", "author.delete"),
//            RoleUtil.READER, List.of(
//                    "reader.create", "reader.read", "reader.update", "reader.delete")
//    );
//
//    public void seedRoles(){
//        Set<String> existingRoles = userRoleRepository.findAllNamesAsSet();
//        List<UserRole> newRoles = ROLE_PERMISSION_MAP.keySet().stream()
//                .filter(roleName -> !existingRoles.contains(roleName))
//                .map(roleName -> UserRole.builder()
//                        .name(roleName)
//                        .build())
//                .toList();
//
//        if(!newRoles.isEmpty()){
//            log.info("Seeding {} new roles", newRoles.size());
//            userRoleRepository.saveAll(newRoles);
//        }
//
//        Map<String, Permission> permissionMap = permissionRepository.findAll().stream()
//                .collect(Collectors.toMap(Permission::getName, p -> p));
//
//        ROLE_PERMISSION_MAP.forEach((roleName, permissionKeys) -> {
//            UserRole role = userRoleRepository.findByNameEqualsIgnoreCase(roleName)
//                    .orElseThrow(()-> new IllegalStateException(String.format("Role '%s' missing after insert", roleName)));
//
//            Set<Permission> permissions = permissionKeys.stream()
//                    .map(permissionMap::get)
//                    .filter(Objects::nonNull)
//                    .collect(Collectors.toSet());
//
//            if(!role.getPermissions().equals(permissions)){
//                role.setPermissions(permissions);
//                userRoleRepository.save(role);
//                roleService.updateCache(role);
//                log.info("Assigned {} permissions to role '{}'", permissions.size(), roleName);
//            }
//        });
//    }
//}