package com.github.lpgflow.domain.user;

import com.github.lpgflow.domain.util.messagesender.EmailSender;
import com.github.lpgflow.infrastructure.security.AuthenticatedUserProvider;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Clock;

class UserFacadeConfiguration {

    public static UserFacade createUserCrud(final UserRepository userRepository,
                                            final RoleRepository roleRepository,
                                            final PasswordEncoder passwordEncoder,
                                            final AuthenticatedUserProvider authenticatedUserProvider,
                                            final OtpRepository otpRepository,
                                            final EmailSender emailSender,
                                            final Clock clock) {
        UserRetriever userRetriever = new UserRetriever(userRepository);
        UserAdder userAdder = new UserAdder(userRepository, userRetriever, passwordEncoder);
        UserUpdater userUpdater = new UserUpdater(userRetriever, userRepository);
        RoleRetriever roleRetriever = new RoleRetriever(roleRepository);
        RoleAssigner roleAssigner = new RoleAssigner(userRetriever, roleRetriever, userRepository);
        OtpRetriever otpRetriever = new OtpRetriever(otpRepository);
        PasswordUpdater passwordUpdater = new PasswordUpdater(
                userRetriever, userRepository, passwordEncoder, authenticatedUserProvider, otpRetriever, otpRepository);
        OtpGenerator otpGenerator = new OtpGenerator(clock);
        PasswordResetOtpSender passwordResetOtpSender = new PasswordResetOtpSender(
                userRetriever, otpGenerator, otpRepository, emailSender);
        return new UserFacade(userRetriever, userAdder, userUpdater, roleRetriever, roleAssigner,
                passwordUpdater, passwordResetOtpSender);
    }
}
