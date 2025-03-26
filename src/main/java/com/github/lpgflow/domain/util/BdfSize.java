package com.github.lpgflow.domain.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.github.lpgflow.domain.warehouse.WarehouseParameterException;
import lombok.Getter;

@Getter
public enum BdfSize {
    SMALL(210),
    MEDIUM(406),
    LARGE(420);

    private final int slots;

    BdfSize(final int slots) {
        this.slots = slots;
    }

    @JsonCreator
    public static BdfSize fromString(String value) {
        for (BdfSize bdfSize : BdfSize.values()) {
            if (bdfSize.name().equalsIgnoreCase(value)) {
                return bdfSize;
            }
        }
        throw new WarehouseParameterException("Illegal value of bdfSize");
    }
}
