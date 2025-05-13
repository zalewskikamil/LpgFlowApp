package com.github.lpgflow.domain.user;

import com.github.lpgflow.infrastructure.security.AuthenticatedUserProvider;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserFacadeConfiguration {

    public static UserFacade createUserCrud(final UserRepository userRepository,
                                            final RoleRepository roleRepository,
                                            final PasswordEncoder passwordEncoder,
                                            final AuthenticatedUserProvider authenticatedUserProvider) {
        UserRetriever userRetriever = new UserRetriever(userRepository);
        UserAdder userAdder = new UserAdder(userRepository, userRetriever, passwordEncoder);
        UserUpdater userUpdater = new UserUpdater(userRetriever, userRepository, passwordEncoder, authenticatedUserProvider);
        RoleRetriever roleRetriever = new RoleRetriever(roleRepository);
        RoleAssigner roleAssigner = new RoleAssigner(userRetriever, roleRetriever, userRepository);
        return new UserFacade(userRetriever, userAdder, userUpdater, roleRetriever, roleAssigner);
    }
}
