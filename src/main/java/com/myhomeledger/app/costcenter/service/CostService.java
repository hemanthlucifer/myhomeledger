package com.myhomeledger.app.costcenter.service;

import com.myhomeledger.app.costcenter.dto.CostCreateRequest;
import com.myhomeledger.app.costcenter.dto.CostResponse;
import com.myhomeledger.app.costcenter.dto.CostUpdateRequest;

public interface CostService {

    CostResponse create(CostCreateRequest request);

    CostResponse getById(int costId);

    CostResponse update(int costId, CostUpdateRequest request);

    void delete(int costId);
}
