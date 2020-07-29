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

import org.cash.manager.domain.ExpanseSeq;
import org.cash.manager.domain.*; // for static metamodels
import org.cash.manager.repository.ExpanseSeqRepository;
import org.cash.manager.service.dto.ExpanseSeqCriteria;
import org.cash.manager.service.dto.ExpanseSeqDTO;
import org.cash.manager.service.mapper.ExpanseSeqMapper;

/**
 * Service for executing complex queries for {@link ExpanseSeq} entities in the database.
 * The main input is a {@link ExpanseSeqCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ExpanseSeqDTO} or a {@link Page} of {@link ExpanseSeqDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ExpanseSeqQueryService extends QueryService<ExpanseSeq> {

    private final Logger log = LoggerFactory.getLogger(ExpanseSeqQueryService.class);

    private final ExpanseSeqRepository expanseSeqRepository;

    private final ExpanseSeqMapper expanseSeqMapper;

    public ExpanseSeqQueryService(ExpanseSeqRepository expanseSeqRepository, ExpanseSeqMapper expanseSeqMapper) {
        this.expanseSeqRepository = expanseSeqRepository;
        this.expanseSeqMapper = expanseSeqMapper;
    }

    /**
     * Return a {@link List} of {@link ExpanseSeqDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ExpanseSeqDTO> findByCriteria(ExpanseSeqCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ExpanseSeq> specification = createSpecification(criteria);
        return expanseSeqMapper.toDto(expanseSeqRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ExpanseSeqDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ExpanseSeqDTO> findByCriteria(ExpanseSeqCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ExpanseSeq> specification = createSpecification(criteria);
        return expanseSeqRepository.findAll(specification, page)
            .map(expanseSeqMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ExpanseSeqCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ExpanseSeq> specification = createSpecification(criteria);
        return expanseSeqRepository.count(specification);
    }

    /**
     * Function to convert {@link ExpanseSeqCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ExpanseSeq> createSpecification(ExpanseSeqCriteria criteria) {
        Specification<ExpanseSeq> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ExpanseSeq_.id));
            }
        }
        return specification;
    }
}
