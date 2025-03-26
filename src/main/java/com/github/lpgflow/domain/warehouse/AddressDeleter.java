package com.github.lpgflow.domain.warehouse;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
class AddressDeleter {

    private final AddressRepository addressRepository;
    private final AddressRetriever addressRetriever;
    private final WarehouseRetriever warehouseRetriever;

    @Transactional
    void deleteById(Long id) {
        Address addressById = addressRetriever.findAddressById(id);
        List<Warehouse> warehousesByAddress = warehouseRetriever.findByAddress(addressById);
        if (!warehousesByAddress.isEmpty()) {
            throw new AddressInUseException("Address with id: " + id + " is still in use");
        }
        log.info("Deleting address with id: {}", id);
        addressRepository.deleteById(id);
    }
}
