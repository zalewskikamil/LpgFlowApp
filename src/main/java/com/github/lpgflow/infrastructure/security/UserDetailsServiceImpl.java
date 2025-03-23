package com.github.lpgflow.infrastructure.security;

import com.github.lpgflow.domain.user.UserFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RequiredArgsConstructor
@Log4j2
class UserDetailsServiceImpl implements UserDetailsService {

    private final UserFacade userFacade;

    @Override
    public UserDetails loadUserByUsername(final String username) {
        return userFacade.findUserByEmailForSecurity(username)
                .map(SecurityUser::new)
                .orElseThrow(() -> {
                    log.warn("Failed login attempt: user {} not found", username);
                    return new UsernameNotFoundException(
                            "Username: " + username + " not found");
                });
    }
}
