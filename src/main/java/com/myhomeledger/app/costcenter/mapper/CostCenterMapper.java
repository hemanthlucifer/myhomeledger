package com.myhomeledger.app.costcenter.mapper;

import com.myhomeledger.app.costcenter.dto.BillCreateRequest;
import com.myhomeledger.app.costcenter.dto.BillResponse;
import com.myhomeledger.app.costcenter.dto.BillUpdateRequest;
import com.myhomeledger.app.costcenter.dto.CostCreateRequest;
import com.myhomeledger.app.costcenter.dto.CostResponse;
import com.myhomeledger.app.costcenter.dto.CostUpdateRequest;
import com.myhomeledger.app.costcenter.dto.ProjectCreateRequest;
import com.myhomeledger.app.costcenter.dto.ProjectResponse;
import com.myhomeledger.app.costcenter.dto.ProjectUpdateRequest;
import com.myhomeledger.app.costcenter.entity.Bill;
import com.myhomeledger.app.costcenter.entity.Cost;
import com.myhomeledger.app.costcenter.entity.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.math.BigDecimal;
import java.time.Instant;

@Mapper(componentModel = "spring", imports = {Instant.class})
public interface CostCenterMapper {

    @Mapping(target = "projectId", ignore = true)
    Project toEntity(ProjectCreateRequest request);

    @Mapping(target = "projectId", ignore = true)
    void updateProject(@MappingTarget Project project, ProjectUpdateRequest request);

    ProjectResponse toProjectResponse(Project project);

    @Mapping(target = "costId", ignore = true)
    Cost toEntity(CostCreateRequest request);

    @Mapping(target = "costId", ignore = true)
    void updateCost(@MappingTarget Cost cost, CostUpdateRequest request);

    CostResponse toCostResponse(Cost cost);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cost", ignore = true)
    @Mapping(target = "createdAt", expression = "java(Instant.now())")
    @Mapping(target = "updatedAt", expression = "java(Instant.now())")
    @Mapping(target = "ammount", source = "amount", qualifiedByName = "bigDecimalToDouble")
    Bill toEntity(BillCreateRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cost", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", expression = "java(Instant.now())")
    @Mapping(target = "ammount", source = "amount", qualifiedByName = "bigDecimalToDouble")
    void updateBill(@MappingTarget Bill bill, BillUpdateRequest request);

    @Mapping(target = "amount", source = "ammount", qualifiedByName = "doubleToBigDecimal")
    @Mapping(target = "costName", source = "cost", qualifiedByName = "costToName")
    BillResponse toBillResponse(Bill bill);

    @Named("costToName")
    default String costToName(Cost cost) {
        return cost == null ? null : cost.getCostName();
    }

    @Named("bigDecimalToDouble")
    default double bigDecimalToDouble(BigDecimal value) {
        return value != null ? value.doubleValue() : 0d;
    }

    @Named("doubleToBigDecimal")
    default BigDecimal doubleToBigDecimal(double value) {
        return BigDecimal.valueOf(value);
    }
}
