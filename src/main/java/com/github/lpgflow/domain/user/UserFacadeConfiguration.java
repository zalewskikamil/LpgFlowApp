package com.github.lpgflow.domain.user;

class UserFacadeConfiguration {

    public static UserFacade createUserCrud(final UserRepository userRepository,
                                            final RoleRepository roleRepository) {
        UserRetriever userRetriever = new UserRetriever(userRepository);
        UserAdder userAdder = new UserAdder(userRepository, userRetriever);
        UserUpdater userUpdater = new UserUpdater(userRetriever, userRepository);
        RoleRetriever roleRetriever = new RoleRetriever(roleRepository);
        RoleAssigner roleAssigner = new RoleAssigner(userRetriever, roleRetriever, userRepository);
        return new UserFacade(userRetriever, userAdder, userUpdater, roleRetriever, roleAssigner);
    }
}
