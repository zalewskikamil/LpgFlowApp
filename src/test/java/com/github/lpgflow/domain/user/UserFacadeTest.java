package com.github.lpgflow.domain.user;

import com.github.lpgflow.domain.user.dto.request.UpdatePasswordRequestDto;
import com.github.lpgflow.domain.user.dto.request.UpdateUserPartiallyRequestDto;
import com.github.lpgflow.domain.user.dto.response.RoleDto;
import com.github.lpgflow.domain.user.dto.response.UpdateUserPartiallyResponseDto;
import com.github.lpgflow.domain.user.dto.response.UserDto;
import com.github.lpgflow.domain.user.dto.response.UserWithDetailsDto;
import com.github.lpgflow.domain.user.dto.request.CreateUserRequestDto;
import com.github.lpgflow.domain.user.dto.response.AssignRoleToUserResponseDto;
import com.github.lpgflow.domain.user.dto.response.CreateUserResponseDto;
import com.github.lpgflow.domain.user.dto.response.GetAllRolesResponseDto;
import com.github.lpgflow.domain.user.dto.response.GetAllUsersWithDetailsResponseDto;
import com.github.lpgflow.domain.user.dto.response.GetRoleResponseDto;
import com.github.lpgflow.domain.user.dto.response.GetUserResponseDto;
import com.github.lpgflow.domain.user.dto.response.GetUserWithDetailsResponseDto;
import com.github.lpgflow.infrastructure.security.AuthenticatedUserProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.when;

class UserFacadeTest {

    AuthenticatedUserProvider authenticatedUserProvider = Mockito.mock(AuthenticatedUserProvider.class);

    UserFacade userFacade = UserFacadeConfiguration.createUserCrud(
            new InMemoryUserRepository(),
            new InMemoryRoleRepository(),
            NoOpPasswordEncoder.getInstance(),
            authenticatedUserProvider);

    @Test
    @DisplayName("Should return 2 users with details")
    public void should_return_two_users_with_details() {
        // given
        assertThat(userFacade.getAllUsersWithDetails(Pageable.unpaged()).users()).isEmpty();
        String firstUserEmail = "test1@test.com";
        String firstUsername = "User1";
        CreateUserRequestDto firstRequest = CreateUserRequestDto.builder()
                .name(firstUsername)
                .email(firstUserEmail)
                .password("password")
                .build();
        Long firstUserId = userFacade.addUser(firstRequest)
                .user().id();
        Long adminRoleId = 1L;
        userFacade.assignRoleToUser(firstUserId, adminRoleId);
        String secondUserEmail = "test2@test.com";
        String secondUsername = "User1";
        CreateUserRequestDto secondRequest = CreateUserRequestDto.builder()
                .name(secondUserEmail)
                .email(secondUsername)
                .password("password")
                .build();
        Long secondUserId = userFacade.addUser(secondRequest)
                .user().id();
        Long plannerRoleId = 2L;
        userFacade.assignRoleToUser(secondUserId, plannerRoleId);
        // when
        GetAllUsersWithDetailsResponseDto result = userFacade.getAllUsersWithDetails(Pageable.unpaged());
        // then
        assertThat(result.users()).hasSize(2);
        assertThat(result.users()).contains(UserWithDetailsDto.builder()
                .id(firstUserId)
                .name(firstUsername)
                .email(firstUserEmail)
                .enabled(true)
                .blocked(false)
                .roles(Set.of(new RoleDto(adminRoleId, "ADMIN")))
                .build()
        );
    }

    @Test
    @DisplayName("Should return user by id When id was exist in db")
    public void should_return_user_by_id_when_id_was_exist_in_db() {
        // given
        assertThat(userFacade.getAllUsersWithDetails(Pageable.unpaged()).users()).isEmpty();
        String email = "test@test.com";
        String username = "User1";
        CreateUserRequestDto request = CreateUserRequestDto.builder()
                .name(username)
                .email(email)
                .password("password")
                .build();
        CreateUserResponseDto createUserResponseDto = userFacade.addUser(request);
        Long userId = createUserResponseDto.user().id();
        Long roleId = 1L;
        userFacade.assignRoleToUser(userId, roleId);
        // when
        GetUserWithDetailsResponseDto result = userFacade.findUserWithDetails(userId);
        // then
        assertThat(result).isEqualTo(new GetUserWithDetailsResponseDto(
                UserWithDetailsDto.builder()
                        .id(userId)
                        .name(username)
                        .email(email)
                        .enabled(true)
                        .blocked(false)
                        .roles(Set.of(new RoleDto(roleId, "ADMIN")))
                        .build()
        ));
    }

