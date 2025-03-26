package com.github.lpgflow.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
class UserUpdater {

    private final UserRetriever userRetriever;
    private final UserRepository userRepository;

    boolean blockUser(final Long id) {
        User user = userRetriever.findById(id);
        if (user.isBlocked()) {
            return false;
        }
        user.setBlocked(true);
        userRepository.save(user);
        return true;
    }

    boolean unblockUser(final Long id) {
        User user = userRetriever.findById(id);
        if (!user.isBlocked()) {
            return false;
        }
        user.setBlocked(false);
        userRepository.save(user);
        return true;
    }

    boolean enableUser(final Long id) {
        User user = userRetriever.findById(id);
        if (user.isEnabled()) {
            return false;
        }
        user.setEnabled(true);
        userRepository.save(user);
        return true;
    }


    boolean disableUser(final Long id) {
        User user = userRetriever.findById(id);
        if (!user.isEnabled()) {
            return false;
        }
        user.setEnabled(false);
        userRepository.save(user);
        return true;
    }
}
