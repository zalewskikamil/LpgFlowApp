package com.github.lpgflow.domain.warehouse;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
class AddressAssigner {

    private final AddressRetriever addressRetriever;
    private final WarehouseRetriever warehouseRetriever;

    Warehouse assignAddressToWarehouse(Long warehouseId, Long addressId, boolean activate) {
        Warehouse warehouse = warehouseRetriever.findById(warehouseId);
        Address address = addressRetriever.findAddressById(addressId);
        warehouse.setAddress(address);
        warehouse.setActive(activate);
        return warehouse;
    }
}
