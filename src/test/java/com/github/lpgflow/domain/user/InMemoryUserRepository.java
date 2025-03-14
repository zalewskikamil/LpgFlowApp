package com.github.lpgflow.domain.user;

import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

class InMemoryUserRepository implements UserRepository {

    Map<Long, User> db = new HashMap<>();
    AtomicInteger index = new AtomicInteger(1);

    @Override
    public List<User> findAll(final Pageable pageable) {
        return new ArrayList<>(db.values());
    }

    @Override
    public Optional<User> findById(final Long id) {
        User user = db.get(id);
        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> findFirstByEmail(final String email) {
        return db.values()
                .stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public User save(final User user) {
        Long userId = user.getId();
        if (userId != null && db.containsKey(userId)) {
            db.put(userId, user);
        } else {
            long index = this.index.getAndIncrement();
            db.put(index, user);
            user.setId(index);
        }
        return user;
    }

    @Override
    public boolean existsByEmail(final String email) {
        return db.values()
                .stream()
                .anyMatch(user -> user.getEmail().equals(email));
    }
}
