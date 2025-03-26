package com.github.lpgflow.domain.warehouse;

import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

class InMemoryWarehouseRepository implements WarehouseRepository {

    Map<Long, Warehouse> db = new HashMap<>();
    AtomicInteger index = new AtomicInteger(1);


    @Override
    public List<Warehouse> findAll(final Pageable pageable) {
        return new ArrayList<>(db.values());
    }

    @Override
    public Optional<Warehouse> findById(final Long id) {
        return db.values()
                .stream()
                .filter(warehouse -> warehouse.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<Warehouse> findByName(final String name) {
        return db.values()
                .stream()
                .filter(warehouse -> warehouse.getName().equals(name))
                .findFirst();
    }

    @Override
    public List<Warehouse> findByRegionalManagerEmail(final String regionalManagerEmail) {
        return db.values()
                .stream()
                .filter(warehouse -> warehouse.getRegionalManagerEmail().equals(regionalManagerEmail))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Warehouse> findByWarehousemanEmail(final String warehousemanEmail) {
        return db.values()
                .stream()
                .filter(warehouse -> warehouse.getWarehousemanEmail().equals(warehousemanEmail))
                .findFirst();
    }

    @Override
    public List<Warehouse> findByAddress(final Address address) {
        return db.values()
                .stream()
                .filter(warehouse -> warehouse.getAddress().equals(address))
                .collect(Collectors.toList());
    }

    @Override
    public Warehouse save(final Warehouse warehouse) {
        Long warehouseId = warehouse.getId();
        if (warehouseId != null && db.containsKey(warehouseId)) {
            db.put(warehouseId, warehouse);
        } else {
            long index = this.index.getAndIncrement();
            db.put(index, warehouse);
            warehouse.setId(index);
        }
        return warehouse;
    }

    @Override
    public void updateById(final Long id, final Warehouse newWarehouse, final long version) {
        Warehouse warehouse = db.get(id);
        warehouse.setRegionalManagerEmail(newWarehouse.getRegionalManagerEmail());
        warehouse.setWarehousemanEmail(newWarehouse.getWarehousemanEmail());
        warehouse.setActive(newWarehouse.getActive());
        warehouse.setMaxCylindersWithoutCollarPerBdf(newWarehouse.getMaxCylindersWithoutCollarPerBdf());
    }
}
