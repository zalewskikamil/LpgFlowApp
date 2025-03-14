package com.github.lpgflow.domain.user;

public class RoleAlreadyAssignedToUserException extends RuntimeException {

    RoleAlreadyAssignedToUserException(String message) {
        super(message);
    }
}
