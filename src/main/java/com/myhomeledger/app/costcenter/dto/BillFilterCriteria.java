package com.myhomeledger.app.costcenter.dto;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Optional filters for listing bills within a project. Used with {@code BillSpecification}.
 */
public record BillFilterCriteria(
        UUID projectId,
        String costName,
        Double minAmount,
        Double maxAmount,
        LocalDate billDateFrom,
        LocalDate billDateTo
) {
    public BillFilterCriteria {
        if (projectId == null) {
            throw new IllegalArgumentException("projectId is required");
        }
        if (costName != null && costName.isBlank()) {
            costName = null;
        }
    }
}
