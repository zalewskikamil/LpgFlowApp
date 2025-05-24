package com.github.lpgflow.domain.bdf;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.Optional;
import java.util.Set;

interface BdfRepository extends Repository<Bdf, Long> {

    @Query("""
            SELECT b FROM Bdf b
            LEFT JOIN FETCH b.cylinders
            WHERE b.id = :id
            """)
    Optional<Bdf> findById(Long id);

    @Modifying(clearAutomatically = true)
    @Query("""
            DELETE FROM Bdf b WHERE b.id = :id
            """)
    void deleteById(Long id);

    @Query("""
            SELECT b FROM Bdf b
            LEFT JOIN FETCH b.cylinders
            WHERE b.ordered = false AND b.createdBy = :createdBy
            """)
    Set<Bdf> findUnorderedBdfsCreatedBy(String createdBy);

    Bdf save(Bdf bdf);
}
