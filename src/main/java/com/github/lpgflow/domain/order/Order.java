package com.github.lpgflow.domain.order;

import com.github.lpgflow.domain.util.entity.BaseEntity;
import com.github.lpgflow.domain.util.enums.OrderStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "orders")
class Order extends BaseEntity {

    @Id
    @GeneratedValue(generator = "orders_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(
            name = "orders_id_seq",
            sequenceName = "orders_id_seq",
            allocationSize = 1
    )
    private Long id;

    @Column(nullable = false)
    private String bdfIds;

    @Column(nullable = false)
    private String createdBy;

    @Column(name = "completion_Date", nullable = false)
    private Instant scheduledCompletionDate;

    @Column(nullable = false)
    private String warehouseName;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    Order(final Collection<Long> bdfIds,
          final String createdBy,
          final Instant scheduledCompletionDate,
          final String warehouseName,
          final OrderStatus status) {
        setBdfIds(bdfIds);
        this.createdBy = createdBy;
        this.scheduledCompletionDate = scheduledCompletionDate;
        this.warehouseName = warehouseName;
        this.status = status;
    }

    @Transient
    Set<Long> getBdfIds() {
        return Arrays.stream(bdfIds.split(","))
                .map(String::trim)
                .map(Long::valueOf)
                .collect(Collectors.toSet());
    }

    @Transient
    void setBdfIds(Collection<Long> bdfIds) {
        this.bdfIds = bdfIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }

    static Order.OrderBuilder builderWithBdfIds(Set<Long> bdfIds) {
        return Order.builder()
                .bdfIds(bdfIds.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(","))
                );
    }
}
