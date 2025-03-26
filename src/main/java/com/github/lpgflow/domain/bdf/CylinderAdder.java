package com.github.lpgflow.domain.bdf;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
class CylinderAdder {

    private final CylinderRepository cylinderRepository;

    Cylinder addCylinder(Cylinder cylinder) {
        log.info("Adding new cylinder (capacity: {}, gasType: {}, usageType: {}, collar: {}, additionalInfo: {})",
                cylinder.getCapacity().name(),
                cylinder.getGasType().name(),
                cylinder.getUsageType().name(),
                cylinder.isCollar(),
                cylinder.getAdditionalInfo());
        return cylinderRepository.save(cylinder);
    }
}
