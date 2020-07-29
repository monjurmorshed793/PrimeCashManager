package org.cash.manager.service;

import org.cash.manager.domain.ExpanseSeq;
import org.cash.manager.repository.ExpanseSeqRepository;
import org.cash.manager.service.dto.ExpanseSeqDTO;
import org.cash.manager.service.mapper.ExpanseSeqMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link ExpanseSeq}.
 */
@Service
@Transactional
public class ExpanseSeqService {

    private final Logger log = LoggerFactory.getLogger(ExpanseSeqService.class);

    private final ExpanseSeqRepository expanseSeqRepository;

    private final ExpanseSeqMapper expanseSeqMapper;

    public ExpanseSeqService(ExpanseSeqRepository expanseSeqRepository, ExpanseSeqMapper expanseSeqMapper) {
        this.expanseSeqRepository = expanseSeqRepository;
        this.expanseSeqMapper = expanseSeqMapper;
    }

    /**
     * Save a expanseSeq.
     *
     * @param expanseSeqDTO the entity to save.
     * @return the persisted entity.
     */
    public ExpanseSeqDTO save(ExpanseSeqDTO expanseSeqDTO) {
        log.debug("Request to save ExpanseSeq : {}", expanseSeqDTO);
        ExpanseSeq expanseSeq = expanseSeqMapper.toEntity(expanseSeqDTO);
        expanseSeq = expanseSeqRepository.save(expanseSeq);
        return expanseSeqMapper.toDto(expanseSeq);
    }

    /**
     * Get all the expanseSeqs.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ExpanseSeqDTO> findAll() {
        log.debug("Request to get all ExpanseSeqs");
        return expanseSeqRepository.findAll().stream()
            .map(expanseSeqMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one expanseSeq by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ExpanseSeqDTO> findOne(Long id) {
        log.debug("Request to get ExpanseSeq : {}", id);
        return expanseSeqRepository.findById(id)
            .map(expanseSeqMapper::toDto);
    }

    /**
     * Delete the expanseSeq by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ExpanseSeq : {}", id);
        expanseSeqRepository.deleteById(id);
    }
}
