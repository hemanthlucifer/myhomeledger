package com.myhomeledger.app.costcenter.controller;

import com.myhomeledger.app.costcenter.CostCenterApiPaths;
import com.myhomeledger.app.costcenter.dto.BillCreateRequest;
import com.myhomeledger.app.costcenter.dto.BillFilterCriteria;
import com.myhomeledger.app.costcenter.dto.BillResponse;
import com.myhomeledger.app.costcenter.dto.BillUpdateRequest;
import com.myhomeledger.app.costcenter.service.BillService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(CostCenterApiPaths.cBASE + "/bills")
public class BillController {

    private final BillService billService;

    @PostMapping
    public ResponseEntity<BillResponse> create(@RequestBody @Valid BillCreateRequest request) {
        BillResponse body = billService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @GetMapping
    public ResponseEntity<BillResponse> getById(@RequestParam UUID id) {
        return ResponseEntity.ok(billService.getById(id));
    }

    @PatchMapping
    public ResponseEntity<BillResponse> update(
            @RequestParam UUID id,
            @RequestBody @Valid BillUpdateRequest request) {
        return ResponseEntity.ok(billService.update(id, request));
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestParam UUID id) {
        billService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-project")
    public ResponseEntity<List<BillResponse>> listByProject(@RequestParam UUID projectId) {
        return ResponseEntity.ok(billService.listByProjectId(projectId));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<BillResponse>> filter(
            @RequestParam UUID projectId,
            @RequestParam(required = false) String costName,
            @RequestParam(required = false) Double minAmount,
            @RequestParam(required = false) Double maxAmount,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate billDateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate billDateTo,
            Authentication authentication) {
        UUID userId = (UUID) authentication.getPrincipal();
        BillFilterCriteria criteria = new BillFilterCriteria(projectId, costName, minAmount, maxAmount, billDateFrom, billDateTo);
        return ResponseEntity.ok(billService.listFiltered(userId, criteria));
    }
}
