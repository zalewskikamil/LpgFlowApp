package com.github.lpgflow.infrastructure.error;

import com.github.lpgflow.domain.bdf.BdfDeleteException;
import com.github.lpgflow.domain.bdf.BdfNotFoundException;
import com.github.lpgflow.domain.order.OrderAccessException;
import com.github.lpgflow.domain.order.OrderCancellationException;
import com.github.lpgflow.domain.order.OrderParameterException;
import com.github.lpgflow.domain.user.EmailSendingException;
import com.github.lpgflow.domain.user.OtpNotFoundException;
import com.github.lpgflow.domain.user.ChangePasswordException;
import com.github.lpgflow.domain.user.ResetPasswordException;
import com.github.lpgflow.domain.user.UpdateUserException;
import com.github.lpgflow.domain.warehouse.AddressInUseException;
import com.github.lpgflow.domain.warehouse.AddressNotFoundException;
import com.github.lpgflow.domain.bdf.AssignCylindersToBdfParameterException;
import com.github.lpgflow.domain.bdf.BdfCylinderNotFound;
import com.github.lpgflow.domain.bdf.CylinderNotFoundException;
import com.github.lpgflow.domain.bdf.CylinderParameterException;
import com.github.lpgflow.domain.warehouse.WarehouseNotFoundException;
import com.github.lpgflow.domain.warehouse.WarehouseParameterException;
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
        HttpStatus status = HttpStatus.CONFLICT;
        return ResponseEntity.status(status).body(new ErrorResponseDto(exception.getMessage(), status));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleException(UserNotFoundException exception) {
        log.warn("UserNotFoundException while accessing user");
        HttpStatus status = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(new ErrorResponseDto(exception.getMessage(), status));
    }

    @ExceptionHandler(UpdateUserException.class)
    public ResponseEntity<ErrorResponseDto> handleException(UpdateUserException exception) {
        log.warn("UpdateUserException while updating user");
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(new ErrorResponseDto(exception.getMessage(), status));
    }

    @ExceptionHandler(ChangePasswordException.class)
    public ResponseEntity<ErrorResponseDto> handleException(ChangePasswordException exception) {
        log.warn("ChangePasswordException while updating user password");
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(new ErrorResponseDto(exception.getMessage(), status));
    }

    @ExceptionHandler(ResetPasswordException.class)
    public ResponseEntity<ErrorResponseDto> handleException(ResetPasswordException exception) {
        log.warn("ResetPasswordException while resetting user password");
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(new ErrorResponseDto(exception.getMessage(), status));
    }

    @ExceptionHandler(EmailSendingException.class)
    public ResponseEntity<ErrorResponseDto> handleException(EmailSendingException exception) {
        log.warn("EmailSendingException while sending email");
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(status).body(new ErrorResponseDto(exception.getMessage(), status));
    }

    @ExceptionHandler(OtpNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleException(OtpNotFoundException exception) {
        log.warn("OtpNotFoundException while retrieving otp");
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
        log.warn("RoleNotFoundException while accessing role");
        HttpStatus status = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(new ErrorResponseDto(exception.getMessage(), status));
    }

    @ExceptionHandler(AddressNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleException(AddressNotFoundException exception) {
        log.warn("AddressNotFoundException while accessing address");
        HttpStatus status = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(new ErrorResponseDto(exception.getMessage(), status));
    }

    @ExceptionHandler(AddressInUseException.class)
    public ResponseEntity<ErrorResponseDto> handleException(AddressInUseException exception) {
        log.warn("AddressInUseException while deleting address");
        HttpStatus status = HttpStatus.CONFLICT;
        return ResponseEntity.status(status).body(new ErrorResponseDto(exception.getMessage(), status));
    }

    @ExceptionHandler(WarehouseNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleException(WarehouseNotFoundException exception) {
        log.warn("WarehouseNotFoundException while accessing warehouse");
        HttpStatus status = HttpStatus.CONFLICT;
        return ResponseEntity.status(status).body(new ErrorResponseDto(exception.getMessage(), status));
    }

    @ExceptionHandler(WarehouseParameterException.class)
    public ResponseEntity<ErrorResponseDto> handleException(WarehouseParameterException exception) {
        log.warn("WarehouseParameterException while adding / updating warehouse");
        HttpStatus status = HttpStatus.CONFLICT;
        return ResponseEntity.status(status).body(new ErrorResponseDto(exception.getMessage(), status));
    }

    @ExceptionHandler(CylinderNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleException(CylinderNotFoundException exception) {
        log.warn("CylinderNotFoundException while retrieving cylinder");
        HttpStatus status = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(new ErrorResponseDto(exception.getMessage(), status));
    }

    @ExceptionHandler(CylinderParameterException.class)
    public ResponseEntity<ErrorResponseDto> handleException(CylinderParameterException exception) {
        log.warn("CylinderParameterException while adding cylinder");
        HttpStatus status = HttpStatus.CONFLICT;
        return ResponseEntity.status(status).body(new ErrorResponseDto(exception.getMessage(), status));
    }

    @ExceptionHandler(BdfNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleException(BdfNotFoundException exception) {
        log.warn("BdfNotFoundException while retrieving BDF");
        HttpStatus status = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(new ErrorResponseDto(exception.getMessage(), status));
    }

    @ExceptionHandler(AssignCylindersToBdfParameterException.class)
    public ResponseEntity<ErrorResponseDto> handleException(AssignCylindersToBdfParameterException exception) {
        log.warn("AssignCylindersToBdfParameterException while assigning cylinders to BDF");
        HttpStatus status = HttpStatus.CONFLICT;
        return ResponseEntity.status(status).body(new ErrorResponseDto(exception.getMessage(), status));
    }

    @ExceptionHandler(BdfCylinderNotFound.class)
    public ResponseEntity<ErrorResponseDto> handleException(BdfCylinderNotFound exception) {
        log.warn("BdfCylinderNotFoundException while retrieving cylinders assigned to BDF");
        HttpStatus status = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(new ErrorResponseDto(exception.getMessage(), status));
    }
    @ExceptionHandler(BdfDeleteException.class)
    public ResponseEntity<ErrorResponseDto> handleException(BdfDeleteException exception) {
        log.warn("BdfDeleteException while deleting BDF");
        HttpStatus status = HttpStatus.CONFLICT;
        return ResponseEntity.status(status).body(new ErrorResponseDto(exception.getMessage(), status));
    }

    @ExceptionHandler(OrderCancellationException.class)
    public ResponseEntity<ErrorResponseDto> handleException(OrderCancellationException exception) {
        log.warn("OrderCancellationException while cancelling order");
        HttpStatus status = HttpStatus.CONFLICT;
        return ResponseEntity.status(status).body(new ErrorResponseDto(exception.getMessage(), status));
    }

    @ExceptionHandler(OrderAccessException.class)
    public ResponseEntity<ErrorResponseDto> handleException(OrderAccessException exception) {
        log.warn("OrderAccessException while adding / retrieving / updating order");
        HttpStatus status = HttpStatus.FORBIDDEN;
        return ResponseEntity.status(status).body(new ErrorResponseDto(exception.getMessage(), status));
    }
    @ExceptionHandler(OrderParameterException.class)
    public ResponseEntity<ErrorResponseDto> handleException(OrderParameterException exception) {
        log.warn("OrderParameterException while adding / updating order");
        HttpStatus status = HttpStatus.CONFLICT;
        return ResponseEntity.status(status).body(new ErrorResponseDto(exception.getMessage(), status));
    }
}
