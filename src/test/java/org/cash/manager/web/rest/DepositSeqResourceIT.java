package org.cash.manager.web.rest;

import org.cash.manager.PrimeCashManagerApp;
import org.cash.manager.domain.DepositSeq;
import org.cash.manager.repository.DepositSeqRepository;
import org.cash.manager.service.DepositSeqService;
import org.cash.manager.service.dto.DepositSeqDTO;
import org.cash.manager.service.mapper.DepositSeqMapper;
import org.cash.manager.service.dto.DepositSeqCriteria;
import org.cash.manager.service.DepositSeqQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link DepositSeqResource} REST controller.
 */
@SpringBootTest(classes = PrimeCashManagerApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class DepositSeqResourceIT {

    @Autowired
    private DepositSeqRepository depositSeqRepository;

    @Autowired
    private DepositSeqMapper depositSeqMapper;

    @Autowired
    private DepositSeqService depositSeqService;

    @Autowired
    private DepositSeqQueryService depositSeqQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDepositSeqMockMvc;

    private DepositSeq depositSeq;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DepositSeq createEntity(EntityManager em) {
        DepositSeq depositSeq = new DepositSeq();
        return depositSeq;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DepositSeq createUpdatedEntity(EntityManager em) {
        DepositSeq depositSeq = new DepositSeq();
        return depositSeq;
    }

    @BeforeEach
    public void initTest() {
        depositSeq = createEntity(em);
    }

    @Test
    @Transactional
    public void createDepositSeq() throws Exception {
        int databaseSizeBeforeCreate = depositSeqRepository.findAll().size();

        // Create the DepositSeq
        DepositSeqDTO depositSeqDTO = depositSeqMapper.toDto(depositSeq);
        restDepositSeqMockMvc.perform(post("/api/deposit-seqs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(depositSeqDTO)))
            .andExpect(status().isCreated());

        // Validate the DepositSeq in the database
        List<DepositSeq> depositSeqList = depositSeqRepository.findAll();
        assertThat(depositSeqList).hasSize(databaseSizeBeforeCreate + 1);
        DepositSeq testDepositSeq = depositSeqList.get(depositSeqList.size() - 1);
    }

    @Test
    @Transactional
    public void createDepositSeqWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = depositSeqRepository.findAll().size();

        // Create the DepositSeq with an existing ID
        depositSeq.setId(1L);
        DepositSeqDTO depositSeqDTO = depositSeqMapper.toDto(depositSeq);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDepositSeqMockMvc.perform(post("/api/deposit-seqs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(depositSeqDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DepositSeq in the database
        List<DepositSeq> depositSeqList = depositSeqRepository.findAll();
        assertThat(depositSeqList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllDepositSeqs() throws Exception {
        // Initialize the database
        depositSeqRepository.saveAndFlush(depositSeq);

        // Get all the depositSeqList
        restDepositSeqMockMvc.perform(get("/api/deposit-seqs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(depositSeq.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getDepositSeq() throws Exception {
        // Initialize the database
        depositSeqRepository.saveAndFlush(depositSeq);

        // Get the depositSeq
        restDepositSeqMockMvc.perform(get("/api/deposit-seqs/{id}", depositSeq.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(depositSeq.getId().intValue()));
    }


    @Test
    @Transactional
    public void getDepositSeqsByIdFiltering() throws Exception {
        // Initialize the database
        depositSeqRepository.saveAndFlush(depositSeq);

        Long id = depositSeq.getId();

        defaultDepositSeqShouldBeFound("id.equals=" + id);
        defaultDepositSeqShouldNotBeFound("id.notEquals=" + id);

        defaultDepositSeqShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDepositSeqShouldNotBeFound("id.greaterThan=" + id);

        defaultDepositSeqShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDepositSeqShouldNotBeFound("id.lessThan=" + id);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDepositSeqShouldBeFound(String filter) throws Exception {
        restDepositSeqMockMvc.perform(get("/api/deposit-seqs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(depositSeq.getId().intValue())));

        // Check, that the count call also returns 1
        restDepositSeqMockMvc.perform(get("/api/deposit-seqs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDepositSeqShouldNotBeFound(String filter) throws Exception {
        restDepositSeqMockMvc.perform(get("/api/deposit-seqs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDepositSeqMockMvc.perform(get("/api/deposit-seqs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingDepositSeq() throws Exception {
        // Get the depositSeq
        restDepositSeqMockMvc.perform(get("/api/deposit-seqs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDepositSeq() throws Exception {
        // Initialize the database
        depositSeqRepository.saveAndFlush(depositSeq);

        int databaseSizeBeforeUpdate = depositSeqRepository.findAll().size();

        // Update the depositSeq
        DepositSeq updatedDepositSeq = depositSeqRepository.findById(depositSeq.getId()).get();
        // Disconnect from session so that the updates on updatedDepositSeq are not directly saved in db
        em.detach(updatedDepositSeq);
        DepositSeqDTO depositSeqDTO = depositSeqMapper.toDto(updatedDepositSeq);

        restDepositSeqMockMvc.perform(put("/api/deposit-seqs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(depositSeqDTO)))
            .andExpect(status().isOk());

        // Validate the DepositSeq in the database
        List<DepositSeq> depositSeqList = depositSeqRepository.findAll();
        assertThat(depositSeqList).hasSize(databaseSizeBeforeUpdate);
        DepositSeq testDepositSeq = depositSeqList.get(depositSeqList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingDepositSeq() throws Exception {
        int databaseSizeBeforeUpdate = depositSeqRepository.findAll().size();

        // Create the DepositSeq
        DepositSeqDTO depositSeqDTO = depositSeqMapper.toDto(depositSeq);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepositSeqMockMvc.perform(put("/api/deposit-seqs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(depositSeqDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DepositSeq in the database
        List<DepositSeq> depositSeqList = depositSeqRepository.findAll();
        assertThat(depositSeqList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDepositSeq() throws Exception {
        // Initialize the database
        depositSeqRepository.saveAndFlush(depositSeq);

        int databaseSizeBeforeDelete = depositSeqRepository.findAll().size();

        // Delete the depositSeq
        restDepositSeqMockMvc.perform(delete("/api/deposit-seqs/{id}", depositSeq.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DepositSeq> depositSeqList = depositSeqRepository.findAll();
        assertThat(depositSeqList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
