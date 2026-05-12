package com.myhomeledger.app.costcenter.service;

import com.myhomeledger.app.costcenter.dto.BillCreateRequest;
import com.myhomeledger.app.costcenter.dto.BillFilterCriteria;
import com.myhomeledger.app.costcenter.dto.BillResponse;
import com.myhomeledger.app.costcenter.dto.BillUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface BillService {

    BillResponse create(BillCreateRequest request);

    BillResponse getById(UUID id);

    BillResponse update(UUID id, BillUpdateRequest request);

    void delete(UUID id);

    List<BillResponse> listByProjectId(UUID projectId);

    List<BillResponse> listFiltered(UUID userId, BillFilterCriteria criteria);
}
