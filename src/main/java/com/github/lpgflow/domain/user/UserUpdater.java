package com.github.lpgflow.domain.user;

import com.github.lpgflow.domain.user.dto.request.UpdatePasswordRequestDto;
import com.github.lpgflow.infrastructure.security.AuthenticatedUserProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
class UserUpdater {

    private final UserRetriever userRetriever;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticatedUserProvider authenticatedUserProvider;

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

    void updatePassword(UpdatePasswordRequestDto request) {
        String newPassword = request.newPassword();
        if (!newPassword.equals(request.confirmNewPassword())) {
            throw new UpdatePasswordException("New password does not match");
        }
        String actualPassword = request.actualPassword();
        if (actualPassword.equals(newPassword)) {
            throw new UpdatePasswordException("The new password must be different from the current one");
        }
        String currentUserEmail = authenticatedUserProvider.getCurrentUserName();
        User user = userRetriever.findByEmail(currentUserEmail);
        if (!passwordEncoder.matches(actualPassword, user.getPassword())) {
            throw new UpdatePasswordException("Wrong actual password");
        }
        String encodedNewPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedNewPassword);
        log.info("Updating user password with id: {}", user.getId());
        userRepository.save(user);
    }
}
