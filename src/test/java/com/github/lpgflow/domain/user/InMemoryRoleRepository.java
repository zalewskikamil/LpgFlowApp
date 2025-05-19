package com.github.lpgflow.domain.user;

import com.github.lpgflow.domain.util.enums.UserRole;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

class InMemoryRoleRepository implements RoleRepository {

    Map<Long, Role> db = new HashMap<>();

    InMemoryRoleRepository() {
        db.put(1L, new Role(1L, UserRole.ADMIN));
        db.put(2L, new Role(2L, UserRole.PLANNER));
    }

    @Override
    public List<Role> findAll() {
        return new ArrayList<>(db.values());
    }

    @Override
    public Optional<Role> findById(final Long id) {
        Role role = db.get(id);
        return Optional.ofNullable(role);
    }
}
