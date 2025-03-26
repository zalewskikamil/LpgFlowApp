package com.github.lpgflow.domain.order;

import com.github.lpgflow.domain.bdf.BdfFacade;
import com.github.lpgflow.infrastructure.security.AuthenticatedUserProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;

@Component
@RequiredArgsConstructor
class OrderParametersValidator {

    private final OrderAccessValidator orderAccessValidator;
    private final AuthenticatedUserProvider authenticatedUserProvider;
    private final BdfFacade bdfFacade;

    void validateForCreation(Order order) {
        String warehouseName = order.getWarehouseName();
        String currentUserEmail = authenticatedUserProvider.getCurrentUserName();
        Collection<Long> bdfIds = order.getBdfIds();
        Instant completionDate = order.getScheduledCompletionDate();
        validateWarehouseName(warehouseName);
        validateBdfIds(bdfIds, currentUserEmail);
        validateCompletionDate(completionDate);
    }

    private void validateWarehouseName(String warehouseName) {
        if (!orderAccessValidator.hasAccess(warehouseName)) {
            throw new OrderAccessException("No access to oder");
        }
    }

    private void validateBdfIds(Collection<Long> bdfIds, String currentUserEmail) {
        if (bdfIds.size() > 2) {
            throw new OrderParameterException("BdfIds must contain at most 2 elements");
        }
        boolean areBdfIdsValid = bdfIds.stream()
                .map(bdfFacade::getBdfById)
                .map(response -> response.bdf().createdBy())
                .allMatch(creator -> creator.equals(currentUserEmail));
        if (!areBdfIdsValid) {
            throw new OrderParameterException("Incorrect bdf ids");
        }
    }

    private void validateCompletionDate(Instant completionDate) {
        ZoneId zone = ZoneId.of("Europe/Warsaw");
        LocalDate targetDate = completionDate.atZone(zone).toLocalDate();
        LocalDate dayAfterTomorrow = LocalDate.now(zone).plusDays(2);
        boolean isCompletionDateValid = !targetDate.isBefore(dayAfterTomorrow);
        if (!isCompletionDateValid) {
            throw new OrderParameterException("Completion date must be at least the day after tomorrow");
        }
    }
}
