package com.github.lpgflow.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
class UserRetriever {

    private final UserRepository userRepository;

    List<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    User findById(final Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id: " + id + " not found"));
    }

    User findByEmail(String email) {
        return userRepository.findFirstByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email: " + email + " not found"));
    }

    boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
