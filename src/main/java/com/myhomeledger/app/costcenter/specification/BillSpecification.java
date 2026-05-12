package com.myhomeledger.app.costcenter.specification;

import com.myhomeledger.app.costcenter.dto.BillFilterCriteria;
import com.myhomeledger.app.costcenter.entity.Bill;
import com.myhomeledger.app.costcenter.entity.Cost;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class BillSpecification {

    private BillSpecification() {}

    public static Specification<Bill> matching(BillFilterCriteria criteria) {
        return (root, query, cb) -> {
            Join<Bill, Cost> cost = root.join("cost", JoinType.INNER);
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("projectId"), criteria.projectId()));

            if (criteria.costId() != null) {
                predicates.add(cb.equal(root.get("costId"), criteria.costId()));
            } else if (criteria.costName() != null) {
                String pattern = likePattern(criteria.costName().toLowerCase(Locale.ROOT));
                predicates.add(cb.like(cb.lower(cost.get("costName")), pattern, '\\'));
            }
            if (criteria.minAmount() != null) {
                predicates.add(cb.ge(root.get("ammount"), criteria.minAmount()));
            }
            if (criteria.maxAmount() != null) {
                predicates.add(cb.le(root.get("ammount"), criteria.maxAmount()));
            }
            if (criteria.billDateFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("billDate"), criteria.billDateFrom()));
            }
            if (criteria.billDateTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("billDate"), criteria.billDateTo()));
            }

            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }

    private static String likePattern(String lowerCaseFragment) {
        String escaped = lowerCaseFragment
                .replace("\\", "\\\\")
                .replace("%", "\\%")
                .replace("_", "\\_");
        return "%" + escaped + "%";
    }
}
