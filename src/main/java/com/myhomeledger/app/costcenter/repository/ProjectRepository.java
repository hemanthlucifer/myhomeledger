package com.myhomeledger.app.costcenter.repository;

import com.myhomeledger.app.costcenter.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, UUID> {

    List<Project> findAllByUserId(UUID userId);
}
