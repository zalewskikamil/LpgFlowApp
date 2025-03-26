package com.github.lpgflow.domain.warehouse;


import com.github.lpgflow.domain.util.BdfSize;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Log4j2
@RequiredArgsConstructor
class WarehouseUpdater {

    private final WarehouseRepository warehouseRepository;
    private final WarehouseRetriever warehouseRetriever;
    private final WarehouseParametersValidator warehouseValidator;

    @Transactional
    void updatePartiallyById(Long warehouseId, Warehouse warehouseFromRequest) {
        Warehouse warehouseFromDatabase = warehouseRetriever.findById(warehouseId);
        validateParametersForUpdate(warehouseFromRequest, warehouseFromDatabase);
        Warehouse warehouseToSave = buildWarehouseToUpdate(warehouseFromRequest, warehouseFromDatabase);
        long version = warehouseFromDatabase.getVersion();
        warehouseRepository.updateById(warehouseId, warehouseToSave, version);
        log.info("Updating warehouse with id: {}. Regional manager email: {}, warehouseman email: {}, " +
                "active: {}, max cylinders without collar per bdf: {}",
                warehouseId, warehouseToSave.getRegionalManagerEmail(),
                warehouseToSave.getWarehousemanEmail(),
                warehouseToSave.getActive(),
                warehouseToSave.getMaxCylindersWithoutCollarPerBdf());
    }

    private void validateParametersForUpdate(Warehouse warehouseFromRequest, Warehouse warehouseFromDatabase) {
        String regionalManagerEmail = warehouseFromRequest.getRegionalManagerEmail();
        String warehousemanEmail = warehouseFromRequest.getWarehousemanEmail();
        BdfSize bdfSize = warehouseFromRequest.getBdfSize() != null ?
                warehouseFromRequest.getBdfSize() : warehouseFromDatabase.getBdfSize();
        Integer maxCylindersWithoutCollarPerBdf = warehouseFromRequest.getMaxCylindersWithoutCollarPerBdf();
        warehouseValidator.validateParametersForUpdate(regionalManagerEmail, warehousemanEmail,
                bdfSize, maxCylindersWithoutCollarPerBdf);
    }

    private Warehouse buildWarehouseToUpdate(Warehouse warehouseFromRequest, Warehouse warehouseFromDatabase) {
        Warehouse.WarehouseBuilder builder = Warehouse.builder();
        if (warehouseFromRequest.getRegionalManagerEmail() != null) {
            builder.regionalManagerEmail(warehouseFromRequest.getRegionalManagerEmail());
        } else {
            builder.regionalManagerEmail(warehouseFromDatabase.getRegionalManagerEmail());
        }
        if (warehouseFromRequest.getWarehousemanEmail() != null) {
            builder.warehousemanEmail(warehouseFromRequest.getWarehousemanEmail());
        } else {
            builder.warehousemanEmail(warehouseFromDatabase.getWarehousemanEmail());
        }
        if (warehouseFromRequest.getActive() != null) {
            builder.active(warehouseFromRequest.getActive());
        } else {
            builder.active(warehouseFromDatabase.getActive());
        }
        if (warehouseFromRequest.getMaxCylindersWithoutCollarPerBdf() != null) {
            builder.maxCylindersWithoutCollarPerBdf(warehouseFromRequest.getMaxCylindersWithoutCollarPerBdf());
        } else {
            builder.maxCylindersWithoutCollarPerBdf(warehouseFromDatabase.getMaxCylindersWithoutCollarPerBdf());
        }
        return builder.build();
    }
}
