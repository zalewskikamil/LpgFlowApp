package com.github.lpgflow.domain.user;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

interface UserRepository extends Repository<User, Long> {

    @Query("SELECT u FROM User u")
    List<User> findAll(Pageable pageable);

    @Query("""
            SELECT u FROM User u
            LEFT JOIN FETCH u.roles
            WHERE u.id = :id
            """)
    Optional<User> findById(Long id);

    Optional<User> findFirstByEmail(String email);

    User save(User user);

    boolean existsByEmail(String email);
}
