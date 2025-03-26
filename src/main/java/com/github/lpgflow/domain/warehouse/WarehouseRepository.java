package com.github.lpgflow.domain.warehouse;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

interface WarehouseRepository extends Repository<Warehouse, Long> {

    List<Warehouse> findAll(Pageable pageable);

    Optional<Warehouse> findById(Long id);

    @Query("SELECT w FROM Warehouse w WHERE w.name = :name")
    Optional<Warehouse> findByName(String name);

    @Query("SELECT w FROM Warehouse w WHERE w.regionalManagerEmail = :regionalManagerEmail")
    List<Warehouse> findByRegionalManagerEmail(String regionalManagerEmail);

    @Query("SELECT w FROM Warehouse w WHERE w.warehousemanEmail = :warehousemanEmail")
    Optional<Warehouse> findByWarehousemanEmail(String warehousemanEmail);

    @Query("SELECT w FROM Warehouse w WHERE w.address = :address")
    List<Warehouse> findByAddress(Address address);

    Warehouse save(Warehouse warehouse);

    @Modifying(clearAutomatically = true)
    @Query("""
            UPDATE Warehouse w SET w.regionalManagerEmail = :#{#newWarehouse.regionalManagerEmail},
            w.warehousemanEmail = :#{#newWarehouse.warehousemanEmail},
            w.active = :#{#newWarehouse.active},
            w.maxCylindersWithoutCollarPerBdf = :#{#newWarehouse.maxCylindersWithoutCollarPerBdf},
            w.version = w.version + 1
            WHERE w.id = :id AND w.version = :version
            """)
    void updateById(Long id, Warehouse newWarehouse, long version);
}
