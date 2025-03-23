package com.github.lpgflow.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class RoleAssigner {

    private final UserRetriever userRetriever;
    private final RoleRetriever roleRetriever;
    private final UserRepository userRepository;

    User assignRoleToUser(Long userId, Long roleId) {
        User user = userRetriever.findById(userId);
        Role roleById = roleRetriever.getRoleById(roleId);
        if (user.getRoles().contains(roleById)) {
            throw new RoleAlreadyAssignedToUserException(
                    "Role " + roleById.getName() + " is already assigned to user with id: " + user.getId());
        }
        user.addRole(roleById);
        return userRepository.save(user);
    }
}
