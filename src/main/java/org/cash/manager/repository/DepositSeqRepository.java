package org.cash.manager.repository;

import org.cash.manager.domain.DepositSeq;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the DepositSeq entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DepositSeqRepository extends JpaRepository<DepositSeq, Long>, JpaSpecificationExecutor<DepositSeq> {
}
