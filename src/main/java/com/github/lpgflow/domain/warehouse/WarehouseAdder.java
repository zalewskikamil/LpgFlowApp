package com.github.lpgflow.domain.warehouse;

import com.github.lpgflow.domain.util.BdfSize;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
class WarehouseAdder {

    private final WarehouseRepository warehouseRepository;
    private final WarehouseParametersValidator warehouseValidator;

    Warehouse addWarehouse(final Warehouse warehouse) {
        warehouseValidator.validateForCreation(warehouse);
        String regionalManagerEmail = warehouse.getRegionalManagerEmail();
        String warehousemanEmail = warehouse.getWarehousemanEmail();
        BdfSize bdfSize = warehouse.getBdfSize();
        int maxCylindersWithoutCollarPerBdf = warehouse.getMaxCylindersWithoutCollarPerBdf();
        log.info("Adding new warehouse: {}, {}, {}, {}, {}",
                warehouse.getName(), regionalManagerEmail, warehousemanEmail,
                bdfSize.name(), maxCylindersWithoutCollarPerBdf);
        return warehouseRepository.save(warehouse);
    }
}
