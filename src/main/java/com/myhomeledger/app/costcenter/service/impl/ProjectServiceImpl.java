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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final BillRepository billRepository;
    private final CostCenterMapper costCenterMapper;

    @Transactional
    @Override
    public ProjectResponse create(ProjectCreateRequest request) {
        Project project = costCenterMapper.toEntity(request);
        Project saved = projectRepository.save(project);
        return costCenterMapper.toProjectResponse(saved);
    }

    @Transactional(readOnly = true)
    @Override
    public ProjectResponse getById(UUID projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new CostCenterNotFoundException("Project not found: " + projectId));
        return costCenterMapper.toProjectResponse(project);
    }

    @Transactional
    @Override
    public ProjectResponse update(UUID projectId, ProjectUpdateRequest request) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new CostCenterNotFoundException("Project not found: " + projectId));
        costCenterMapper.updateProject(project, request);
        Project saved = projectRepository.save(project);
        return costCenterMapper.toProjectResponse(saved);
    }

    @Transactional
    @Override
    public void delete(UUID projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new CostCenterNotFoundException("Project not found: " + projectId);
        }
        if (billRepository.countByProjectId(projectId) > 0) {
            throw new CostCenterConflictException("Cannot delete project: bills exist for this project.");
        }
        projectRepository.deleteById(projectId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProjectResponse> listByUserId(UUID userId) {
        return projectRepository.findAllByUserId(userId).stream()
                .map(costCenterMapper::toProjectResponse)
                .toList();
    }
}