    @Test
    @DisplayName("Should throw exception UserNotFound When id was not exist in db")
    public void should_throw_exception_user_not_found_when_id_was_not_exist_in_db() {
        // given
        assertThat(userFacade.getAllUsersWithDetails(Pageable.unpaged()).users()).isEmpty();
        Long userId = 1L;
        // when
        Throwable throwable = catchThrowable(() -> userFacade.findUserWithDetails(userId));
        // then
        assertThat(throwable).isInstanceOf(UserNotFoundException.class);
        assertThat(throwable.getMessage()).isEqualTo("User with id: " + userId + " not found");
    }

    @Test
    @DisplayName("Should return user by email When email was exist in db")
    public void should_return_user_by_email_when_email_was_exist_in_db() {
        // given
        assertThat(userFacade.getAllUsersWithDetails(Pageable.unpaged()).users()).isEmpty();
        String email = "test@test.com";
        String username = "User1";
        CreateUserRequestDto request = CreateUserRequestDto.builder()
                .name(username)
                .email(email)
                .password("password")
                .build();
        userFacade.addUser(request);
        // when
        GetUserResponseDto result = userFacade.findUserByEmail(email);
        // then
        UserDto userDto = UserDto.builder()
                .id(1L)
                .name(username)
                .email(email)
                .build();
        assertThat(result).isEqualTo(new GetUserResponseDto(userDto));
    }

    @Test
    @DisplayName("Should throw exception UserNotFound When email was not exist in db")
    public void should_throw_exception_user_not_found_when_email_was_not_exist_in_db() {
        // given
        assertThat(userFacade.getAllUsersWithDetails(Pageable.unpaged()).users()).isEmpty();
        String email = "test@test.com";
        // when
        Throwable throwable = catchThrowable(() -> userFacade.findUserByEmail(email));
        // then
        assertThat(throwable).isInstanceOf(UserNotFoundException.class);
        assertThat(throwable.getMessage()).isEqualTo("User with email: " + email + " not found");
    }

    @Test
    @DisplayName("Should add user with id: 1")
    public void should_add_user_with_id_one() {
        // given
        assertThat(userFacade.getAllUsersWithDetails(Pageable.unpaged()).users()).isEmpty();
        String username = "User1";
        CreateUserRequestDto request = CreateUserRequestDto.builder()
                .name(username)
                .password("password")
                .build();
        // when
        CreateUserResponseDto result = userFacade.addUser(request);
        // then
        UserDto userDto = UserDto.builder()
                .id(1L)
                .name(username)
                .build();
        assertThat(result).isEqualTo(new CreateUserResponseDto(userDto));
        assertThat(userFacade.getAllUsersWithDetails(Pageable.unpaged()).users()).hasSize(1);
    }

    @Test
    @DisplayName("Should throw exception EmailAlreadyExists When another user already had this email")
    public void should_throw_exception_email_already_exist_when_another_user_already_had_this_email() {
        // given
        assertThat(userFacade.getAllUsersWithDetails(Pageable.unpaged()).users()).isEmpty();
        String email = "test@test.com";
        CreateUserRequestDto firstUserRequest = CreateUserRequestDto.builder()
                .name("User1")
                .email(email)
                .password("password")
                .build();
        userFacade.addUser(firstUserRequest);
        CreateUserRequestDto secondUserRequest = CreateUserRequestDto.builder()
                .name("User2")
                .email(email)
                .password("password")
                .build();
        // when
        Throwable throwable = catchThrowable(() -> userFacade.addUser(secondUserRequest));
        // then
        assertThat(throwable).isInstanceOf(EmailAlreadyExistException.class);
        assertThat(throwable.getMessage()).isEqualTo("User with email: " + email + " already exists");
    }

    @Test
    @DisplayName("Should assign role to user When role id was exist")
    public void should_assign_role_to_user_when_role_id_was_exist() {
        // given
        assertThat(userFacade.getAllUsersWithDetails(Pageable.unpaged()).users()).isEmpty();
        String email = "test@test.com";
        CreateUserRequestDto firstUserRequest = CreateUserRequestDto.builder()
                .name("User1")
                .email(email)
                .password("password")
                .build();
        Long userId = userFacade.addUser(firstUserRequest).user().id();
        assertThat(userFacade.findUserWithDetails(userId).user().roles()).isEmpty();
        Long plannerRoleId = 2L;
        // when
        AssignRoleToUserResponseDto result = userFacade.assignRoleToUser(userId, plannerRoleId);
        // then
        assertThat(result).isEqualTo(AssignRoleToUserResponseDto.builder()
                .user(UserWithDetailsDto.builder()
                        .id(userId)
                        .name("User1")
                        .email(email)
                        .enabled(true)
                        .blocked(false)
                        .roles(Set.of(new RoleDto(plannerRoleId, "PLANNER")))
                        .build())
                .build());
        assertThat(result.user().roles()).hasSize(1);
    }

