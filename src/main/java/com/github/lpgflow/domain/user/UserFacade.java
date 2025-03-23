package com.github.lpgflow.domain.user;

import com.github.lpgflow.domain.user.dto.UserWithDetailsDto;
import com.github.lpgflow.domain.user.dto.request.CreateUserRequestDto;
import com.github.lpgflow.domain.user.dto.response.AssignRoleToUserResponseDto;
import com.github.lpgflow.domain.user.dto.response.CreateUserResponseDto;
import com.github.lpgflow.domain.user.dto.response.GetAllRolesResponseDto;
import com.github.lpgflow.domain.user.dto.response.GetAllUsersWithDetailsResponseDto;
import com.github.lpgflow.domain.user.dto.response.GetRoleResponseDto;
import com.github.lpgflow.domain.user.dto.response.GetUserResponseDto;
import com.github.lpgflow.domain.user.dto.response.GetUserWithDetailsResponseDto;
import com.github.lpgflow.domain.user.dto.response.UserForSecurityDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserFacade {

    private final UserRetriever userRetriever;
    private final UserAdder userAdder;
    private final UserUpdater userUpdater;
    private final RoleRetriever roleRetriever;
    private final RoleAssigner roleAssigner;
    private final UserMapper userMapper;

    public GetAllUsersWithDetailsResponseDto getAllUsersWithDetails(Pageable pageable) {
        List<User> users = userRetriever.findAll(pageable);
        return GetAllUsersWithDetailsResponseDto.builder()
                .users(users.stream()
                        .map(userMapper::mapFromUserToUserWithDetailsDto)
                        .collect(Collectors.toList()))
                .build();
    }

    public GetUserWithDetailsResponseDto findUserWithDetails(final Long id) {
        User userById =  userRetriever.findById(id);
        return GetUserWithDetailsResponseDto.builder()
                .user(userMapper.mapFromUserToUserWithDetailsDto(userById))
                .build();
    }

    public GetUserResponseDto findUserByEmail(String email) {
        User userByEmail = userRetriever.findByEmail(email);
        return userMapper.mapFromUserToGetUserResponseDto(userByEmail);
    }

    public Optional<UserForSecurityDto> findUserByEmailForSecurity(String email) {
        try {
            User userByEmail = userRetriever.findByEmail(email);
            return Optional.of(userMapper.mapFromUserToUserForSecurityDto(userByEmail));
        } catch (UserNotFoundException e) {
            return Optional.empty();
        }
    }

    public CreateUserResponseDto addUser(CreateUserRequestDto request) {
        User user = userMapper.mapFromCreateUserRequestDtoToUser(request);
        User savedUser = userAdder.addUser(user);
        return userMapper.mapFromUserToCreateUserRequestDto(savedUser);
    }

    public AssignRoleToUserResponseDto assignRoleToUser(Long userId, Long roleId) {
        User user = roleAssigner.assignRoleToUser(userId, roleId);
        UserWithDetailsDto userWithDetailsDto = userMapper.mapFromUserToUserWithDetailsDto(user);
        return new AssignRoleToUserResponseDto(userWithDetailsDto);
    }

    public boolean blockUser(final Long id) {
        return userUpdater.blockUser(id);
    }

    public boolean unblockUser(final Long id) {
        return userUpdater.unblockUser(id);
    }

    public boolean disableUser(final Long id) {
        return userUpdater.disableUser(id);
    }

    public boolean enableUser(final Long id) {
        return userUpdater.enableUser(id);
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
