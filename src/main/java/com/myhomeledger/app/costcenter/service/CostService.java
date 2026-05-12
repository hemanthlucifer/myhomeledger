package com.myhomeledger.app.costcenter.service;

import com.myhomeledger.app.costcenter.dto.CostCreateRequest;
import com.myhomeledger.app.costcenter.dto.CostResponse;
import com.myhomeledger.app.costcenter.dto.CostUpdateRequest;

import java.util.List;

public interface CostService {

    CostResponse create(CostCreateRequest request);

    CostResponse getById(int costId);

    List<CostResponse> getAll();

    CostResponse update(int costId, CostUpdateRequest request);

    void delete(int costId);
}
