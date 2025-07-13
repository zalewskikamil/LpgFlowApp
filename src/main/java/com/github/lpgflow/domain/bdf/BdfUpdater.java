package com.github.lpgflow.domain.bdf;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
class BdfUpdater {

    private final BdfRetriever bdfRetriever;
    private final BdfRepository bdfRepository;
    private final BdfCylinderRepository bdfCylinderRepository;
    private final BdfCylinderRetriever bdfCylinderRetriever;
    private final CylinderRetriever cylinderRetriever;

    @Value("${cylinder.default.id}")
    private Long defaultCylinderId;

    void updateBdfPartiallyById(Long bdfId, Long cylinderId, int newQuantity) {
        Bdf bdfFromDatabase = bdfRetriever.findById(bdfId);
        Cylinder cylinder = cylinderRetriever.findById(cylinderId);
        BdfCylinder bdfCylinderToUpdate = bdfCylinderRetriever.findByIdBdfIdAndCylinderId(bdfId, cylinderId);
        Cylinder defaultCylinder = cylinderRetriever.findById(defaultCylinderId);
        BdfCylinder defaultBdfCylinder = bdfCylinderRetriever.findByIdBdfIdAndCylinderId(bdfId, defaultCylinderId);
        Set<BdfCylinder> bdfCylindersFromDatabase = bdfFromDatabase.getCylinders();
        int cylinderToUpdateQuantity = bdfCylindersFromDatabase.stream()
                .filter(bdfCylinder -> bdfCylinder.getId().equals(bdfCylinderToUpdate.getId()))
                .map(BdfCylinder::getQuantity)
                .findFirst()
                .get();
        int defaultCylinderQuantity = defaultBdfCylinder.getQuantity();
        int defaultCylinderSlots = defaultCylinderQuantity * defaultCylinder.getBdfSlots();
        int cylinderToUpdateSlots = cylinderToUpdateQuantity * cylinder.getBdfSlots();
        int requiredSlots = newQuantity * cylinder.getBdfSlots();
        if (requiredSlots > defaultCylinderSlots + cylinderToUpdateSlots) {
            throw new RuntimeException("Cylinder parameters invalid");
        }
        bdfCylinderToUpdate.setQuantity(newQuantity);
        BdfCylinder savedBdfCylinder = bdfCylinderRepository.save(bdfCylinderToUpdate);
        bdfCylindersFromDatabase.add(savedBdfCylinder);
        defaultCylinderSlots = defaultCylinderSlots - (requiredSlots - cylinderToUpdateSlots);
        defaultCylinderQuantity = defaultCylinderSlots / defaultCylinder.getBdfSlots();
        defaultBdfCylinder.setQuantity(defaultCylinderQuantity);
        bdfCylindersFromDatabase.add(defaultBdfCylinder);
        bdfRepository.save(bdfFromDatabase);
    }

    void setOrderedStatus(Long bdfId, boolean ordered) {
        Bdf bdf = bdfRetriever.findById(bdfId);
        bdf.setOrdered(ordered);
        bdfRepository.save(bdf);
    }
}
