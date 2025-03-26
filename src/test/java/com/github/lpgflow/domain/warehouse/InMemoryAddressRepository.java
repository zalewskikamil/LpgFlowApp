package com.github.lpgflow.domain.warehouse;

import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

class InMemoryAddressRepository implements AddressRepository {

    Map<Long, Address> db = new HashMap<>();
    AtomicInteger index = new AtomicInteger(1);

    @Override
    public List<Address> findAll(final Pageable pageable) {
        return new ArrayList<>(db.values());
    }

    @Override
    public Optional<Address> findById(final Long id) {
        return db.values()
                .stream()
                .filter(address -> address.getId().equals(id))
                .findFirst();
    }

    @Override
    public Address save(final Address address) {
        Long addressId = address.getId();
        if (addressId != null && db.containsKey(addressId)) {
            db.put(addressId, address);
        } else {
            long index = this.index.getAndIncrement();
            db.put(index, address);
            address.setId(index);
        }
        return address;
    }

    @Override
    public void deleteById(final Long id) {
        db.remove(id);
    }
}
