package com.github.lpgflow.domain.warehouse;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
class AddressRetriever {

    private final AddressRepository addressRepository;

    List<Address> getAllAddresses(Pageable pageable) {
        return addressRepository.findAll(pageable);
    }

    Address findAddressById(Long id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> new AddressNotFoundException("Address with id: " + id + " not found"));
    }
}
