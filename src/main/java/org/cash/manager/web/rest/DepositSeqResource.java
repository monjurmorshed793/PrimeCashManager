package org.cash.manager.web.rest;

import org.cash.manager.service.DepositSeqService;
import org.cash.manager.web.rest.errors.BadRequestAlertException;
import org.cash.manager.service.dto.DepositSeqDTO;
import org.cash.manager.service.dto.DepositSeqCriteria;
import org.cash.manager.service.DepositSeqQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link org.cash.manager.domain.DepositSeq}.
 */
@RestController
@RequestMapping("/api")
public class DepositSeqResource {

    private final Logger log = LoggerFactory.getLogger(DepositSeqResource.class);

    private static final String ENTITY_NAME = "depositSeq";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DepositSeqService depositSeqService;

    private final DepositSeqQueryService depositSeqQueryService;

    public DepositSeqResource(DepositSeqService depositSeqService, DepositSeqQueryService depositSeqQueryService) {
        this.depositSeqService = depositSeqService;
        this.depositSeqQueryService = depositSeqQueryService;
    }

    /**
     * {@code POST  /deposit-seqs} : Create a new depositSeq.
     *
     * @param depositSeqDTO the depositSeqDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new depositSeqDTO, or with status {@code 400 (Bad Request)} if the depositSeq has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/deposit-seqs")
    public ResponseEntity<DepositSeqDTO> createDepositSeq(@RequestBody DepositSeqDTO depositSeqDTO) throws URISyntaxException {
        log.debug("REST request to save DepositSeq : {}", depositSeqDTO);
        if (depositSeqDTO.getId() != null) {
            throw new BadRequestAlertException("A new depositSeq cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DepositSeqDTO result = depositSeqService.save(depositSeqDTO);
        return ResponseEntity.created(new URI("/api/deposit-seqs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /deposit-seqs} : Updates an existing depositSeq.
     *
     * @param depositSeqDTO the depositSeqDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated depositSeqDTO,
     * or with status {@code 400 (Bad Request)} if the depositSeqDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the depositSeqDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/deposit-seqs")
    public ResponseEntity<DepositSeqDTO> updateDepositSeq(@RequestBody DepositSeqDTO depositSeqDTO) throws URISyntaxException {
        log.debug("REST request to update DepositSeq : {}", depositSeqDTO);
        if (depositSeqDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DepositSeqDTO result = depositSeqService.save(depositSeqDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, depositSeqDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /deposit-seqs} : get all the depositSeqs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of depositSeqs in body.
     */
    @GetMapping("/deposit-seqs")
    public ResponseEntity<List<DepositSeqDTO>> getAllDepositSeqs(DepositSeqCriteria criteria) {
        log.debug("REST request to get DepositSeqs by criteria: {}", criteria);
        List<DepositSeqDTO> entityList = depositSeqQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /deposit-seqs/count} : count all the depositSeqs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/deposit-seqs/count")
    public ResponseEntity<Long> countDepositSeqs(DepositSeqCriteria criteria) {
        log.debug("REST request to count DepositSeqs by criteria: {}", criteria);
        return ResponseEntity.ok().body(depositSeqQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /deposit-seqs/:id} : get the "id" depositSeq.
     *
     * @param id the id of the depositSeqDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the depositSeqDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/deposit-seqs/{id}")
    public ResponseEntity<DepositSeqDTO> getDepositSeq(@PathVariable Long id) {
        log.debug("REST request to get DepositSeq : {}", id);
        Optional<DepositSeqDTO> depositSeqDTO = depositSeqService.findOne(id);
        return ResponseUtil.wrapOrNotFound(depositSeqDTO);
    }

    /**
     * {@code DELETE  /deposit-seqs/:id} : delete the "id" depositSeq.
     *
     * @param id the id of the depositSeqDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/deposit-seqs/{id}")
    public ResponseEntity<Void> deleteDepositSeq(@PathVariable Long id) {
        log.debug("REST request to delete DepositSeq : {}", id);
        depositSeqService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
