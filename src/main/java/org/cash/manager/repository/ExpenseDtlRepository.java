package org.cash.manager.repository;

import org.cash.manager.domain.ExpenseDtl;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ExpenseDtl entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExpenseDtlRepository extends JpaRepository<ExpenseDtl, Long>, JpaSpecificationExecutor<ExpenseDtl> {
}
