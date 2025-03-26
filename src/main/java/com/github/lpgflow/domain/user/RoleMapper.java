package com.github.lpgflow.domain.user;

import com.github.lpgflow.domain.user.dto.response.RoleDto;
import com.github.lpgflow.domain.user.dto.response.GetAllRolesResponseDto;
import com.github.lpgflow.domain.user.dto.response.GetRoleResponseDto;

import java.util.List;
import java.util.stream.Collectors;

class RoleMapper {

    static RoleDto mapFromRoleToRoleDto(Role role) {
        return RoleDto.builder()
                .id(role.getId())
                .name(role.getName().name())
                .build();
    }

    static GetRoleResponseDto mapFromRoleToGetRoleResponseDto(Role role) {
        return GetRoleResponseDto.builder()
                .role(mapFromRoleToRoleDto(role))
                .build();
    }

    static GetAllRolesResponseDto mapFromListOfRolesToGetAllRolesResponseDto(List<Role> roles) {
        return GetAllRolesResponseDto.builder()
                .roles(roles.stream()
                        .map(RoleMapper::mapFromRoleToRoleDto)
                        .collect(Collectors.toList()))
                .build();
    }
}
