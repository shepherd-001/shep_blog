package com.shepherd.shep_blog.services.userRoleAndPermission;

import com.shepherd.shep_blog.data.model.Permission;
import com.shepherd.shep_blog.data.repository.PermissionRepository;
import com.shepherd.shep_blog.exceptions.AlreadyExistsException;
import com.shepherd.shep_blog.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PermissionService {
    private final PermissionRepository permissionRepository;
    private final CacheManager cacheManager;
    private static final String CACHE_NAME = "permissions";

//    @Cacheable(value = CACHE_NAME, key = "#name.toUpperCase()")
    public Permission getPermission(String name){
        log.info("Fetching permission '{}' from DB", name);
        return permissionRepository.findByNameEqualsIgnoreCase(name.trim())
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found"));
    }

//    @CachePut(value = CACHE_NAME, key = "#name.toUpperCase()")
    public Permission addPermission(String name){
        name = name.trim();
        if(permissionRepository.existsByNameEqualsIgnoreCase(name)){
            throw new AlreadyExistsException(String.format("Permission '%s' already exists", name));
        }

        log.info("Adding permission '{}' to DB", name);
        return permissionRepository.save(
                Permission.builder()
                        .name(name.toUpperCase())
                        .build()
        );
    }

//    @CacheEvict(value = CACHE_NAME, key = "#name.toUpperCase()")
    public void deletePermission(String name){
        Permission permission = getPermission(name);
        permissionRepository.delete(permission);
        log.info("Deleted permission '{}' from DB", name);
    }

    public void clearCache(){
        Cache cache = cacheManager.getCache(CACHE_NAME);
        if(cache != null){
            cache.clear();
            log.info("Cache cleared");
        }
    }
}