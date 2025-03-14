package com.github.lpgflow.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
class RoleRetriever {

    private final RoleRepository roleRepository;

    List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    Role getRoleById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(
                ()-> new RoleNotFoundException("Role with id: " + id + " not found")
        );
    }

}