    @Test
    @DisplayName("Should throw exception UserNotFound during assignment role When user id was not exist")
    public void should_throw_exception_user_not_found_during_assignment_role_when_user_id_was_not_exist() {
        // given
        assertThat(userFacade.getAllUsersWithDetails(Pageable.unpaged()).users()).isEmpty();
        // when
        Long notExistingUserId = 1L;
        Throwable throwable = catchThrowable(() -> userFacade.assignRoleToUser(notExistingUserId, 1L));
        // then
        assertThat(throwable).isInstanceOf(UserNotFoundException.class);
        assertThat(throwable.getMessage()).isEqualTo("User with id: " + notExistingUserId + " not found");
        assertThat(userFacade.getAllUsersWithDetails(Pageable.unpaged()).users()).isEmpty();
    }

    @Test
    @DisplayName("Should throw exception RoleNotFound during assignment role When role id was not exist")
    public void should_throw_exception_role_not_found_during_assignment_role_when_role_id_was_not_exist() {
        // given
        assertThat(userFacade.getAllUsersWithDetails(Pageable.unpaged()).users()).isEmpty();
        CreateUserRequestDto userRequest = CreateUserRequestDto.builder()
                .name("User1")
                .email("test@test.com")
                .password("password")
                .build();
        Long userId = userFacade.addUser(userRequest).user().id();
        assertThat(userFacade.findUserWithDetails(userId).user().roles()).isEmpty();
        // when
        Long notExistingRoleId = 10L;
        Throwable throwable = catchThrowable(() -> userFacade.assignRoleToUser(userId, notExistingRoleId));
        // then
        assertThat(throwable).isInstanceOf(RoleNotFoundException.class);
        assertThat(throwable.getMessage()).isEqualTo("Role with id: " + notExistingRoleId + " not found");
        assertThat(userFacade.findUserWithDetails(userId).user().roles()).isEmpty();
    }

    @Test
    @DisplayName("Should throw exception RoleAlreadyAssignedToUser When role was already assigned")
    public void should_throw_exception_role_already_assigned_to_user_when_role_was_already_assigned() {
        // given
        assertThat(userFacade.getAllUsersWithDetails(Pageable.unpaged()).users()).isEmpty();
        String email = "test@test.com";
        CreateUserRequestDto firstUserRequest = CreateUserRequestDto.builder()
                .name("User1")
                .email(email)
                .password("password")
                .build();
        Long userId = userFacade.addUser(firstUserRequest).user().id();
        assertThat(userFacade.findUserWithDetails(userId).user().roles()).isEmpty();
        Long adminRoleId = 1L;
        userFacade.assignRoleToUser(userId, 1L);
        // when
        Throwable throwable = catchThrowable(() -> userFacade.assignRoleToUser(userId, adminRoleId));
        // then
        assertThat(throwable).isInstanceOf(RoleAlreadyAssignedToUserException.class);
        assertThat(throwable.getMessage()).isEqualTo(
                "Role ADMIN is already assigned to user with id: " + userId);
        assertThat(userFacade.findUserWithDetails(userId).user().roles()).hasSize(1);
    }

    @Test
    @DisplayName("Should update user partially")
    public void should_update_user_partially() {
        // given
        CreateUserRequestDto dto = CreateUserRequestDto.builder()
                .name("User1")
                .password("password")
                .build();
        Long userId = userFacade.addUser(dto)
                .user().id();
        assertThat(userFacade.findUserWithDetails(userId).user().enabled()).isTrue();
        assertThat(userFacade.findUserWithDetails(userId).user().blocked()).isFalse();
        UpdateUserPartiallyRequestDto request = UpdateUserPartiallyRequestDto.builder()
                .enabled(false)
                .blocked(true)
                .build();
        // when
        UpdateUserPartiallyResponseDto result = userFacade.updateUserPartiallyById(userId, request);
        // then
        UserWithDetailsDto user = UserWithDetailsDto.builder()
                .id(userId)
                .name("User1")
                .enabled(false)
                .blocked(true)
                .roles(Set.of())
                .build();
        UpdateUserPartiallyResponseDto response = UpdateUserPartiallyResponseDto.builder()
                .user(user)
                .build();
        assertThat(result).isEqualTo(response);
        assertThat(userFacade.findUserWithDetails(userId).user().enabled()).isFalse();
        assertThat(userFacade.findUserWithDetails(userId).user().blocked()).isTrue();
    }

