package com.myhomeledger.app.costcenter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillResponse {

    private UUID id;
    private int costId;
    private String costName;
    private UUID projectId;
    private BigDecimal amount;
    private LocalDate billDate;
    private String items;
    private Instant createdAt;
    private Instant updatedAt;
}
