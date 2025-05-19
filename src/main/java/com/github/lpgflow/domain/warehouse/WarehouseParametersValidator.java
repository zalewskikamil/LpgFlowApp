package com.github.lpgflow.domain.warehouse;

import com.github.lpgflow.domain.user.UserFacade;
import com.github.lpgflow.domain.util.enums.UserRole;
import com.github.lpgflow.domain.user.dto.response.RoleDto;
import com.github.lpgflow.domain.user.dto.response.UserWithDetailsDto;
import com.github.lpgflow.domain.util.enums.BdfSize;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
class WarehouseParametersValidator {

    private final UserFacade userFacade;
    private final WarehouseRetriever warehouseRetriever;

    void validateForCreation(Warehouse warehouse) {
        validateWarehouseName(warehouse.getName());
        validateRegionalManagerEmail(warehouse.getRegionalManagerEmail());
        validateWarehousemanEmail(warehouse.getWarehousemanEmail());
        validateMaxCylindersWithoutCollarPerBdf(warehouse.getBdfSize(), warehouse.getMaxCylindersWithoutCollarPerBdf());
    }

    void validateParametersForUpdate(String regionalManagerEmail,
                           String warehousemanEmail,
                           BdfSize bdfSize,
                           Integer maxCylindersWithoutCollarPerBdf) {
        if (regionalManagerEmail != null) {
            validateRegionalManagerEmail(regionalManagerEmail);
        }
        if (warehousemanEmail != null) {
            validateWarehousemanEmail(warehousemanEmail);
        }
        if (maxCylindersWithoutCollarPerBdf != null) {
            validateMaxCylindersWithoutCollarPerBdf(bdfSize, maxCylindersWithoutCollarPerBdf);
        }
    }

    private void validateWarehouseName(String warehouseName) {
        try {
            warehouseRetriever.findByName(warehouseName);
            throw new WarehouseParameterException("Name : " + warehouseName + " is not unique");
        } catch (WarehouseNotFoundException ignored) {}
    }

    private void validateRegionalManagerEmail(String email) {
        UserWithDetailsDto regionalManagerDetails = userFacade
                .findUserWithDetailsByEmail(email)
                .user();
        Set<RoleDto> regionalManagerRoles = regionalManagerDetails.roles();
        if (regionalManagerRoles.stream()
                .noneMatch(roleDto -> roleDto.name().equalsIgnoreCase(UserRole.REGIONAL_MANAGER.name()))) {
            throw new WarehouseParameterException("Email: " + email + " does not belong to regional manager");
        }
    }

    private void validateWarehousemanEmail(String email) {
        UserWithDetailsDto warehousemanDetails = userFacade
                .findUserWithDetailsByEmail(email)
                .user();
        Set<RoleDto> warehousemanRoles = warehousemanDetails.roles();
        if (warehousemanRoles.stream()
                .noneMatch(roleDto -> roleDto.name().equalsIgnoreCase(UserRole.WAREHOUSEMAN.name()))) {
            throw new WarehouseParameterException("Email: " + email + " does not belong to warehouseman");
        }
        try {
            warehouseRetriever.findByWarehousemanEmail(email);
            throw new WarehouseParameterException("Warehouseman with email: " + email +
                    " already has a warehouse assigned");
        } catch (WarehouseNotFoundException ignored) {}
    }

    private void validateMaxCylindersWithoutCollarPerBdf(BdfSize bdfSize, Integer maxCylindersWithoutCollarPerBdf) {
        int bdfSlots = bdfSize.getSlots();
        int maxPossibleCylindersWithoutCollarPerBdf = bdfSlots / 2;
        if (maxCylindersWithoutCollarPerBdf == null || maxCylindersWithoutCollarPerBdf < 0 ||
                maxCylindersWithoutCollarPerBdf > maxPossibleCylindersWithoutCollarPerBdf) {
            throw new WarehouseParameterException("MaxCylinderWithoutCollarPerBdf must be a number between 0 and "
                    + maxPossibleCylindersWithoutCollarPerBdf);
        }
    }
}
