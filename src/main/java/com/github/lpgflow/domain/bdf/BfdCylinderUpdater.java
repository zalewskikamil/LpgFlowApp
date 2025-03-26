package com.github.lpgflow.domain.bdf;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
class BfdCylinderUpdater {

    private final BdfCylinderRepository bdfCylinderRepository;
    private final BdfCylinderRetriever bdfCylinderRetriever;

    BdfCylinder update(BdfCylinder bdfCylinderFromRequest) {
        Long bdfId = bdfCylinderFromRequest.getBdf().getId();
        Long cylinderId = bdfCylinderFromRequest.getCylinder().getId();
        BdfCylinder bdfCylinderFromDatabase = bdfCylinderRetriever.findByIdBdfIdAndCylinderId(bdfId, cylinderId);
        bdfCylinderFromDatabase.setQuantity(bdfCylinderFromRequest.getQuantity());
        return bdfCylinderRepository.save(bdfCylinderFromDatabase);
    }
}
