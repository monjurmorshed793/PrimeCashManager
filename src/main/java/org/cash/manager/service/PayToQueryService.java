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

import org.cash.manager.domain.PayTo;
import org.cash.manager.domain.*; // for static metamodels
import org.cash.manager.repository.PayToRepository;
import org.cash.manager.service.dto.PayToCriteria;
import org.cash.manager.service.dto.PayToDTO;
import org.cash.manager.service.mapper.PayToMapper;

/**
 * Service for executing complex queries for {@link PayTo} entities in the database.
 * The main input is a {@link PayToCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PayToDTO} or a {@link Page} of {@link PayToDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PayToQueryService extends QueryService<PayTo> {

    private final Logger log = LoggerFactory.getLogger(PayToQueryService.class);

    private final PayToRepository payToRepository;

    private final PayToMapper payToMapper;

    public PayToQueryService(PayToRepository payToRepository, PayToMapper payToMapper) {
        this.payToRepository = payToRepository;
        this.payToMapper = payToMapper;
    }

    /**
     * Return a {@link List} of {@link PayToDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PayToDTO> findByCriteria(PayToCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PayTo> specification = createSpecification(criteria);
        return payToMapper.toDto(payToRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PayToDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PayToDTO> findByCriteria(PayToCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PayTo> specification = createSpecification(criteria);
        return payToRepository.findAll(specification, page)
            .map(payToMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PayToCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PayTo> specification = createSpecification(criteria);
        return payToRepository.count(specification);
    }

    /**
     * Function to convert {@link PayToCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PayTo> createSpecification(PayToCriteria criteria) {
        Specification<PayTo> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PayTo_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), PayTo_.name));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), PayTo_.createdBy));
            }
            if (criteria.getCreatedOn() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedOn(), PayTo_.createdOn));
            }
            if (criteria.getModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getModifiedBy(), PayTo_.modifiedBy));
            }
            if (criteria.getModifiedOn() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getModifiedOn(), PayTo_.modifiedOn));
            }
        }
        return specification;
    }
}
