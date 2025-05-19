package com.github.lpgflow.domain.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
class UserUpdater {

    private final UserRetriever userRetriever;
    private final UserRepository userRepository;

    User updatePartiallyById(Long id, User userFromRequest) {
        Boolean enabled = userFromRequest.getEnabled();
        Boolean blocked = userFromRequest.getBlocked();
        if (enabled == null && blocked == null) {
            throw new UpdateUserException("No user parameters to change");
        }
        User userFromDatabase = userRetriever.findById(id);
        if (enabled != null) {
            userFromDatabase.setEnabled(enabled);
        }
        if (blocked != null) {
            userFromDatabase.setBlocked(blocked);
        }
        log.info("Updating user with id: {}", id);
        return userRepository.save(userFromDatabase);
    }
}
