package com.shepherd.shep_blog.services.admin;

import com.shepherd.shep_blog.data.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService{
    private final AdminRepository adminRepository;
}
