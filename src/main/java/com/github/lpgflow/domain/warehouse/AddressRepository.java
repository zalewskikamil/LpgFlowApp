package com.github.lpgflow.domain.warehouse;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

interface AddressRepository extends Repository<Address, Long> {

    List<Address> findAll(Pageable pageable);

    Optional<Address> findById(Long id);

    Address save(Address address);

    @Modifying
    @Query("DELETE FROM Address a WHERE a.id = :id")
    void deleteById(Long id);
}
