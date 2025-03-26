package com.github.lpgflow.domain.warehouse;

import com.github.lpgflow.domain.util.BaseEntity;
import com.github.lpgflow.domain.util.BdfSize;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
class Warehouse extends BaseEntity {

    @Id
    @GeneratedValue(generator = "warehouse_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(
            name = "warehouse_id_seq",
            sequenceName = "warehouse_id_seq",
            allocationSize = 1
    )
    private Long id;

    @Column(unique = true)
    private String name;

    @Column(nullable = false)
    private String regionalManagerEmail;

    @Column(nullable = false)
    private String warehousemanEmail;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    private Boolean active = false;

    @Enumerated(EnumType.STRING)
    private BdfSize bdfSize;

    @Column(nullable = false)
    private Integer maxCylindersWithoutCollarPerBdf;

    Warehouse(String name, String regionalManagerEmail, String warehousemanEmail, boolean active, BdfSize bdfSize,
              Integer maxCylindersWithoutCollarPerBdf) {
        this.name = name;
        this.regionalManagerEmail = regionalManagerEmail;
        this.warehousemanEmail = warehousemanEmail;
        this.active = active;
        this.bdfSize = bdfSize;
        this.maxCylindersWithoutCollarPerBdf = maxCylindersWithoutCollarPerBdf;
    }
}
