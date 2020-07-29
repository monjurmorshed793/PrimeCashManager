package org.cash.manager.service;

import org.cash.manager.domain.DepositSeq;
import org.cash.manager.repository.DepositSeqRepository;
import org.cash.manager.service.dto.DepositSeqDTO;
import org.cash.manager.service.mapper.DepositSeqMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link DepositSeq}.
 */
@Service
@Transactional
public class DepositSeqService {

    private final Logger log = LoggerFactory.getLogger(DepositSeqService.class);

    private final DepositSeqRepository depositSeqRepository;

    private final DepositSeqMapper depositSeqMapper;

    public DepositSeqService(DepositSeqRepository depositSeqRepository, DepositSeqMapper depositSeqMapper) {
        this.depositSeqRepository = depositSeqRepository;
        this.depositSeqMapper = depositSeqMapper;
    }

    /**
     * Save a depositSeq.
     *
     * @param depositSeqDTO the entity to save.
     * @return the persisted entity.
     */
    public DepositSeqDTO save(DepositSeqDTO depositSeqDTO) {
        log.debug("Request to save DepositSeq : {}", depositSeqDTO);
        DepositSeq depositSeq = depositSeqMapper.toEntity(depositSeqDTO);
        depositSeq = depositSeqRepository.save(depositSeq);
        return depositSeqMapper.toDto(depositSeq);
    }

    /**
     * Get all the depositSeqs.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<DepositSeqDTO> findAll() {
        log.debug("Request to get all DepositSeqs");
        return depositSeqRepository.findAll().stream()
            .map(depositSeqMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one depositSeq by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DepositSeqDTO> findOne(Long id) {
        log.debug("Request to get DepositSeq : {}", id);
        return depositSeqRepository.findById(id)
            .map(depositSeqMapper::toDto);
    }

    /**
     * Delete the depositSeq by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete DepositSeq : {}", id);
        depositSeqRepository.deleteById(id);
    }
}
