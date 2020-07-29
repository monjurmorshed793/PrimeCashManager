package org.cash.manager.service;

import org.cash.manager.domain.Deposit;
import org.cash.manager.repository.DepositRepository;
import org.cash.manager.service.dto.DepositDTO;
import org.cash.manager.service.mapper.DepositMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Deposit}.
 */
@Service
@Transactional
public class DepositService {

    private final Logger log = LoggerFactory.getLogger(DepositService.class);

    private final DepositRepository depositRepository;

    private final DepositMapper depositMapper;

    public DepositService(DepositRepository depositRepository, DepositMapper depositMapper) {
        this.depositRepository = depositRepository;
        this.depositMapper = depositMapper;
    }

    /**
     * Save a deposit.
     *
     * @param depositDTO the entity to save.
     * @return the persisted entity.
     */
    public DepositDTO save(DepositDTO depositDTO) {
        log.debug("Request to save Deposit : {}", depositDTO);
        Deposit deposit = depositMapper.toEntity(depositDTO);
        deposit = depositRepository.save(deposit);
        return depositMapper.toDto(deposit);
    }

    /**
     * Get all the deposits.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DepositDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Deposits");
        return depositRepository.findAll(pageable)
            .map(depositMapper::toDto);
    }

    /**
     * Get one deposit by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DepositDTO> findOne(Long id) {
        log.debug("Request to get Deposit : {}", id);
        return depositRepository.findById(id)
            .map(depositMapper::toDto);
    }

    /**
     * Delete the deposit by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Deposit : {}", id);
        depositRepository.deleteById(id);
    }
}
