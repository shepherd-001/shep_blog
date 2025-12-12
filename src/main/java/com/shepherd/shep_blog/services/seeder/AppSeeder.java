package com.shepherd.shep_blog.services.seeder;

import com.shepherd.shep_blog.services.superAdmin.SuperAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppSeeder implements ApplicationRunner {
    private final PermissionSeeder permissionSeeder;
    private final RoleSeeder roleSeeder;
    private final SuperAdminService superAdminService;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        permissionSeeder.seedPermissions();
        roleSeeder.seedRoles();
        superAdminService.createSuperAdminIfNotExists();
    }
}
