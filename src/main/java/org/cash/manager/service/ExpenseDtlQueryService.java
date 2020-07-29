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

import org.cash.manager.domain.ExpenseDtl;
import org.cash.manager.domain.*; // for static metamodels
import org.cash.manager.repository.ExpenseDtlRepository;
import org.cash.manager.service.dto.ExpenseDtlCriteria;
import org.cash.manager.service.dto.ExpenseDtlDTO;
import org.cash.manager.service.mapper.ExpenseDtlMapper;

/**
 * Service for executing complex queries for {@link ExpenseDtl} entities in the database.
 * The main input is a {@link ExpenseDtlCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ExpenseDtlDTO} or a {@link Page} of {@link ExpenseDtlDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ExpenseDtlQueryService extends QueryService<ExpenseDtl> {

    private final Logger log = LoggerFactory.getLogger(ExpenseDtlQueryService.class);

    private final ExpenseDtlRepository expenseDtlRepository;

    private final ExpenseDtlMapper expenseDtlMapper;

    public ExpenseDtlQueryService(ExpenseDtlRepository expenseDtlRepository, ExpenseDtlMapper expenseDtlMapper) {
        this.expenseDtlRepository = expenseDtlRepository;
        this.expenseDtlMapper = expenseDtlMapper;
    }

    /**
     * Return a {@link List} of {@link ExpenseDtlDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ExpenseDtlDTO> findByCriteria(ExpenseDtlCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ExpenseDtl> specification = createSpecification(criteria);
        return expenseDtlMapper.toDto(expenseDtlRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ExpenseDtlDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ExpenseDtlDTO> findByCriteria(ExpenseDtlCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ExpenseDtl> specification = createSpecification(criteria);
        return expenseDtlRepository.findAll(specification, page)
            .map(expenseDtlMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ExpenseDtlCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ExpenseDtl> specification = createSpecification(criteria);
        return expenseDtlRepository.count(specification);
    }

    /**
     * Function to convert {@link ExpenseDtlCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ExpenseDtl> createSpecification(ExpenseDtlCriteria criteria) {
        Specification<ExpenseDtl> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ExpenseDtl_.id));
            }
            if (criteria.getQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuantity(), ExpenseDtl_.quantity));
            }
            if (criteria.getUnitPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUnitPrice(), ExpenseDtl_.unitPrice));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), ExpenseDtl_.amount));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), ExpenseDtl_.createdBy));
            }
            if (criteria.getCreatedOn() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedOn(), ExpenseDtl_.createdOn));
            }
            if (criteria.getModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getModifiedBy(), ExpenseDtl_.modifiedBy));
            }
            if (criteria.getModifiedOn() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getModifiedOn(), ExpenseDtl_.modifiedOn));
            }
            if (criteria.getItemId() != null) {
                specification = specification.and(buildSpecification(criteria.getItemId(),
                    root -> root.join(ExpenseDtl_.item, JoinType.LEFT).get(Item_.id)));
            }
            if (criteria.getExpenseId() != null) {
                specification = specification.and(buildSpecification(criteria.getExpenseId(),
                    root -> root.join(ExpenseDtl_.expense, JoinType.LEFT).get(Expense_.id)));
            }
        }
        return specification;
    }
}
