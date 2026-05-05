package com.myhomeledger.app.costcenter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CostCreateRequest {

    @NotBlank
    @Size(min = 1, max = 255)
    private String costName;
}
