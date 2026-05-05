package com.myhomeledger.app.costcenter.service;

import com.myhomeledger.app.costcenter.dto.ProjectCreateRequest;
import com.myhomeledger.app.costcenter.dto.ProjectResponse;
import com.myhomeledger.app.costcenter.dto.ProjectUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface ProjectService {

    ProjectResponse create(ProjectCreateRequest request);

    ProjectResponse getById(UUID projectId);

    ProjectResponse update(UUID projectId, ProjectUpdateRequest request);

    void delete(UUID projectId);

    List<ProjectResponse> listByUserId(UUID userId);
}
