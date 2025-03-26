package com.github.lpgflow.domain.bdf;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.Optional;
import java.util.Set;

interface BdfCylinderRepository extends Repository<BdfCylinder, Long> {

    Set<BdfCylinder> findByIdBdfId(Long bdfId);

    Optional<BdfCylinder> findByIdBdfIdAndCylinderId(Long bdfId, Long cylinderId);

    boolean existsByIdBdfIdAndCylinderId(Long bdfId, Long cylinderId);

    BdfCylinder save(BdfCylinder bdfCylinder);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM BdfCylinder bc WHERE bc.id.bdfId = :bdfId")
    void deleteByBdfId(Long bdfId);
}
