package org.cash.manager.service;

import org.cash.manager.domain.PayTo;
import org.cash.manager.repository.PayToRepository;
import org.cash.manager.service.dto.PayToDTO;
import org.cash.manager.service.mapper.PayToMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link PayTo}.
 */
@Service
@Transactional
public class PayToService {

    private final Logger log = LoggerFactory.getLogger(PayToService.class);

    private final PayToRepository payToRepository;

    private final PayToMapper payToMapper;

    public PayToService(PayToRepository payToRepository, PayToMapper payToMapper) {
        this.payToRepository = payToRepository;
        this.payToMapper = payToMapper;
    }

    /**
     * Save a payTo.
     *
     * @param payToDTO the entity to save.
     * @return the persisted entity.
     */
    public PayToDTO save(PayToDTO payToDTO) {
        log.debug("Request to save PayTo : {}", payToDTO);
        PayTo payTo = payToMapper.toEntity(payToDTO);
        payTo = payToRepository.save(payTo);
        return payToMapper.toDto(payTo);
    }

    /**
     * Get all the payTos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PayToDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PayTos");
        return payToRepository.findAll(pageable)
            .map(payToMapper::toDto);
    }

    /**
     * Get one payTo by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PayToDTO> findOne(Long id) {
        log.debug("Request to get PayTo : {}", id);
        return payToRepository.findById(id)
            .map(payToMapper::toDto);
    }

    /**
     * Delete the payTo by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PayTo : {}", id);
        payToRepository.deleteById(id);
    }
}
