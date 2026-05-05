package com.myhomeledger.app.costcenter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class ProjectCreateRequest {

    @NotBlank
    @Size(min = 1, max = 255)
    private String projectName;

    @NotNull
    private UUID userId;
}
