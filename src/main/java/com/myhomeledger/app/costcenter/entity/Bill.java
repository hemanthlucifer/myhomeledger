package com.myhomeledger.app.costcenter.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bill",indexes = {
        @Index(name = "projectId_costId",columnList = "project_id,cost_id"),
        @Index(name = "filter_index",columnList = "project_id,cost_id,ammount,bill_date")
})
public class Bill {

    @Id
    @UuidGenerator
    private UUID id;

    @Column(name = "cost_id", nullable = false)
    private int costId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cost_id", referencedColumnName = "cost_id", insertable = false, updatable = false)
    private Cost cost;

    @Column(name = "project_id", nullable = false)
    private UUID projectId;

    @Column(name = "ammount", nullable = false)
    private double ammount;

    @Column(name = "bill_date", nullable = false)
    private LocalDate billDate;

    @Column(name = "items", nullable = false)
    private String items;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

}
