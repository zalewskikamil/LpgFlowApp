package com.github.lpgflow.infrastructure.user.user;

import com.github.lpgflow.domain.user.UserFacade;
import com.github.lpgflow.domain.user.dto.request.CreateUserRequestDto;
import com.github.lpgflow.domain.user.dto.request.UpdatePasswordRequestDto;
import com.github.lpgflow.domain.user.dto.request.UpdateUserPartiallyRequestDto;
import com.github.lpgflow.domain.user.dto.response.AssignRoleToUserResponseDto;
import com.github.lpgflow.domain.user.dto.response.CreateUserResponseDto;
import com.github.lpgflow.domain.user.dto.response.GetAllUsersWithDetailsResponseDto;
import com.github.lpgflow.domain.user.dto.response.GetUserResponseDto;
import com.github.lpgflow.domain.user.dto.response.GetUserWithDetailsResponseDto;
import com.github.lpgflow.domain.user.dto.response.UpdateUserPartiallyResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserFacade userFacade;

    @GetMapping
    public ResponseEntity<GetAllUsersWithDetailsResponseDto> getAllUsers(
            @PageableDefault(page = 0, size = 10) Pageable pageable) {
        GetAllUsersWithDetailsResponseDto response = userFacade.getAllUsersWithDetails(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetUserWithDetailsResponseDto> getUserById(@PathVariable Long id) {
        GetUserWithDetailsResponseDto response = userFacade.findUserWithDetails(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<GetUserResponseDto> getUserByEmail(@PathVariable String email) {
        GetUserResponseDto response = userFacade.findUserByEmail(email);
        return ResponseEntity.ok(response);
    }


    @PostMapping
    public ResponseEntity<CreateUserResponseDto> postUser(@RequestBody @Valid CreateUserRequestDto request) {
        CreateUserResponseDto body = userFacade.addUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @PutMapping("/{userId}/role/{roleId}")
    public ResponseEntity<AssignRoleToUserResponseDto> assignRoleToUser(@PathVariable Long userId,
                                                                        @PathVariable Long roleId) {
        AssignRoleToUserResponseDto body = userFacade.assignRoleToUser(userId, roleId);
        return ResponseEntity.ok(body);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UpdateUserPartiallyResponseDto> updateUserPartially(@PathVariable Long id,
                                                                              UpdateUserPartiallyRequestDto request) {
        UpdateUserPartiallyResponseDto response = userFacade.updateUserPartiallyById(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/password")
    public ResponseEntity<Void> updateUserPartially(
            @Valid UpdatePasswordRequestDto request) {
        userFacade.updateUserPassword(request);
        return ResponseEntity.ok().build();
    }
}
