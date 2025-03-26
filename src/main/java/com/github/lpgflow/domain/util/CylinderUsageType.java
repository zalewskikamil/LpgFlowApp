package com.github.lpgflow.domain.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.github.lpgflow.domain.bdf.CylinderParameterException;

public enum CylinderUsageType {
    HEATING,
    FORKLIFT;

    @JsonCreator
    public static CylinderUsageType fromString(String value) {
        for (CylinderUsageType cylinderUsageType : CylinderUsageType.values()) {
            if (cylinderUsageType.name().equalsIgnoreCase(value)) {
                return cylinderUsageType;
            }
        }
        throw new CylinderParameterException("Illegal value of cylinder capacity");
    }
}
