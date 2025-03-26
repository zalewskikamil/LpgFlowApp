package com.github.lpgflow.domain.warehouse;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
class AddressAdder {

    private final AddressRepository addressRepository;

    Address addAddress(final Address address) {
        log.info("Adding new address: {}, {} {}",
                address.getStreet(), address.getPostalCode(), address.getCity());
        return addressRepository.save(address);
    }
}
