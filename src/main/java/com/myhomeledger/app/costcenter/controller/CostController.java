package com.myhomeledger.app.costcenter.controller;

import com.myhomeledger.app.costcenter.CostCenterApiPaths;
import com.myhomeledger.app.costcenter.dto.CostCreateRequest;
import com.myhomeledger.app.costcenter.dto.CostResponse;
import com.myhomeledger.app.costcenter.dto.CostUpdateRequest;
import com.myhomeledger.app.costcenter.service.CostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(CostCenterApiPaths.cBASE + "/costs")
public class CostController {

    private final CostService costService;

    @PostMapping
    public ResponseEntity<CostResponse> create(@RequestBody @Valid CostCreateRequest request) {
        CostResponse body = costService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @GetMapping
    public ResponseEntity<CostResponse> getById(@RequestParam int costId) {
        return ResponseEntity.ok(costService.getById(costId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<CostResponse>> getAll() {
        return ResponseEntity.ok(costService.getAll());
    }

    @PatchMapping
    public ResponseEntity<CostResponse> update(
            @RequestParam int costId,
            @RequestBody @Valid CostUpdateRequest request) {
        return ResponseEntity.ok(costService.update(costId, request));
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestParam int costId) {
        costService.delete(costId);
        return ResponseEntity.noContent().build();
    }
}
