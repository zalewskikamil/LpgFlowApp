package com.github.lpgflow.domain.bdf;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
class BdfCylinderDeleter {

    private final BdfCylinderRepository bdfCylinderRepository;

    void deleteByBdfId(Long bdfId) {
        bdfCylinderRepository.deleteByBdfId(bdfId);
    }
}
