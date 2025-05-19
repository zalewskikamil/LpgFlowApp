package com.github.lpgflow.domain.bdf;

import com.github.lpgflow.domain.util.entity.BaseEntity;
import com.github.lpgflow.domain.util.enums.CylinderCapacity;
import com.github.lpgflow.domain.util.enums.CylinderUsageType;
import com.github.lpgflow.domain.util.enums.GasType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
class Cylinder extends BaseEntity {

    @Id
    @GeneratedValue(generator = "cylinder_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(
            name = "cylinder_id_seq",
            sequenceName = "cylinder_id_seq",
            allocationSize = 1
    )
    private Long id;

    @Enumerated(EnumType.STRING)
    private CylinderCapacity capacity;

    @Enumerated(EnumType.STRING)
    private GasType gasType;

    @Enumerated(EnumType.STRING)
    private CylinderUsageType usageType;

    @Column(name = "has_collar", nullable = false)
    private boolean collar;

    private String additionalInfo;

    private int bdfSlots;

    Cylinder(final CylinderCapacity capacity, final GasType gasType, final CylinderUsageType usageType,
             final boolean collar, final String additionalInfo) {
        this.capacity = capacity;
        this.gasType = gasType;
        this.usageType = usageType;
        this.collar = collar;
        this.additionalInfo = additionalInfo;
        this.bdfSlots = calculateBdfSlots(capacity);
    }

    Cylinder(final Long id, final CylinderCapacity capacity, final GasType gasType, final CylinderUsageType usageType,
             final boolean collar, final String additionalInfo) {
        this.id = id;
        this.capacity = capacity;
        this.gasType = gasType;
        this.usageType = usageType;
        this.collar = collar;
        this.additionalInfo = additionalInfo;
        this.bdfSlots = calculateBdfSlots(capacity);
    }

    private int calculateBdfSlots(CylinderCapacity capacity) {
        return capacity.getCapacityInKg() > 11 ? 2 : 1;
    }
}
