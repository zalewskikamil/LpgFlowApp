package com.github.lpgflow.domain.bdf;


import com.github.lpgflow.domain.util.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
class BdfCylinder extends BaseEntity {

    @EmbeddedId
    private BdfCylinderId id;

    @ManyToOne
    @MapsId("bdfId")
    @JoinColumn(nullable = false)
    private Bdf bdf;

    @ManyToOne
    @MapsId("cylinderId")
    private Cylinder cylinder;

    @Column(nullable = false)
    private int quantity;

    BdfCylinder(final Bdf bdf, final Cylinder cylinder, final int quantity) {
        this.bdf = bdf;
        this.cylinder = cylinder;
        this.quantity = quantity;
        this.id = new BdfCylinderId(bdf.getId(), cylinder.getId());
    }
}
