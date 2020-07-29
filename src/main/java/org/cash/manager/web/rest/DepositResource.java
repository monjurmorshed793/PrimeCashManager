package org.cash.manager.web.rest;

import org.cash.manager.service.DepositService;
import org.cash.manager.web.rest.errors.BadRequestAlertException;
import org.cash.manager.service.dto.DepositDTO;
import org.cash.manager.service.dto.DepositCriteria;
import org.cash.manager.service.DepositQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link org.cash.manager.domain.Deposit}.
 */
@RestController
@RequestMapping("/api")
public class DepositResource {

    private final Logger log = LoggerFactory.getLogger(DepositResource.class);

    private static final String ENTITY_NAME = "deposit";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DepositService depositService;

    private final DepositQueryService depositQueryService;

    public DepositResource(DepositService depositService, DepositQueryService depositQueryService) {
        this.depositService = depositService;
        this.depositQueryService = depositQueryService;
    }

    /**
     * {@code POST  /deposits} : Create a new deposit.
     *
     * @param depositDTO the depositDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new depositDTO, or with status {@code 400 (Bad Request)} if the deposit has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/deposits")
    public ResponseEntity<DepositDTO> createDeposit(@Valid @RequestBody DepositDTO depositDTO) throws URISyntaxException {
        log.debug("REST request to save Deposit : {}", depositDTO);
        if (depositDTO.getId() != null) {
            throw new BadRequestAlertException("A new deposit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DepositDTO result = depositService.save(depositDTO);
        return ResponseEntity.created(new URI("/api/deposits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /deposits} : Updates an existing deposit.
     *
     * @param depositDTO the depositDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated depositDTO,
     * or with status {@code 400 (Bad Request)} if the depositDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the depositDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/deposits")
    public ResponseEntity<DepositDTO> updateDeposit(@Valid @RequestBody DepositDTO depositDTO) throws URISyntaxException {
        log.debug("REST request to update Deposit : {}", depositDTO);
        if (depositDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DepositDTO result = depositService.save(depositDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, depositDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /deposits} : get all the deposits.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of deposits in body.
     */
    @GetMapping("/deposits")
    public ResponseEntity<List<DepositDTO>> getAllDeposits(DepositCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Deposits by criteria: {}", criteria);
        Page<DepositDTO> page = depositQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /deposits/count} : count all the deposits.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/deposits/count")
    public ResponseEntity<Long> countDeposits(DepositCriteria criteria) {
        log.debug("REST request to count Deposits by criteria: {}", criteria);
        return ResponseEntity.ok().body(depositQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /deposits/:id} : get the "id" deposit.
     *
     * @param id the id of the depositDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the depositDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/deposits/{id}")
    public ResponseEntity<DepositDTO> getDeposit(@PathVariable Long id) {
        log.debug("REST request to get Deposit : {}", id);
        Optional<DepositDTO> depositDTO = depositService.findOne(id);
        return ResponseUtil.wrapOrNotFound(depositDTO);
    }

    /**
     * {@code DELETE  /deposits/:id} : delete the "id" deposit.
     *
     * @param id the id of the depositDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/deposits/{id}")
    public ResponseEntity<Void> deleteDeposit(@PathVariable Long id) {
        log.debug("REST request to delete Deposit : {}", id);
        depositService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
