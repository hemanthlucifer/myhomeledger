package com.myhomeledger.app.costcenter.service.impl;

import com.myhomeledger.app.costcenter.dto.BillCreateRequest;
import com.myhomeledger.app.costcenter.dto.BillFilterCriteria;
import com.myhomeledger.app.costcenter.dto.BillResponse;
import com.myhomeledger.app.costcenter.dto.BillUpdateRequest;
import com.myhomeledger.app.costcenter.entity.Bill;
import com.myhomeledger.app.costcenter.entity.Project;
import com.myhomeledger.app.costcenter.exceptions.CostCenterNotFoundException;
import com.myhomeledger.app.costcenter.mapper.CostCenterMapper;
import com.myhomeledger.app.costcenter.repository.BillRepository;
import com.myhomeledger.app.costcenter.repository.CostRepository;
import com.myhomeledger.app.costcenter.repository.ProjectRepository;
import com.myhomeledger.app.costcenter.service.BillService;
import com.myhomeledger.app.costcenter.specification.BillSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class BillServiceImpl implements BillService {

    private final BillRepository billRepository;
    private final ProjectRepository projectRepository;
    private final CostRepository costRepository;
    private final CostCenterMapper costCenterMapper;

    @Transactional
    @Override
    public BillResponse create(BillCreateRequest request) {
        log.info("Creating bill for project {} costId {}", request.getProjectId(), request.getCostId());
        requireProject(request.getProjectId());
        requireCost(request.getCostId());
        Bill bill = costCenterMapper.toEntity(request);
        Bill saved = billRepository.save(bill);
        log.info("Created bill {} for project {}", saved.getId(), saved.getProjectId());
        return costCenterMapper.toBillResponse(saved);
    }

    @Transactional(readOnly = true)
    @Override
    public BillResponse getById(UUID id) {
        log.info("Fetching bill {}", id);
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new CostCenterNotFoundException("Bill not found: " + id));
        return costCenterMapper.toBillResponse(bill);
    }

    @Transactional
    @Override
    public BillResponse update(UUID id, BillUpdateRequest request) {
        log.info("Updating bill {}", id);
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new CostCenterNotFoundException("Bill not found: " + id));
        requireProject(request.getProjectId());
        requireCost(request.getCostId());
        costCenterMapper.updateBill(bill, request);
        Bill saved = billRepository.save(bill);
        log.info("Updated bill {} for project {}", saved.getId(), saved.getProjectId());
        return costCenterMapper.toBillResponse(saved);
    }

    @Transactional
    @Override
    public void delete(UUID id) {
        log.info("Deleting bill {}", id);
        if (!billRepository.existsById(id)) {
            throw new CostCenterNotFoundException("Bill not found: " + id);
        }
        billRepository.deleteById(id);
        log.info("Deleted bill {}", id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BillResponse> listByProjectId(UUID projectId) {
        log.info("Listing bills for project {}", projectId);
        return billRepository.findAllByProjectIdOrderByBillDateDesc(projectId).stream()
                .map(costCenterMapper::toBillResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<BillResponse> listFiltered(UUID userId, BillFilterCriteria criteria) {
        log.info("Filtering bills for user {} project {}", userId, criteria.projectId());
        Project project = projectRepository.findById(criteria.projectId())
                .orElseThrow(() -> new CostCenterNotFoundException("Project not found: " + criteria.projectId()));
        if (!project.getUserId().equals(userId)) {
            // Deliberately mimic "not found" to avoid leaking project existence across users.
            throw new CostCenterNotFoundException("Project not found: " + criteria.projectId());
        }
        validateFilterRanges(criteria);
        Specification<Bill> spec = BillSpecification.matching(criteria);
        Sort sort = Sort.by(Sort.Order.desc("billDate"), Sort.Order.desc("id"));
        return billRepository.findAll(spec, sort).stream()
                .map(costCenterMapper::toBillResponse)
                .toList();
    }

    private static void validateFilterRanges(BillFilterCriteria criteria) {
        if (criteria.minAmount() != null && criteria.maxAmount() != null && criteria.minAmount() > criteria.maxAmount()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "minAmount must be less than or equal to maxAmount");
        }
        if (criteria.billDateFrom() != null && criteria.billDateTo() != null && criteria.billDateFrom().isAfter(criteria.billDateTo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "billDateFrom must be on or before billDateTo");
        }
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
