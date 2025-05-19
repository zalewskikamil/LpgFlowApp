package com.github.lpgflow.domain.util.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.github.lpgflow.domain.bdf.CylinderParameterException;

public enum CylinderCapacity {

    TEN_KG(10),
    ELEVEN_KG(11),
    THIRTY_KG(30),
    THIRTY_THREE_KG(33);

    private final int capacityInKg;

    CylinderCapacity(final int capacity) {
        this.capacityInKg = capacity;
    }

    public int getCapacityInKg() {
        return capacityInKg;
    }

    @JsonCreator
    public static CylinderCapacity fromString(String value) {
        for (CylinderCapacity cylinderCapacity : CylinderCapacity.values()) {
            if (cylinderCapacity.name().equalsIgnoreCase(value)) {
                return cylinderCapacity;
            }
        }
        throw new CylinderParameterException("Illegal value of cylinderCapacity");
    }
}
