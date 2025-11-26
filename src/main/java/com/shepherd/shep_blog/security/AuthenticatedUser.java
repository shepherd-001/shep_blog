package com.shepherd.shep_blog.security;

import com.shepherd.shep_blog.data.model.User;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Builder
@Getter
@Slf4j
public class AuthenticatedUser implements UserDetails {
    private final User user;
    private transient Collection<? extends GrantedAuthority> authorities;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(authorities != null) {
            return authorities;
        }

        if(user == null || user.getRole() == null) {
            log.warn("The user or user role is null");
            authorities = Collections.emptyList();
            return authorities;
        }

        List<SimpleGrantedAuthority> auths = new ArrayList<>();
        auths.add(new SimpleGrantedAuthority("ROLE_"+user.getRole().name()));

//        UserRole role = user.getRole();
//        List<SimpleGrantedAuthority> auths = new ArrayList<>();
//        auths.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
//
//        if(role.getPermissions() != null) {
//            role.getPermissions().stream()
//                    .filter(Objects::nonNull)
//                    .map(permission -> new SimpleGrantedAuthority(permission.getName()))
//                    .forEach(auths::add);
//        }
        authorities = Collections.unmodifiableCollection(auths);
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return (user.isEnabled() && user.isEmailVerified());
    }
}