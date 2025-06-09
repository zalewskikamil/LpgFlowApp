package com.github.lpgflow.domain.warehouse;

import com.github.lpgflow.domain.util.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter(AccessLevel.PACKAGE)
@Setter(AccessLevel.PACKAGE)
@NoArgsConstructor
class Address extends BaseEntity {

    @Id
    @GeneratedValue(generator = "address_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(
            name = "address_id_seq",
            sequenceName = "address_id_seq",
            allocationSize = 1
    )
    Long id;

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String postalCode;

    Address(final String street, final String city, final String postalCode) {
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
    }
}
