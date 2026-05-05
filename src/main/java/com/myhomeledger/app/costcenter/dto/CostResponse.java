package com.myhomeledger.app.costcenter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CostResponse {

    private int costId;
    private String costName;
}
