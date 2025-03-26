package com.github.lpgflow.domain.bdf;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
class BdfDeleter {

    private final BdfRepository bdfRepository;
    private final BdfRetriever bdfRetriever;
    private final BdfCylinderDeleter bdfCylinderDeleter;

    void deleteUnorderedBdfById(Long bdfId, String currentUserEmail) {
        Bdf bdfById = bdfRetriever.findById(bdfId);
        if (bdfById.isOrdered()) {
            throw new BdfDeleteException("Bdf is ordered. Deletion possible from the order level");
        }
        if (currentUserEmail.equals(bdfById.getCreatedBy())) {
            bdfById.setCylinders(Set.of());
            bdfCylinderDeleter.deleteByBdfId(bdfId);
            log.info("Deleting BDF with id {}", bdfId);
            bdfRepository.deleteById(bdfId);
        } else {
            throw new BdfDeleteException("Insufficient permissions to delete bdf with id: " + bdfId);
        }
    }
}
