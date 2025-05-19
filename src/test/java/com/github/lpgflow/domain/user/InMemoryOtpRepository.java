package com.github.lpgflow.domain.user;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

class InMemoryOtpRepository implements OtpRepository {

    Map<Long, Otp> db = new HashMap<>();
    AtomicInteger index = new AtomicInteger(1);

    @Override
    public Optional<Otp> findByUserEmail(final String userEmail) {
        return db.values()
                .stream()
                .filter(otp -> otp.getUserEmail().equals(userEmail))
                .findFirst();
    }

    @Override
    public Otp save(final Otp otp) {
        Long id = otp.getId();
        if (id != null && db.containsKey(id)) {
            db.put(id, otp);
        } else {
            long index = this.index.getAndIncrement();
            db.put(index, otp);
            otp.setId(index);
        }
        return otp;
    }

    @Override
    public void deleteAllByUserEmail(final String userEmail) {
        db.entrySet()
                .removeIf(
                        entry -> userEmail.equals(entry.getValue().getUserEmail()));
    }
}
