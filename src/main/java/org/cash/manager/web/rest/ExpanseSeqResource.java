package org.cash.manager.web.rest;

import org.cash.manager.service.ExpanseSeqService;
import org.cash.manager.web.rest.errors.BadRequestAlertException;
import org.cash.manager.service.dto.ExpanseSeqDTO;
import org.cash.manager.service.dto.ExpanseSeqCriteria;
import org.cash.manager.service.ExpanseSeqQueryService;

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
 * REST controller for managing {@link org.cash.manager.domain.ExpanseSeq}.
 */
@RestController
@RequestMapping("/api")
public class ExpanseSeqResource {

    private final Logger log = LoggerFactory.getLogger(ExpanseSeqResource.class);

    private static final String ENTITY_NAME = "expanseSeq";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExpanseSeqService expanseSeqService;

    private final ExpanseSeqQueryService expanseSeqQueryService;

    public ExpanseSeqResource(ExpanseSeqService expanseSeqService, ExpanseSeqQueryService expanseSeqQueryService) {
        this.expanseSeqService = expanseSeqService;
        this.expanseSeqQueryService = expanseSeqQueryService;
    }

    /**
     * {@code POST  /expanse-seqs} : Create a new expanseSeq.
     *
     * @param expanseSeqDTO the expanseSeqDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new expanseSeqDTO, or with status {@code 400 (Bad Request)} if the expanseSeq has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/expanse-seqs")
    public ResponseEntity<ExpanseSeqDTO> createExpanseSeq(@RequestBody ExpanseSeqDTO expanseSeqDTO) throws URISyntaxException {
        log.debug("REST request to save ExpanseSeq : {}", expanseSeqDTO);
        if (expanseSeqDTO.getId() != null) {
            throw new BadRequestAlertException("A new expanseSeq cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExpanseSeqDTO result = expanseSeqService.save(expanseSeqDTO);
        return ResponseEntity.created(new URI("/api/expanse-seqs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /expanse-seqs} : Updates an existing expanseSeq.
     *
     * @param expanseSeqDTO the expanseSeqDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated expanseSeqDTO,
     * or with status {@code 400 (Bad Request)} if the expanseSeqDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the expanseSeqDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/expanse-seqs")
    public ResponseEntity<ExpanseSeqDTO> updateExpanseSeq(@RequestBody ExpanseSeqDTO expanseSeqDTO) throws URISyntaxException {
        log.debug("REST request to update ExpanseSeq : {}", expanseSeqDTO);
        if (expanseSeqDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ExpanseSeqDTO result = expanseSeqService.save(expanseSeqDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, expanseSeqDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /expanse-seqs} : get all the expanseSeqs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of expanseSeqs in body.
     */
    @GetMapping("/expanse-seqs")
    public ResponseEntity<List<ExpanseSeqDTO>> getAllExpanseSeqs(ExpanseSeqCriteria criteria) {
        log.debug("REST request to get ExpanseSeqs by criteria: {}", criteria);
        List<ExpanseSeqDTO> entityList = expanseSeqQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /expanse-seqs/count} : count all the expanseSeqs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/expanse-seqs/count")
    public ResponseEntity<Long> countExpanseSeqs(ExpanseSeqCriteria criteria) {
        log.debug("REST request to count ExpanseSeqs by criteria: {}", criteria);
        return ResponseEntity.ok().body(expanseSeqQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /expanse-seqs/:id} : get the "id" expanseSeq.
     *
     * @param id the id of the expanseSeqDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the expanseSeqDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/expanse-seqs/{id}")
    public ResponseEntity<ExpanseSeqDTO> getExpanseSeq(@PathVariable Long id) {
        log.debug("REST request to get ExpanseSeq : {}", id);
        Optional<ExpanseSeqDTO> expanseSeqDTO = expanseSeqService.findOne(id);
        return ResponseUtil.wrapOrNotFound(expanseSeqDTO);
    }

    /**
     * {@code DELETE  /expanse-seqs/:id} : delete the "id" expanseSeq.
     *
     * @param id the id of the expanseSeqDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/expanse-seqs/{id}")
    public ResponseEntity<Void> deleteExpanseSeq(@PathVariable Long id) {
        log.debug("REST request to delete ExpanseSeq : {}", id);
        expanseSeqService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
