package com.shepherd.shep_blog.services.userRoleAndPermission;

import com.shepherd.shep_blog.data.model.Permission;
import com.shepherd.shep_blog.data.model.UserRole;
import com.shepherd.shep_blog.data.repository.UserRoleRepository;
import com.shepherd.shep_blog.common.exceptions.AlreadyExistsException;
import com.shepherd.shep_blog.common.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleService {
    private final UserRoleRepository userRoleRepository;
    private final PermissionService permissionService;
    private final CacheManager cacheManager;
    private static final String CACHE_NAME = "roles";


//    @Cacheable(value = CACHE_NAME, key = "#name.toUpperCase()")
    public UserRole getRole(String name){
        log.info("==>> Loading role '{}' from DB", name);
        return userRoleRepository.findByNameEqualsIgnoreCase(name.trim())
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Role '%s' not found", name)));
    }

//    @CachePut(value = CACHE_NAME, key = "#name.toUpperCase()")
    public UserRole addRole(String name){
        name = name.trim();
        if(userRoleRepository.existsByNameEqualsIgnoreCase(name)){
            throw new AlreadyExistsException(String.format("Role '%s' already exists", name));
        }
        log.info("Adding role '{}' to DB", name);
        return userRoleRepository.save(UserRole.builder()
                .name(name.toUpperCase())
                .build());
    }

//    @CacheEvict(value = CACHE_NAME, key = "#name.toUpperCase()")
    public void deleteRole(String name){
        UserRole role = getRole(name);
        userRoleRepository.delete(role);
        log.info("Deleted role '{}' from DB", name);
    }

    public UserRole assignPermissionsToRole(String name, List<String> permissionNames){
        UserRole role = getRole(name);

        Set<Permission> permissions = permissionNames.stream()
                .map(permission -> permissionService.getPermission(permission))
                .collect(Collectors.toSet());

        role.setPermissions(permissions);
        UserRole updatedRole = userRoleRepository.save(role);
        log.info("Assigned '{}' permissions to role '{}'", permissions.size(), name);
        return updatedRole;
    }

    public void updateCache(UserRole role){
        Cache cache = cacheManager.getCache(CACHE_NAME);
        if(cache != null){
            cache.put(role.getName().toUpperCase(), role);
        }
    }
}