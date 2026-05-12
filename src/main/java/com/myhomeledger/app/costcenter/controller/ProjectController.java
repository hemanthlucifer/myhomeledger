package com.myhomeledger.app.costcenter.controller;

import com.myhomeledger.app.costcenter.CostCenterApiPaths;
import com.myhomeledger.app.costcenter.dto.ProjectCreateRequest;
import com.myhomeledger.app.costcenter.dto.ProjectResponse;
import com.myhomeledger.app.costcenter.dto.ProjectUpdateRequest;
import com.myhomeledger.app.costcenter.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(CostCenterApiPaths.cBASE + "/projects")
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<ProjectResponse> create(@RequestBody @Valid ProjectCreateRequest request) {
        log.info("Create project request received for user {}", request.getUserId());
        ProjectResponse body = projectService.create(request);
        log.info("Project {} created for user {}", body.getProjectId(), body.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @GetMapping
    public ResponseEntity<ProjectResponse> getById(@RequestParam UUID projectId) {
        log.info("Get project request received for project {}", projectId);
        return ResponseEntity.ok(projectService.getById(projectId));
    }

    @PatchMapping
    public ResponseEntity<ProjectResponse> update(
            @RequestParam UUID projectId,
            @RequestBody @Valid ProjectUpdateRequest request) {
        log.info("Update project request received for project {}", projectId);
        return ResponseEntity.ok(projectService.update(projectId, request));
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestParam UUID projectId) {
        log.info("Delete project request received for project {}", projectId);
        projectService.delete(projectId);
        log.info("Project {} deleted", projectId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-user")
    public ResponseEntity<List<ProjectResponse>> listByUser(@RequestParam UUID userId) {
        log.info("List projects request received for user {}", userId);
        return ResponseEntity.ok(projectService.listByUserId(userId));
    }
}
