package com.myhomeledger.app.costcenter.repository;

import com.myhomeledger.app.costcenter.entity.Cost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CostRepository extends JpaRepository<Cost, Integer> {

    boolean existsByCostNameAndCostIdNot(String costName, int costId);

    boolean existsByCostName(String costName);

    Optional<Cost> findByCostName(String costName);
}
