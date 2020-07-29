package org.cash.manager.service;

import org.cash.manager.domain.ExpenseDtl;
import org.cash.manager.repository.ExpenseDtlRepository;
import org.cash.manager.service.dto.ExpenseDtlDTO;
import org.cash.manager.service.mapper.ExpenseDtlMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link ExpenseDtl}.
 */
@Service
@Transactional
public class ExpenseDtlService {

    private final Logger log = LoggerFactory.getLogger(ExpenseDtlService.class);

    private final ExpenseDtlRepository expenseDtlRepository;

    private final ExpenseDtlMapper expenseDtlMapper;

    public ExpenseDtlService(ExpenseDtlRepository expenseDtlRepository, ExpenseDtlMapper expenseDtlMapper) {
        this.expenseDtlRepository = expenseDtlRepository;
        this.expenseDtlMapper = expenseDtlMapper;
    }

    /**
     * Save a expenseDtl.
     *
     * @param expenseDtlDTO the entity to save.
     * @return the persisted entity.
     */
    public ExpenseDtlDTO save(ExpenseDtlDTO expenseDtlDTO) {
        log.debug("Request to save ExpenseDtl : {}", expenseDtlDTO);
        ExpenseDtl expenseDtl = expenseDtlMapper.toEntity(expenseDtlDTO);
        expenseDtl = expenseDtlRepository.save(expenseDtl);
        return expenseDtlMapper.toDto(expenseDtl);
    }

    /**
     * Get all the expenseDtls.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ExpenseDtlDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ExpenseDtls");
        return expenseDtlRepository.findAll(pageable)
            .map(expenseDtlMapper::toDto);
    }

    /**
     * Get one expenseDtl by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ExpenseDtlDTO> findOne(Long id) {
        log.debug("Request to get ExpenseDtl : {}", id);
        return expenseDtlRepository.findById(id)
            .map(expenseDtlMapper::toDto);
    }

    /**
     * Delete the expenseDtl by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ExpenseDtl : {}", id);
        expenseDtlRepository.deleteById(id);
    }
}
