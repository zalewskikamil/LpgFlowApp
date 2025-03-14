package com.github.lpgflow.domain.user;

import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

interface RoleRepository extends Repository<Role, Long> {

    List<Role> findAll();

    Optional<Role> findById(Long id);
}
