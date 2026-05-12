package com.myhomeledger.app.costcenter.service.impl;

import com.myhomeledger.app.costcenter.dto.ProjectCreateRequest;
import com.myhomeledger.app.costcenter.dto.ProjectResponse;
import com.myhomeledger.app.costcenter.dto.ProjectUpdateRequest;
import com.myhomeledger.app.costcenter.entity.Project;
import com.myhomeledger.app.costcenter.exceptions.CostCenterConflictException;
import com.myhomeledger.app.costcenter.exceptions.CostCenterNotFoundException;
import com.myhomeledger.app.costcenter.mapper.CostCenterMapper;
import com.myhomeledger.app.costcenter.repository.BillRepository;
import com.myhomeledger.app.costcenter.repository.ProjectRepository;
import com.myhomeledger.app.costcenter.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final BillRepository billRepository;
    private final CostCenterMapper costCenterMapper;

    @Transactional
    @Override
    public ProjectResponse create(ProjectCreateRequest request) {
        log.info("Creating project for user {}", request.getUserId());
        Project project = costCenterMapper.toEntity(request);
        Project saved = projectRepository.save(project);
        log.info("Created project {} for user {}", saved.getProjectId(), saved.getUserId());
        return costCenterMapper.toProjectResponse(saved);
    }

    @Transactional(readOnly = true)
    @Override
    public ProjectResponse getById(UUID projectId) {
        log.info("Fetching project {}", projectId);
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new CostCenterNotFoundException("Project not found: " + projectId));
        return costCenterMapper.toProjectResponse(project);
    }

    @Transactional
    @Override
    public ProjectResponse update(UUID projectId, ProjectUpdateRequest request) {
        log.info("Updating project {}", projectId);
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new CostCenterNotFoundException("Project not found: " + projectId));
        costCenterMapper.updateProject(project, request);
        Project saved = projectRepository.save(project);
        log.info("Updated project {}", projectId);
        return costCenterMapper.toProjectResponse(saved);
    }

    @Transactional
    @Override
    public void delete(UUID projectId) {
        log.info("Deleting project {}", projectId);
        if (!projectRepository.existsById(projectId)) {
            throw new CostCenterNotFoundException("Project not found: " + projectId);
        }
        if (billRepository.countByProjectId(projectId) > 0) {
            log.warn("Project {} delete blocked: bills exist", projectId);
            throw new CostCenterConflictException("Cannot delete project: bills exist for this project.");
        }
        projectRepository.deleteById(projectId);
        log.info("Deleted project {}", projectId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProjectResponse> listByUserId(UUID userId) {
        log.info("Listing projects for user {}", userId);
        return projectRepository.findAllByUserId(userId).stream()
                .map(costCenterMapper::toProjectResponse)
                .toList();
    }
}
