package com.github.lpgflow.infrastructure.error;

import com.github.lpgflow.domain.user.EmailAlreadyExistException;
import com.github.lpgflow.domain.user.RoleAlreadyAssignedToUserException;
import com.github.lpgflow.domain.user.RoleNotFoundException;
import com.github.lpgflow.domain.user.UserNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Log4j2
class ErrorHandler {

    @ExceptionHandler(EmailAlreadyExistException.class)
    public ResponseEntity<ErrorResponseDto> handleException(EmailAlreadyExistException exception) {
        log.warn("EmailAlreadyExistException while adding user");
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(new ErrorResponseDto(exception.getMessage(), status));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleException(UserNotFoundException exception) {
        log.warn("UserNotFoundException while accessing user");
        HttpStatus status = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(new ErrorResponseDto(exception.getMessage(), status));
    }

    @ExceptionHandler(RoleAlreadyAssignedToUserException.class)
    public ResponseEntity<ErrorResponseDto> handleException(RoleAlreadyAssignedToUserException exception) {
        log.warn("RoleAlreadyAssignedToUserException while assigning role to user");
        HttpStatus status = HttpStatus.CONFLICT;
        return ResponseEntity.status(status).body(new ErrorResponseDto(exception.getMessage(), status));
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleException(RoleNotFoundException exception) {
        log.warn("RoleAlreadyAssignedToUserException while accessing role");
        HttpStatus status = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(new ErrorResponseDto(exception.getMessage(), status));
    }
}
