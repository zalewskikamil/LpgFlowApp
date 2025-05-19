package com.github.lpgflow.domain.bdf;

import com.github.lpgflow.domain.util.entity.BaseEntity;
import com.github.lpgflow.domain.util.enums.BdfSize;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
class Bdf extends BaseEntity {

    @Id
    @GeneratedValue(generator = "bdf_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(
            name = "bdf_id_seq",
            sequenceName = "bdf_id_seq",
            allocationSize = 1
    )
    private Long id;

    @Enumerated(EnumType.STRING)
    private BdfSize size;

    @OneToMany(mappedBy = "bdf")
    private Set<BdfCylinder> cylinders = new HashSet<>();

    private boolean ordered;

    @Column(nullable = false)
    private String createdBy;

    Bdf(final BdfSize size, final boolean ordered, final String createdBy) {
        this.size = size;
        this.ordered = ordered;
        this.createdBy = createdBy;
    }

    void addCylindersToBdf(BdfCylinder cylinder) {
        cylinders.add(cylinder);
    }
}
