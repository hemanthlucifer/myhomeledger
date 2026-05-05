package com.myhomeledger.app.costcenter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponse {

    private UUID projectId;
    private String projectName;
    private UUID userId;
}
