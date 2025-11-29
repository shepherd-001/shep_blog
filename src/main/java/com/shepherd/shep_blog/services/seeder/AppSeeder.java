package com.shepherd.shep_blog.services.seeder;

import com.shepherd.shep_blog.services.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppSeeder implements ApplicationRunner {
    private final PermissionSeeder permissionSeeder;
    private final RoleSeeder roleSeeder;
    private final AdminService adminService;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        permissionSeeder.seedPermissions();
        roleSeeder.seedRoles();
        adminService.createSuperAdminIfNotExists();
    }
}
