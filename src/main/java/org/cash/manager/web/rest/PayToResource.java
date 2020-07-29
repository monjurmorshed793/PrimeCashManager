package org.cash.manager.web.rest;

import org.cash.manager.service.PayToService;
import org.cash.manager.web.rest.errors.BadRequestAlertException;
import org.cash.manager.service.dto.PayToDTO;
import org.cash.manager.service.dto.PayToCriteria;
import org.cash.manager.service.PayToQueryService;

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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link org.cash.manager.domain.PayTo}.
 */
@RestController
@RequestMapping("/api")
public class PayToResource {

    private final Logger log = LoggerFactory.getLogger(PayToResource.class);

    private static final String ENTITY_NAME = "payTo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PayToService payToService;

    private final PayToQueryService payToQueryService;

    public PayToResource(PayToService payToService, PayToQueryService payToQueryService) {
        this.payToService = payToService;
        this.payToQueryService = payToQueryService;
    }

    /**
     * {@code POST  /pay-tos} : Create a new payTo.
     *
     * @param payToDTO the payToDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new payToDTO, or with status {@code 400 (Bad Request)} if the payTo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pay-tos")
    public ResponseEntity<PayToDTO> createPayTo(@RequestBody PayToDTO payToDTO) throws URISyntaxException {
        log.debug("REST request to save PayTo : {}", payToDTO);
        if (payToDTO.getId() != null) {
            throw new BadRequestAlertException("A new payTo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PayToDTO result = payToService.save(payToDTO);
        return ResponseEntity.created(new URI("/api/pay-tos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pay-tos} : Updates an existing payTo.
     *
     * @param payToDTO the payToDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated payToDTO,
     * or with status {@code 400 (Bad Request)} if the payToDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the payToDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pay-tos")
    public ResponseEntity<PayToDTO> updatePayTo(@RequestBody PayToDTO payToDTO) throws URISyntaxException {
        log.debug("REST request to update PayTo : {}", payToDTO);
        if (payToDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PayToDTO result = payToService.save(payToDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, payToDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /pay-tos} : get all the payTos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of payTos in body.
     */
    @GetMapping("/pay-tos")
    public ResponseEntity<List<PayToDTO>> getAllPayTos(PayToCriteria criteria, Pageable pageable) {
        log.debug("REST request to get PayTos by criteria: {}", criteria);
        Page<PayToDTO> page = payToQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pay-tos/count} : count all the payTos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/pay-tos/count")
    public ResponseEntity<Long> countPayTos(PayToCriteria criteria) {
        log.debug("REST request to count PayTos by criteria: {}", criteria);
        return ResponseEntity.ok().body(payToQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /pay-tos/:id} : get the "id" payTo.
     *
     * @param id the id of the payToDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the payToDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pay-tos/{id}")
    public ResponseEntity<PayToDTO> getPayTo(@PathVariable Long id) {
        log.debug("REST request to get PayTo : {}", id);
        Optional<PayToDTO> payToDTO = payToService.findOne(id);
        return ResponseUtil.wrapOrNotFound(payToDTO);
    }

    /**
     * {@code DELETE  /pay-tos/:id} : delete the "id" payTo.
     *
     * @param id the id of the payToDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pay-tos/{id}")
    public ResponseEntity<Void> deletePayTo(@PathVariable Long id) {
        log.debug("REST request to delete PayTo : {}", id);
        payToService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
