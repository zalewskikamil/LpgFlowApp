package com.github.lpgflow.domain.bdf;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
class CylinderRetriever {

    private final CylinderRepository cylinderRepository;

    List<Cylinder> getAllCylinders(Pageable pageable) {
        return cylinderRepository.findAll(pageable);
    }

    Cylinder findById(Long id) {
        return cylinderRepository.findById(id)
                .orElseThrow(() -> new CylinderNotFoundException("Cylinder with id: " + id + " not found"));
    }
}
