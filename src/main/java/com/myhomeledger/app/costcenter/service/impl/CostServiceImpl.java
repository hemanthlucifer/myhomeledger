package com.myhomeledger.app.costcenter.service.impl;

import com.myhomeledger.app.costcenter.dto.CostCreateRequest;
import com.myhomeledger.app.costcenter.dto.CostResponse;
import com.myhomeledger.app.costcenter.dto.CostUpdateRequest;
import com.myhomeledger.app.costcenter.entity.Cost;
import com.myhomeledger.app.costcenter.exceptions.CostCenterConflictException;
import com.myhomeledger.app.costcenter.exceptions.CostCenterNotFoundException;
import com.myhomeledger.app.costcenter.mapper.CostCenterMapper;
import com.myhomeledger.app.costcenter.repository.BillRepository;
import com.myhomeledger.app.costcenter.repository.CostRepository;
import com.myhomeledger.app.costcenter.service.CostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CostServiceImpl implements CostService {

    private final CostRepository costRepository;
    private final BillRepository billRepository;
    private final CostCenterMapper costCenterMapper;

    @Transactional
    @Override
    public CostResponse create(CostCreateRequest request) {
        if (costRepository.existsByCostName(request.getCostName())) {
            throw new CostCenterConflictException("A cost with this name already exists.");
        }
        Cost cost = costRepository.save(costCenterMapper.toEntity(request));
        return costCenterMapper.toCostResponse(cost);
    }

    @Transactional(readOnly = true)
    @Override
    public CostResponse getById(int costId) {
        Cost cost = costRepository.findById(costId)
                .orElseThrow(() -> new CostCenterNotFoundException("Cost not found: " + costId));
        return costCenterMapper.toCostResponse(cost);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CostResponse> getAll() {
        return costRepository.findAll(Sort.by(Sort.Direction.ASC, "costName"))
                .stream()
                .map(costCenterMapper::toCostResponse)
                .toList();
    }

    @Transactional
    @Override
    public CostResponse update(int costId, CostUpdateRequest request) {
        Cost cost = costRepository.findById(costId)
                .orElseThrow(() -> new CostCenterNotFoundException("Cost not found: " + costId));
        if (costRepository.existsByCostNameAndCostIdNot(request.getCostName(), costId)) {
            throw new CostCenterConflictException("A cost with this name already exists.");
        }
        costCenterMapper.updateCost(cost, request);
        Cost saved = costRepository.save(cost);
        return costCenterMapper.toCostResponse(saved);
    }

    @Transactional
    @Override
    public void delete(int costId) {
        if (!costRepository.existsById(costId)) {
            throw new CostCenterNotFoundException("Cost not found: " + costId);
        }
        if (billRepository.countByCostId(costId) > 0) {
            throw new CostCenterConflictException("Cannot delete cost: bills reference this cost.");
        }
        costRepository.deleteById(costId);
    }
}
