package com.github.lpgflow.domain.user;

import org.springframework.security.crypto.password.PasswordEncoder;

class UserFacadeConfiguration {

    public static UserFacade createUserCrud(final UserRepository userRepository,
                                            final RoleRepository roleRepository,
                                            final PasswordEncoder passwordEncoder) {
        UserRetriever userRetriever = new UserRetriever(userRepository);
        UserAdder userAdder = new UserAdder(userRepository, userRetriever);
        UserUpdater userUpdater = new UserUpdater(userRetriever, userRepository);
        RoleRetriever roleRetriever = new RoleRetriever(roleRepository);
        RoleAssigner roleAssigner = new RoleAssigner(userRetriever, roleRetriever, userRepository);
        UserMapper userMapper = new UserMapper(passwordEncoder);
        return new UserFacade(userRetriever, userAdder, userUpdater, roleRetriever, roleAssigner, userMapper);
    }
}
