package com.github.lpgflow.domain.order;

import com.github.lpgflow.domain.bdf.BdfFacade;
import com.github.lpgflow.domain.bdf.dto.response.BdfDto;
import com.github.lpgflow.domain.bdf.dto.response.GetBdfByIdDto;
import com.github.lpgflow.domain.warehouse.WarehouseFacade;
import com.github.lpgflow.domain.warehouse.dto.response.WarehouseDto;
import com.github.lpgflow.infrastructure.security.AuthenticatedUserProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor
class OrderParametersValidator {

    private final OrderAccessValidator orderAccessValidator;
    private final AuthenticatedUserProvider authenticatedUserProvider;
    private final BdfFacade bdfFacade;
    private final WarehouseFacade warehouseFacade;

    void validateForCreation(Order order) {
        String warehouseName = order.getWarehouseName();
        String currentUserEmail = authenticatedUserProvider.getCurrentUserName();
        Collection<Long> bdfIds = order.getBdfIds();
        Instant completionDate = order.getScheduledCompletionDate();
        validateWarehouseName(warehouseName);
        validateBdfIds(bdfIds, warehouseName, currentUserEmail);
        validateCompletionDate(completionDate);
    }

    private void validateWarehouseName(String warehouseName) {
        if (!orderAccessValidator.hasAccess(warehouseName)) {
            throw new OrderAccessException("No access to oder");
        }
        WarehouseDto warehouse = warehouseFacade.findWarehouseByName(warehouseName).warehouse();
        if (!warehouse.active()) {
            throw new OrderAccessException("Warehouse not active");
        }
    }

    private void validateBdfIds(Collection<Long> bdfIds, String warehouseName, String currentUserEmail) {
        if (bdfIds.size() > 2) {
            throw new OrderParameterException("BdfIds must contain at most 2 elements");
        }
        List<BdfDto> bdfDtos = bdfIds.stream()
                .map(bdfFacade::getBdfById)
                .map(GetBdfByIdDto::bdf)
                .toList();
        String bdfSizeAssignedToWarehouse = warehouseFacade.findWarehouseByName(warehouseName)
                .warehouse()
                .bdfSize();
        boolean areBdfIdsValid = bdfDtos.stream()
                .allMatch(bdf ->
                        bdf.createdBy().equals(currentUserEmail) &&
                            bdf.size().equals(bdfSizeAssignedToWarehouse) &&
                            !bdf.ordered());
        if (!areBdfIdsValid) {
            throw new OrderParameterException("Incorrect bdf ids");
        }
    }

    private void validateCompletionDate(Instant completionDate) {
        ZoneId zone = ZoneId.of("Europe/Warsaw");
        LocalDate targetDate = completionDate.atZone(zone).toLocalDate();
        LocalDate dayAfterTomorrow = LocalDate.now(zone).plusDays(2);
        if (targetDate.isBefore(dayAfterTomorrow)) {
            throw new OrderParameterException("Completion date must be at least the day after tomorrow");
        }
    }
}
