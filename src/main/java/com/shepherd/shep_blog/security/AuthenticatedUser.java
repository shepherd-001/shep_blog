package com.shepherd.shep_blog.security;

import com.shepherd.shep_blog.data.model.User;
import com.shepherd.shep_blog.data.model.UserRole;
import com.shepherd.shep_blog.common.exceptions.UserNotVerifiedException;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Builder
@Getter
@Slf4j
public class AuthenticatedUser implements UserDetails {
    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(user == null) {
            log.warn("AuthenticatedUser has no user.");
            return Collections.emptyList();
        }

        Set<UserRole> roles = user.getRoles();
        if(roles == null || roles.isEmpty()) {
            log.warn("User {} has no roles", user.getEmail());
            return Collections.emptyList();
        }

        return roles.stream()
                .filter(Objects::nonNull)
                .flatMap(role -> {
                    Stream<SimpleGrantedAuthority> roleAuthority =
                            Stream.of(new  SimpleGrantedAuthority("ROLE_" + role.getName()));
                    Stream<SimpleGrantedAuthority> permissionAuthorities =
                            Optional.ofNullable(role.getPermissions())
                                    .orElse(Collections.emptySet())
                                    .stream()
                                    .filter(Objects::nonNull)
                                    .map(permission -> new SimpleGrantedAuthority(permission.getName()));
                    return Stream.concat(roleAuthority, permissionAuthorities);
                })
                .collect(Collectors.toUnmodifiableSet());
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
        if (!user.isEmailVerified()) {
            throw new UserNotVerifiedException("Verify your email before you proceed");
        }
        return user.isEnabled();
    }
}