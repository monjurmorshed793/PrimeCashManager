package org.cash.manager.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import org.cash.manager.domain.DepositSeq;
import org.cash.manager.domain.*; // for static metamodels
import org.cash.manager.repository.DepositSeqRepository;
import org.cash.manager.service.dto.DepositSeqCriteria;
import org.cash.manager.service.dto.DepositSeqDTO;
import org.cash.manager.service.mapper.DepositSeqMapper;

/**
 * Service for executing complex queries for {@link DepositSeq} entities in the database.
 * The main input is a {@link DepositSeqCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DepositSeqDTO} or a {@link Page} of {@link DepositSeqDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DepositSeqQueryService extends QueryService<DepositSeq> {

    private final Logger log = LoggerFactory.getLogger(DepositSeqQueryService.class);

    private final DepositSeqRepository depositSeqRepository;

    private final DepositSeqMapper depositSeqMapper;

    public DepositSeqQueryService(DepositSeqRepository depositSeqRepository, DepositSeqMapper depositSeqMapper) {
        this.depositSeqRepository = depositSeqRepository;
        this.depositSeqMapper = depositSeqMapper;
    }

    /**
     * Return a {@link List} of {@link DepositSeqDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DepositSeqDTO> findByCriteria(DepositSeqCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<DepositSeq> specification = createSpecification(criteria);
        return depositSeqMapper.toDto(depositSeqRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DepositSeqDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DepositSeqDTO> findByCriteria(DepositSeqCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DepositSeq> specification = createSpecification(criteria);
        return depositSeqRepository.findAll(specification, page)
            .map(depositSeqMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DepositSeqCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<DepositSeq> specification = createSpecification(criteria);
        return depositSeqRepository.count(specification);
    }

    /**
     * Function to convert {@link DepositSeqCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DepositSeq> createSpecification(DepositSeqCriteria criteria) {
        Specification<DepositSeq> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), DepositSeq_.id));
            }
        }
        return specification;
    }
}
