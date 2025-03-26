package com.github.lpgflow.domain.bdf;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
class BdfAdder {

    private final BdfRepository bdfRepository;
    private final BdfRetriever bdfRetriever;
    private final CylinderAssigner cylinderAssigner;

    Bdf addBdf(final Bdf bdf) {
        Bdf savedBdf = bdfRepository.save(bdf);
        cylinderAssigner.assignDefaultCylindersToBdf(savedBdf.getId());
        return bdfRetriever.findById(savedBdf.getId());
    }
}
