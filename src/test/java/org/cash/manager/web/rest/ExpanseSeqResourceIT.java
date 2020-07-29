package org.cash.manager.web.rest;

import org.cash.manager.PrimeCashManagerApp;
import org.cash.manager.domain.ExpanseSeq;
import org.cash.manager.repository.ExpanseSeqRepository;
import org.cash.manager.service.ExpanseSeqService;
import org.cash.manager.service.dto.ExpanseSeqDTO;
import org.cash.manager.service.mapper.ExpanseSeqMapper;
import org.cash.manager.service.dto.ExpanseSeqCriteria;
import org.cash.manager.service.ExpanseSeqQueryService;

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
 * Integration tests for the {@link ExpanseSeqResource} REST controller.
 */
@SpringBootTest(classes = PrimeCashManagerApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class ExpanseSeqResourceIT {

    @Autowired
    private ExpanseSeqRepository expanseSeqRepository;

    @Autowired
    private ExpanseSeqMapper expanseSeqMapper;

    @Autowired
    private ExpanseSeqService expanseSeqService;

    @Autowired
    private ExpanseSeqQueryService expanseSeqQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExpanseSeqMockMvc;

    private ExpanseSeq expanseSeq;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExpanseSeq createEntity(EntityManager em) {
        ExpanseSeq expanseSeq = new ExpanseSeq();
        return expanseSeq;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExpanseSeq createUpdatedEntity(EntityManager em) {
        ExpanseSeq expanseSeq = new ExpanseSeq();
        return expanseSeq;
    }

    @BeforeEach
    public void initTest() {
        expanseSeq = createEntity(em);
    }

    @Test
    @Transactional
    public void createExpanseSeq() throws Exception {
        int databaseSizeBeforeCreate = expanseSeqRepository.findAll().size();

        // Create the ExpanseSeq
        ExpanseSeqDTO expanseSeqDTO = expanseSeqMapper.toDto(expanseSeq);
        restExpanseSeqMockMvc.perform(post("/api/expanse-seqs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(expanseSeqDTO)))
            .andExpect(status().isCreated());

        // Validate the ExpanseSeq in the database
        List<ExpanseSeq> expanseSeqList = expanseSeqRepository.findAll();
        assertThat(expanseSeqList).hasSize(databaseSizeBeforeCreate + 1);
        ExpanseSeq testExpanseSeq = expanseSeqList.get(expanseSeqList.size() - 1);
    }

    @Test
    @Transactional
    public void createExpanseSeqWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = expanseSeqRepository.findAll().size();

        // Create the ExpanseSeq with an existing ID
        expanseSeq.setId(1L);
        ExpanseSeqDTO expanseSeqDTO = expanseSeqMapper.toDto(expanseSeq);

        // An entity with an existing ID cannot be created, so this API call must fail
        restExpanseSeqMockMvc.perform(post("/api/expanse-seqs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(expanseSeqDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ExpanseSeq in the database
        List<ExpanseSeq> expanseSeqList = expanseSeqRepository.findAll();
        assertThat(expanseSeqList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllExpanseSeqs() throws Exception {
        // Initialize the database
        expanseSeqRepository.saveAndFlush(expanseSeq);

        // Get all the expanseSeqList
        restExpanseSeqMockMvc.perform(get("/api/expanse-seqs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(expanseSeq.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getExpanseSeq() throws Exception {
        // Initialize the database
        expanseSeqRepository.saveAndFlush(expanseSeq);

        // Get the expanseSeq
        restExpanseSeqMockMvc.perform(get("/api/expanse-seqs/{id}", expanseSeq.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(expanseSeq.getId().intValue()));
    }


    @Test
    @Transactional
    public void getExpanseSeqsByIdFiltering() throws Exception {
        // Initialize the database
        expanseSeqRepository.saveAndFlush(expanseSeq);

        Long id = expanseSeq.getId();

        defaultExpanseSeqShouldBeFound("id.equals=" + id);
        defaultExpanseSeqShouldNotBeFound("id.notEquals=" + id);

        defaultExpanseSeqShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultExpanseSeqShouldNotBeFound("id.greaterThan=" + id);

        defaultExpanseSeqShouldBeFound("id.lessThanOrEqual=" + id);
        defaultExpanseSeqShouldNotBeFound("id.lessThan=" + id);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultExpanseSeqShouldBeFound(String filter) throws Exception {
        restExpanseSeqMockMvc.perform(get("/api/expanse-seqs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(expanseSeq.getId().intValue())));

        // Check, that the count call also returns 1
        restExpanseSeqMockMvc.perform(get("/api/expanse-seqs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultExpanseSeqShouldNotBeFound(String filter) throws Exception {
        restExpanseSeqMockMvc.perform(get("/api/expanse-seqs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restExpanseSeqMockMvc.perform(get("/api/expanse-seqs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingExpanseSeq() throws Exception {
        // Get the expanseSeq
        restExpanseSeqMockMvc.perform(get("/api/expanse-seqs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExpanseSeq() throws Exception {
        // Initialize the database
        expanseSeqRepository.saveAndFlush(expanseSeq);

        int databaseSizeBeforeUpdate = expanseSeqRepository.findAll().size();

        // Update the expanseSeq
        ExpanseSeq updatedExpanseSeq = expanseSeqRepository.findById(expanseSeq.getId()).get();
        // Disconnect from session so that the updates on updatedExpanseSeq are not directly saved in db
        em.detach(updatedExpanseSeq);
        ExpanseSeqDTO expanseSeqDTO = expanseSeqMapper.toDto(updatedExpanseSeq);

        restExpanseSeqMockMvc.perform(put("/api/expanse-seqs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(expanseSeqDTO)))
            .andExpect(status().isOk());

        // Validate the ExpanseSeq in the database
        List<ExpanseSeq> expanseSeqList = expanseSeqRepository.findAll();
        assertThat(expanseSeqList).hasSize(databaseSizeBeforeUpdate);
        ExpanseSeq testExpanseSeq = expanseSeqList.get(expanseSeqList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingExpanseSeq() throws Exception {
        int databaseSizeBeforeUpdate = expanseSeqRepository.findAll().size();

        // Create the ExpanseSeq
        ExpanseSeqDTO expanseSeqDTO = expanseSeqMapper.toDto(expanseSeq);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExpanseSeqMockMvc.perform(put("/api/expanse-seqs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(expanseSeqDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ExpanseSeq in the database
        List<ExpanseSeq> expanseSeqList = expanseSeqRepository.findAll();
        assertThat(expanseSeqList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteExpanseSeq() throws Exception {
        // Initialize the database
        expanseSeqRepository.saveAndFlush(expanseSeq);

        int databaseSizeBeforeDelete = expanseSeqRepository.findAll().size();

        // Delete the expanseSeq
        restExpanseSeqMockMvc.perform(delete("/api/expanse-seqs/{id}", expanseSeq.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ExpanseSeq> expanseSeqList = expanseSeqRepository.findAll();
        assertThat(expanseSeqList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
