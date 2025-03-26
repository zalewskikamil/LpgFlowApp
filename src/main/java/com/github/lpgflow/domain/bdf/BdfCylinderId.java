package com.github.lpgflow.domain.bdf;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
class BdfCylinderId implements Serializable {

    private Long bdfId;

    private Long cylinderId;

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        final BdfCylinderId that = (BdfCylinderId) o;
        return Objects.equals(bdfId, that.bdfId) && Objects.equals(cylinderId, that.cylinderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bdfId, cylinderId);
    }
}
