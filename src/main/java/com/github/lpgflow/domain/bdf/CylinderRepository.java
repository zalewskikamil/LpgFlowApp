package com.github.lpgflow.domain.bdf;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

interface CylinderRepository extends Repository<Cylinder, Long> {

    List<Cylinder> findAll(Pageable pageable);

    Optional<Cylinder> findById(Long id);

    Cylinder save(Cylinder cylinder);
}
