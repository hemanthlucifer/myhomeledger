package com.myhomeledger.app.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class WebBillCreateForm {

    @NotNull(message = "Select a cost type")
    private Integer costId;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than zero")
    private BigDecimal amount;

    @NotNull(message = "Bill date is required")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate billDate;

    @NotBlank(message = "Items description is required")
    @Size(min = 1, max = 4000)
    private String items;
}
