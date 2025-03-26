package com.github.lpgflow.domain.warehouse;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
class WarehouseRetriever {

    private final WarehouseRepository warehouseRepository;

    List<Warehouse> getAllWarehouses(Pageable pageable) {
        return warehouseRepository.findAll(pageable);
    }

    Warehouse findById(Long id) {
        return warehouseRepository.findById(id)
                .orElseThrow(() -> new WarehouseNotFoundException("Warehouse with id: " + id + " not found"));
    }

    List<Warehouse> findByRegionalManagerEmail(String regionalManagerEmail) {
        return warehouseRepository.findByRegionalManagerEmail(regionalManagerEmail);
    }

    Warehouse findByWarehousemanEmail(String warehousemanEmail) {
        return warehouseRepository.findByWarehousemanEmail(warehousemanEmail)
                .orElseThrow(() -> new WarehouseNotFoundException(
                        "Warehouse with warehouseman email:  " + warehousemanEmail + " not found"));
    }

    Warehouse findByName(String name) {
        return warehouseRepository.findByName(name)
                .orElseThrow(() -> new WarehouseNotFoundException(
                        "Warehouse with name:  " + name + " not found"));
    }

    List<Warehouse> findByAddress(Address address) {
        return warehouseRepository.findByAddress(address);
    }
}
