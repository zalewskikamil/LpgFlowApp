package com.github.lpgflow.domain.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor(access = lombok.AccessLevel.PACKAGE)
class UserAdder {

    private final UserRepository userRepository;
    private final UserRetriever userRetriever;
    private final PasswordEncoder passwordEncoder;

    User addUser(final User user) {
        String email = user.getEmail();
        if (userRetriever.existsByEmail(email)) {
            throw new EmailAlreadyExistException("User with email: " + email + " already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        log.info("Adding user: {}", email);
        return userRepository.save(user);
    }
}
