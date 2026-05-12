package com.myhomeledger.app.costcenter.repository;

import com.myhomeledger.app.costcenter.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface BillRepository extends JpaRepository<Bill, UUID>, JpaSpecificationExecutor<Bill> {

    List<Bill> findAllByProjectIdOrderByBillDateDesc(UUID projectId);

    long countByProjectId(UUID projectId);

    long countByCostId(int costId);
}
