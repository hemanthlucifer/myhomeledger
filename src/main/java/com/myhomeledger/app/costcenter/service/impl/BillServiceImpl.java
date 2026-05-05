package com.myhomeledger.app.costcenter.service.impl;

import com.myhomeledger.app.costcenter.dto.BillCreateRequest;
import com.myhomeledger.app.costcenter.dto.BillResponse;
import com.myhomeledger.app.costcenter.dto.BillUpdateRequest;
import com.myhomeledger.app.costcenter.entity.Bill;
import com.myhomeledger.app.costcenter.exceptions.CostCenterNotFoundException;
import com.myhomeledger.app.costcenter.mapper.CostCenterMapper;
import com.myhomeledger.app.costcenter.repository.BillRepository;
import com.myhomeledger.app.costcenter.repository.CostRepository;
import com.myhomeledger.app.costcenter.repository.ProjectRepository;
import com.myhomeledger.app.costcenter.service.BillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BillServiceImpl implements BillService {

    private final BillRepository billRepository;
    private final ProjectRepository projectRepository;
    private final CostRepository costRepository;
    private final CostCenterMapper costCenterMapper;

    @Transactional
    @Override
    public BillResponse create(BillCreateRequest request) {
        requireProject(request.getProjectId());
        requireCost(request.getCostId());
        Bill bill = costCenterMapper.toEntity(request);
        Bill saved = billRepository.save(bill);
        return costCenterMapper.toBillResponse(saved);
    }

    @Transactional(readOnly = true)
    @Override
    public BillResponse getById(UUID id) {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new CostCenterNotFoundException("Bill not found: " + id));
        return costCenterMapper.toBillResponse(bill);
    }

    @Transactional
    @Override
    public BillResponse update(UUID id, BillUpdateRequest request) {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new CostCenterNotFoundException("Bill not found: " + id));
        requireProject(request.getProjectId());
        requireCost(request.getCostId());
        costCenterMapper.updateBill(bill, request);
        Bill saved = billRepository.save(bill);
        return costCenterMapper.toBillResponse(saved);
    }

    @Transactional
    @Override
    public void delete(UUID id) {
        if (!billRepository.existsById(id)) {
            throw new CostCenterNotFoundException("Bill not found: " + id);
        }
        billRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BillResponse> listByProjectId(UUID projectId) {
        return billRepository.findAllByProjectIdOrderByBillDateDesc(projectId).stream()
                .map(costCenterMapper::toBillResponse)
                .toList();
    }

    private void requireProject(UUID projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new CostCenterNotFoundException("Project not found: " + projectId);
        }
    }

    private void requireCost(int costId) {
        if (!costRepository.existsById(costId)) {
            throw new CostCenterNotFoundException("Cost not found: " + costId);
        }
    }
}
