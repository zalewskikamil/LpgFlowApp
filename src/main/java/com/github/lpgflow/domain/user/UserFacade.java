package com.github.lpgflow.domain.user;

import com.github.lpgflow.domain.user.dto.request.CreateUserRequestDto;
import com.github.lpgflow.domain.user.dto.request.UpdatePasswordRequestDto;
import com.github.lpgflow.domain.user.dto.request.UpdateUserPartiallyRequestDto;
import com.github.lpgflow.domain.user.dto.response.AssignRoleToUserResponseDto;
import com.github.lpgflow.domain.user.dto.response.CreateUserResponseDto;
import com.github.lpgflow.domain.user.dto.response.GetAllRolesResponseDto;
import com.github.lpgflow.domain.user.dto.response.GetAllUsersWithDetailsResponseDto;
import com.github.lpgflow.domain.user.dto.response.GetRoleResponseDto;
import com.github.lpgflow.domain.user.dto.response.GetUserResponseDto;
import com.github.lpgflow.domain.user.dto.response.GetUserWithDetailsResponseDto;
import com.github.lpgflow.domain.user.dto.response.UpdateUserPartiallyResponseDto;
import com.github.lpgflow.domain.user.dto.response.UserForSecurityDto;
import com.github.lpgflow.domain.user.dto.response.UserWithDetailsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserFacade {

    private final UserRetriever userRetriever;
    private final UserAdder userAdder;
    private final UserUpdater userUpdater;
    private final RoleRetriever roleRetriever;
    private final RoleAssigner roleAssigner;

    public GetAllUsersWithDetailsResponseDto getAllUsersWithDetails(Pageable pageable) {
        List<User> users = userRetriever.findAll(pageable);
        return UserMapper.mapFromListUsersToGetAllUsersWithDetailsResponseDto(users);
    }

    public GetUserWithDetailsResponseDto findUserWithDetails(Long id) {
        User userById =  userRetriever.findById(id);
        return UserMapper.mapFromUserToGetUserWithDetailsResponseDto(userById);
    }

    public GetUserWithDetailsResponseDto findUserWithDetailsByEmail(String email) {
        User userByEmail = userRetriever.findByEmail(email);
        return UserMapper.mapFromUserToGetUserWithDetailsResponseDto(userByEmail);
    }

    public GetUserResponseDto findUserByEmail(String email) {
        User userByEmail = userRetriever.findByEmail(email);
        return UserMapper.mapFromUserToGetUserResponseDto(userByEmail);
    }

    public Optional<UserForSecurityDto> findUserByEmailForSecurity(String email) {
        try {
            User userByEmail = userRetriever.findByEmail(email);
            return Optional.of(UserMapper.mapFromUserToUserForSecurityDto(userByEmail));
        } catch (UserNotFoundException e) {
            return Optional.empty();
        }
    }

    public CreateUserResponseDto addUser(CreateUserRequestDto request) {
        User user = UserMapper.mapFromCreateUserRequestDtoToUser(request);
        User savedUser = userAdder.addUser(user);
        return UserMapper.mapFromUserToCreateUserResponseDto(savedUser);
    }

    public AssignRoleToUserResponseDto assignRoleToUser(Long userId, Long roleId) {
        User user = roleAssigner.assignRoleToUser(userId, roleId);
        UserWithDetailsDto userWithDetailsDto = UserMapper.mapFromUserToUserWithDetailsDto(user);
        return new AssignRoleToUserResponseDto(userWithDetailsDto);
    }

    public UpdateUserPartiallyResponseDto updateUserPartiallyById(Long id, UpdateUserPartiallyRequestDto request) {
        User userFromRequest = UserMapper.mapFromUpdateUserPartiallyRequestDtoToUser(request);
        User updatedUser = userUpdater.updatePartiallyById(id, userFromRequest);
        return UserMapper.mapFromUserToUpdateUserPartiallyResponseDto(updatedUser);
    }

    public void updateUserPassword(UpdatePasswordRequestDto request) {
        userUpdater.updatePassword(request);
    }

    public GetAllRolesResponseDto getAllRoles() {
        List<Role> roles = roleRetriever.getAllRoles();
        return RoleMapper.mapFromListOfRolesToGetAllRolesResponseDto(roles);
    }

    public GetRoleResponseDto getRoleById(final Long id) {
        Role roleById = roleRetriever.getRoleById(id);
        return RoleMapper.mapFromRoleToGetRoleResponseDto(roleById);
    }
}
