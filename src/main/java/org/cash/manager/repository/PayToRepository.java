package org.cash.manager.repository;

import org.cash.manager.domain.PayTo;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the PayTo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PayToRepository extends JpaRepository<PayTo, Long>, JpaSpecificationExecutor<PayTo> {
}
