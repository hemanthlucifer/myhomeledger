package com.myhomeledger.app.costcenter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
public class BillUpdateRequest {

    @NotNull
    private Integer costId;

    @NotNull
    private UUID projectId;

    @NotNull
    @Positive
    private BigDecimal amount;

    @NotNull
    private LocalDate billDate;

    @NotBlank
    @Size(min = 1, max = 4000)
    private String items;
}
