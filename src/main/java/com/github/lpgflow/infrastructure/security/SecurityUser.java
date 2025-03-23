package com.github.lpgflow.infrastructure.security;

import com.github.lpgflow.domain.user.dto.response.UserForSecurityDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class SecurityUser implements UserDetails {

    private final String username;
    private final String password;
    private final Set<String> roles;
    private final boolean enabled;
    private final boolean blocked;

    SecurityUser(UserForSecurityDto dto) {
        this.username = dto.email();
        this.password = dto.password();
        this.roles = dto.roles();
        this.enabled = dto.enabled();
        this.blocked = dto.blocked();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> (GrantedAuthority) () -> "ROLE_" + role)
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !blocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public List<String> getAuthoritiesAsString() {
        return roles.stream().toList();
    }
 }
