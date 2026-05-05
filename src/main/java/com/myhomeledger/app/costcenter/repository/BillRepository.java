package com.myhomeledger.app.costcenter.repository;

import com.myhomeledger.app.costcenter.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BillRepository extends JpaRepository<Bill, UUID> {

    List<Bill> findAllByProjectIdOrderByBillDateDesc(UUID projectId);

    long countByProjectId(UUID projectId);

    long countByCostId(int costId);
}
