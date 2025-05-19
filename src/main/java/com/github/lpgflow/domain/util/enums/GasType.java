package com.github.lpgflow.domain.util.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.github.lpgflow.domain.bdf.CylinderParameterException;

public enum GasType {
    PROPANE,
    MIXED;

    @JsonCreator
    public static GasType fromString(String value) {
        for (GasType gasType : GasType.values()) {
            if (gasType.name().equalsIgnoreCase(value)) {
                return gasType;
            }
        }
        throw new CylinderParameterException("Illegal value of gasType");
    }
}