    @Test
    @DisplayName("Should throw UpdateUserException When no user parameters was sending")
    public void should_throw_user_update_exception_when_no_user_parameters_was_sending() {
        // given
        CreateUserRequestDto dto = CreateUserRequestDto.builder()
                .name("User1")
                .password("password")
                .build();
        Long userId = userFacade.addUser(dto)
                .user().id();
        UpdateUserPartiallyRequestDto request = UpdateUserPartiallyRequestDto.builder().build();
        // when
        Throwable throwable = catchThrowable(() -> userFacade.updateUserPartiallyById(userId, request));
        // then
        assertThat(throwable).isInstanceOf(UpdateUserException.class);
        assertThat(throwable.getMessage()).isEqualTo(
                "No user parameters to change");
    }

    @Test
    @DisplayName("Should throw UpdatePasswordException When new password was not matched")
    public void should_throw_update_password_exception_when_new_password_was_not_matched() {
        // given
        String password = "password";
        String email = "test@test.pl";
        CreateUserRequestDto dto = CreateUserRequestDto.builder()
                .name("User1")
                .email(email)
                .password(password)
                .build();
        UpdatePasswordRequestDto request = UpdatePasswordRequestDto.builder()
                .actualPassword(password)
                .newPassword("newPassword")
                .confirmNewPassword("anotherPassword")
                .build();
        when(authenticatedUserProvider.getCurrentUserName()).thenReturn(email);
        // when
        Throwable throwable = catchThrowable(() -> userFacade.updateUserPassword(request));
        // then
        assertThat(throwable).isInstanceOf(UpdatePasswordException.class);
        assertThat(throwable.getMessage()).isEqualTo("New password does not match");
    }

    @Test
    @DisplayName("Should throw UpdatePasswordException When actual password was the same as the new password")
    public void should_throw_update_password_exception_when_actual_password_was_the_same_as_the_new_password() {
        // given
        String password = "password";
        String email = "test@test.pl";
        CreateUserRequestDto dto = CreateUserRequestDto.builder()
                .name("User1")
                .email(email)
                .password(password)
                .build();
        UpdatePasswordRequestDto request = UpdatePasswordRequestDto.builder()
                .actualPassword(password)
                .newPassword(password)
                .confirmNewPassword(password)
                .build();
        when(authenticatedUserProvider.getCurrentUserName()).thenReturn(email);
        // when
        Throwable throwable = catchThrowable(() -> userFacade.updateUserPassword(request));
        // then
        assertThat(throwable).isInstanceOf(UpdatePasswordException.class);
        assertThat(throwable.getMessage()).isEqualTo("The new password must be different from the current one");
    }

    @Test
    @DisplayName("Should throw UpdatePasswordException When actual password was not matched")
    public void should_throw_update_password_exception_when_actual_password_was_not_matched() {
        // given
        String password = "password";
        String email = "test@test.pl";
        CreateUserRequestDto dto = CreateUserRequestDto.builder()
                .name("User1")
                .email(email)
                .password(password)
                .build();
        Long userId = userFacade.addUser(dto)
                .user().id();
        String newPassword = "newPassword";
        UpdatePasswordRequestDto request = UpdatePasswordRequestDto.builder()
                .actualPassword("wrongActualPassword")
                .newPassword(newPassword)
                .confirmNewPassword(newPassword)
                .build();
        when(authenticatedUserProvider.getCurrentUserName()).thenReturn(email);
        // when
        Throwable throwable = catchThrowable(() -> userFacade.updateUserPassword(request));
        // then
        assertThat(throwable).isInstanceOf(UpdatePasswordException.class);
        assertThat(throwable.getMessage()).isEqualTo("Wrong actual password");
    }

    @Test
    @DisplayName("Should return two roles")
    public void should_return_two_roles() {
        // given
        // when
        GetAllRolesResponseDto response = userFacade.getAllRoles();
        // then
        assertThat(response).isEqualTo(new GetAllRolesResponseDto(List.of(
                RoleDto.builder()
                        .id(1L)
                        .name("ADMIN")
                        .build(),
                RoleDto.builder()
                        .id(2L)
                        .name("PLANNER")
                        .build()))
        );
    }

    @Test
    @DisplayName("Should return role by id When role id exists")
    public void should_return_role_by_id_when_role_id_exists() {
        // given
        Long roleId = 1L;
        // when
        GetRoleResponseDto response = userFacade.getRoleById(roleId);
        // then
        assertThat(response).isEqualTo(new GetRoleResponseDto(
                RoleDto.builder()
                        .id(roleId)
                        .name("ADMIN")
                        .build()
        ));
    }

    @Test
    @DisplayName("Should throw exception RoleNotFound When role id not exist")
    public void should_throw_exception_role_not_found_when_role_id_not_exist() {
        // given
        Long roleId = 100L;
        // when
        Throwable throwable = catchThrowable(() -> userFacade.getRoleById(roleId));
        // then
        assertThat(throwable).isInstanceOf(RoleNotFoundException.class);
        assertThat(throwable.getMessage()).isEqualTo("Role with id: " + roleId + " not found");
    }
}