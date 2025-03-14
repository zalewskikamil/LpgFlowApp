package com.github.lpgflow.infrastructure.user.role;

import com.github.lpgflow.domain.user.UserFacade;
import com.github.lpgflow.domain.user.dto.response.GetAllRolesResponseDto;
import com.github.lpgflow.domain.user.dto.response.GetRoleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
class RoleController {

    private final UserFacade userFacade;

    @GetMapping
    public ResponseEntity<GetAllRolesResponseDto> getAllRoles() {
        GetAllRolesResponseDto response = userFacade.getAllRoles();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetRoleResponseDto> getRoleById(@PathVariable Long id) {
        GetRoleResponseDto response = userFacade.getRoleById(id);
        return ResponseEntity.ok(response);
    }
}
