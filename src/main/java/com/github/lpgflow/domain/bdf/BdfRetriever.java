package com.github.lpgflow.domain.bdf;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
class BdfRetriever {

    private final BdfRepository bdfRepository;

    Bdf findById(Long id) {
        return bdfRepository.findById(id)
                .orElseThrow(() -> new BdfNotFoundException("BDF with id " + id + " not found"));
    }

    Set<Bdf> findUnorderedBdfsByUserEmail(String userEmail) {
        return bdfRepository.findUnorderedBdfsCreatedBy(userEmail);
    }

    boolean existsById(Long id) {
        return false;
    }
}
