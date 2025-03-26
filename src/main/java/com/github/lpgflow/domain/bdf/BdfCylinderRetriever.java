package com.github.lpgflow.domain.bdf;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
class BdfCylinderRetriever {

    private final BdfCylinderRepository bdfCylinderRepository;

    boolean existsByBdfIdAndCylinderId(Long bdfId, Long cylinderId) {
        return bdfCylinderRepository.existsByIdBdfIdAndCylinderId(bdfId, cylinderId);
    }

    BdfCylinder findByIdBdfIdAndCylinderId(Long bdfId, Long cylinderId) {
        return bdfCylinderRepository.findByIdBdfIdAndCylinderId(bdfId, cylinderId).
                orElseThrow(() -> new BdfCylinderNotFound(
                        "Cylinders with id: " + cylinderId + " assigned to BDF with id: " + bdfId + " not found"));
    }

    Set<BdfCylinder> getBdfCylindersByBdfId(Long bdfId) {
        return bdfCylinderRepository.findByIdBdfId(bdfId);
    }
}
