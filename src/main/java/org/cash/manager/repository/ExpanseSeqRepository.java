package org.cash.manager.repository;

import org.cash.manager.domain.ExpanseSeq;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ExpanseSeq entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExpanseSeqRepository extends JpaRepository<ExpanseSeq, Long>, JpaSpecificationExecutor<ExpanseSeq> {
}
