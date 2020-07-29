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

import org.cash.manager.domain.Deposit;
import org.cash.manager.domain.*; // for static metamodels
import org.cash.manager.repository.DepositRepository;
import org.cash.manager.service.dto.DepositCriteria;
import org.cash.manager.service.dto.DepositDTO;
import org.cash.manager.service.mapper.DepositMapper;

/**
 * Service for executing complex queries for {@link Deposit} entities in the database.
 * The main input is a {@link DepositCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DepositDTO} or a {@link Page} of {@link DepositDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DepositQueryService extends QueryService<Deposit> {

    private final Logger log = LoggerFactory.getLogger(DepositQueryService.class);

    private final DepositRepository depositRepository;

    private final DepositMapper depositMapper;

    public DepositQueryService(DepositRepository depositRepository, DepositMapper depositMapper) {
        this.depositRepository = depositRepository;
        this.depositMapper = depositMapper;
    }

    /**
     * Return a {@link List} of {@link DepositDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DepositDTO> findByCriteria(DepositCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Deposit> specification = createSpecification(criteria);
        return depositMapper.toDto(depositRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DepositDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DepositDTO> findByCriteria(DepositCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Deposit> specification = createSpecification(criteria);
        return depositRepository.findAll(specification, page)
            .map(depositMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DepositCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Deposit> specification = createSpecification(criteria);
        return depositRepository.count(specification);
    }

    /**
     * Function to convert {@link DepositCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Deposit> createSpecification(DepositCriteria criteria) {
        Specification<Deposit> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Deposit_.id));
            }
            if (criteria.getLoginId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLoginId(), Deposit_.loginId));
            }
            if (criteria.getDepositNo() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDepositNo(), Deposit_.depositNo));
            }
            if (criteria.getDepositBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDepositBy(), Deposit_.depositBy));
            }
            if (criteria.getDepositDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDepositDate(), Deposit_.depositDate));
            }
            if (criteria.getMedium() != null) {
                specification = specification.and(buildSpecification(criteria.getMedium(), Deposit_.medium));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), Deposit_.amount));
            }
            if (criteria.getIsPosted() != null) {
                specification = specification.and(buildSpecification(criteria.getIsPosted(), Deposit_.isPosted));
            }
            if (criteria.getPostDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPostDate(), Deposit_.postDate));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), Deposit_.createdBy));
            }
            if (criteria.getCreatedOn() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedOn(), Deposit_.createdOn));
            }
            if (criteria.getModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getModifiedBy(), Deposit_.modifiedBy));
            }
            if (criteria.getModifiedOn() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getModifiedOn(), Deposit_.modifiedOn));
            }
        }
        return specification;
    }
}
