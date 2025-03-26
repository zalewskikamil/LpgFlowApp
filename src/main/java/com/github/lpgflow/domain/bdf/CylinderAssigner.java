package com.github.lpgflow.domain.bdf;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
class CylinderAssigner {

    private final BdfCylinderRepository bdfCylinderRepository;
    private final BdfRetriever bdfRetriever;
    private final CylinderRetriever cylinderRetriever;
    private final BdfRepository bdfRepository;
    private final BdfCylinderRetriever bdfCylinderRetriever;

    private final long defaultCylinderId = 1L;


    void assignDefaultCylindersToBdf(Long bdfId) {
        Bdf bdf = bdfRetriever.findById(bdfId);
        Cylinder cylinder = cylinderRetriever.findById(defaultCylinderId);
        BdfCylinder bdfCylinder = new BdfCylinder(bdf, cylinder, bdf.getSize().getSlots());
        BdfCylinder savedBdfCylinder = bdfCylinderRepository.save(bdfCylinder);
        bdf.addCylindersToBdf(savedBdfCylinder);
        bdfRepository.save(bdf);
    }

    void assignCylindersToBdf(Long bdfId, Long cylinderId, int cylindersQuantity) {
        Bdf bdf = bdfRetriever.findById(bdfId);
        Cylinder cylinder = cylinderRetriever.findById(cylinderId);
        if (bdfCylinderRetriever.existsByBdfIdAndCylinderId(bdfId, cylinderId)) {
            throw new AssignCylindersToBdfParameterException(
                    "Cylinders with id: " + cylinderId + " are already assigned to bdf with id: " + bdfId +
                            ". Please update bdf to change cylinder quantity");
        }
        int slotsPerCylinder = cylinder.getBdfSlots();
        int assignedCylindersSlots = slotsPerCylinder * cylindersQuantity;
        BdfCylinder defaultBdfCylinder = bdfCylinderRetriever.findByIdBdfIdAndCylinderId(bdfId, defaultCylinderId);
        int defaultCylindersQuantity = defaultBdfCylinder.getQuantity();
        if (assignedCylindersSlots > defaultCylindersQuantity) {
            throw new AssignCylindersToBdfParameterException("You are trying to assign too many cylinders. " +
                    "Please reduce the number of assigned cylinders or increase the number of default cylinders first");
        }
        defaultBdfCylinder.setQuantity(defaultCylindersQuantity - cylindersQuantity);
        bdf.addCylindersToBdf(defaultBdfCylinder);
        bdfCylinderRepository.save(defaultBdfCylinder);
        BdfCylinder bdfCylinderToSave = new BdfCylinder(bdf, cylinder, cylindersQuantity);
        log.info("Adding {} cylinders with id {} to BDF with id {}",
                bdfCylinderToSave.getQuantity(), cylinderId, bdfId);
        BdfCylinder savedBdfCylinder = bdfCylinderRepository.save(bdfCylinderToSave);
        bdf.addCylindersToBdf(savedBdfCylinder);
        bdfRepository.save(bdf);
    }
}
