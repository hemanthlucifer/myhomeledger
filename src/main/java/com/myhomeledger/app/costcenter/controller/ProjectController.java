package com.myhomeledger.app.costcenter.controller;

import com.myhomeledger.app.costcenter.CostCenterApiPaths;
import com.myhomeledger.app.costcenter.dto.ProjectCreateRequest;
import com.myhomeledger.app.costcenter.dto.ProjectResponse;
import com.myhomeledger.app.costcenter.dto.ProjectUpdateRequest;
import com.myhomeledger.app.costcenter.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(CostCenterApiPaths.cBASE + "/projects")
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<ProjectResponse> create(@RequestBody @Valid ProjectCreateRequest request) {
        ProjectResponse body = projectService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @GetMapping
    public ResponseEntity<ProjectResponse> getById(@RequestParam UUID projectId) {
        return ResponseEntity.ok(projectService.getById(projectId));
    }

    @PatchMapping
    public ResponseEntity<ProjectResponse> update(
            @RequestParam UUID projectId,
            @RequestBody @Valid ProjectUpdateRequest request) {
        return ResponseEntity.ok(projectService.update(projectId, request));
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestParam UUID projectId) {
        projectService.delete(projectId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-user")
    public ResponseEntity<List<ProjectResponse>> listByUser(@RequestParam UUID userId) {
        return ResponseEntity.ok(projectService.listByUserId(userId));
    }
}
