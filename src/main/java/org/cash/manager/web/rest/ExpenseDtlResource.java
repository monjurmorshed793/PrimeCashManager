package org.cash.manager.web.rest;

import org.cash.manager.service.ExpenseDtlService;
import org.cash.manager.web.rest.errors.BadRequestAlertException;
import org.cash.manager.service.dto.ExpenseDtlDTO;
import org.cash.manager.service.dto.ExpenseDtlCriteria;
import org.cash.manager.service.ExpenseDtlQueryService;

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
 * REST controller for managing {@link org.cash.manager.domain.ExpenseDtl}.
 */
@RestController
@RequestMapping("/api")
public class ExpenseDtlResource {

    private final Logger log = LoggerFactory.getLogger(ExpenseDtlResource.class);

    private static final String ENTITY_NAME = "expenseDtl";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExpenseDtlService expenseDtlService;

    private final ExpenseDtlQueryService expenseDtlQueryService;

    public ExpenseDtlResource(ExpenseDtlService expenseDtlService, ExpenseDtlQueryService expenseDtlQueryService) {
        this.expenseDtlService = expenseDtlService;
        this.expenseDtlQueryService = expenseDtlQueryService;
    }

    /**
     * {@code POST  /expense-dtls} : Create a new expenseDtl.
     *
     * @param expenseDtlDTO the expenseDtlDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new expenseDtlDTO, or with status {@code 400 (Bad Request)} if the expenseDtl has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/expense-dtls")
    public ResponseEntity<ExpenseDtlDTO> createExpenseDtl(@Valid @RequestBody ExpenseDtlDTO expenseDtlDTO) throws URISyntaxException {
        log.debug("REST request to save ExpenseDtl : {}", expenseDtlDTO);
        if (expenseDtlDTO.getId() != null) {
            throw new BadRequestAlertException("A new expenseDtl cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExpenseDtlDTO result = expenseDtlService.save(expenseDtlDTO);
        return ResponseEntity.created(new URI("/api/expense-dtls/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /expense-dtls} : Updates an existing expenseDtl.
     *
     * @param expenseDtlDTO the expenseDtlDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated expenseDtlDTO,
     * or with status {@code 400 (Bad Request)} if the expenseDtlDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the expenseDtlDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/expense-dtls")
    public ResponseEntity<ExpenseDtlDTO> updateExpenseDtl(@Valid @RequestBody ExpenseDtlDTO expenseDtlDTO) throws URISyntaxException {
        log.debug("REST request to update ExpenseDtl : {}", expenseDtlDTO);
        if (expenseDtlDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ExpenseDtlDTO result = expenseDtlService.save(expenseDtlDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, expenseDtlDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /expense-dtls} : get all the expenseDtls.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of expenseDtls in body.
     */
    @GetMapping("/expense-dtls")
    public ResponseEntity<List<ExpenseDtlDTO>> getAllExpenseDtls(ExpenseDtlCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ExpenseDtls by criteria: {}", criteria);
        Page<ExpenseDtlDTO> page = expenseDtlQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /expense-dtls/count} : count all the expenseDtls.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/expense-dtls/count")
    public ResponseEntity<Long> countExpenseDtls(ExpenseDtlCriteria criteria) {
        log.debug("REST request to count ExpenseDtls by criteria: {}", criteria);
        return ResponseEntity.ok().body(expenseDtlQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /expense-dtls/:id} : get the "id" expenseDtl.
     *
     * @param id the id of the expenseDtlDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the expenseDtlDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/expense-dtls/{id}")
    public ResponseEntity<ExpenseDtlDTO> getExpenseDtl(@PathVariable Long id) {
        log.debug("REST request to get ExpenseDtl : {}", id);
        Optional<ExpenseDtlDTO> expenseDtlDTO = expenseDtlService.findOne(id);
        return ResponseUtil.wrapOrNotFound(expenseDtlDTO);
    }

    /**
     * {@code DELETE  /expense-dtls/:id} : delete the "id" expenseDtl.
     *
     * @param id the id of the expenseDtlDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/expense-dtls/{id}")
    public ResponseEntity<Void> deleteExpenseDtl(@PathVariable Long id) {
        log.debug("REST request to delete ExpenseDtl : {}", id);
        expenseDtlService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
