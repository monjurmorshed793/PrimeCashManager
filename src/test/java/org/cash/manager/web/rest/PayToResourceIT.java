package org.cash.manager.web.rest;

import org.cash.manager.PrimeCashManagerApp;
import org.cash.manager.domain.PayTo;
import org.cash.manager.repository.PayToRepository;
import org.cash.manager.service.PayToService;
import org.cash.manager.service.dto.PayToDTO;
import org.cash.manager.service.mapper.PayToMapper;
import org.cash.manager.service.dto.PayToCriteria;
import org.cash.manager.service.PayToQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link PayToResource} REST controller.
 */
@SpringBootTest(classes = PrimeCashManagerApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class PayToResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_MODIFIED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFIED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private PayToRepository payToRepository;

    @Autowired
    private PayToMapper payToMapper;

    @Autowired
    private PayToService payToService;

    @Autowired
    private PayToQueryService payToQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPayToMockMvc;

    private PayTo payTo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PayTo createEntity(EntityManager em) {
        PayTo payTo = new PayTo()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .createdBy(DEFAULT_CREATED_BY)
            .createdOn(DEFAULT_CREATED_ON)
            .modifiedBy(DEFAULT_MODIFIED_BY)
            .modifiedOn(DEFAULT_MODIFIED_ON);
        return payTo;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PayTo createUpdatedEntity(EntityManager em) {
        PayTo payTo = new PayTo()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .modifiedBy(UPDATED_MODIFIED_BY)
            .modifiedOn(UPDATED_MODIFIED_ON);
        return payTo;
    }

    @BeforeEach
    public void initTest() {
        payTo = createEntity(em);
    }

    @Test
    @Transactional
    public void createPayTo() throws Exception {
        int databaseSizeBeforeCreate = payToRepository.findAll().size();

        // Create the PayTo
        PayToDTO payToDTO = payToMapper.toDto(payTo);
        restPayToMockMvc.perform(post("/api/pay-tos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(payToDTO)))
            .andExpect(status().isCreated());

        // Validate the PayTo in the database
        List<PayTo> payToList = payToRepository.findAll();
        assertThat(payToList).hasSize(databaseSizeBeforeCreate + 1);
        PayTo testPayTo = payToList.get(payToList.size() - 1);
        assertThat(testPayTo.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPayTo.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPayTo.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testPayTo.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testPayTo.getModifiedBy()).isEqualTo(DEFAULT_MODIFIED_BY);
        assertThat(testPayTo.getModifiedOn()).isEqualTo(DEFAULT_MODIFIED_ON);
    }

    @Test
    @Transactional
    public void createPayToWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = payToRepository.findAll().size();

        // Create the PayTo with an existing ID
        payTo.setId(1L);
        PayToDTO payToDTO = payToMapper.toDto(payTo);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPayToMockMvc.perform(post("/api/pay-tos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(payToDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PayTo in the database
        List<PayTo> payToList = payToRepository.findAll();
        assertThat(payToList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllPayTos() throws Exception {
        // Initialize the database
        payToRepository.saveAndFlush(payTo);

        // Get all the payToList
        restPayToMockMvc.perform(get("/api/pay-tos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(payTo.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].modifiedBy").value(hasItem(DEFAULT_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].modifiedOn").value(hasItem(DEFAULT_MODIFIED_ON.toString())));
    }
    
    @Test
    @Transactional
    public void getPayTo() throws Exception {
        // Initialize the database
        payToRepository.saveAndFlush(payTo);

        // Get the payTo
        restPayToMockMvc.perform(get("/api/pay-tos/{id}", payTo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(payTo.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.modifiedBy").value(DEFAULT_MODIFIED_BY))
            .andExpect(jsonPath("$.modifiedOn").value(DEFAULT_MODIFIED_ON.toString()));
    }


    @Test
    @Transactional
    public void getPayTosByIdFiltering() throws Exception {
        // Initialize the database
        payToRepository.saveAndFlush(payTo);

        Long id = payTo.getId();

        defaultPayToShouldBeFound("id.equals=" + id);
        defaultPayToShouldNotBeFound("id.notEquals=" + id);

        defaultPayToShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPayToShouldNotBeFound("id.greaterThan=" + id);

        defaultPayToShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPayToShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllPayTosByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        payToRepository.saveAndFlush(payTo);

        // Get all the payToList where name equals to DEFAULT_NAME
        defaultPayToShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the payToList where name equals to UPDATED_NAME
        defaultPayToShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPayTosByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        payToRepository.saveAndFlush(payTo);

        // Get all the payToList where name not equals to DEFAULT_NAME
        defaultPayToShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the payToList where name not equals to UPDATED_NAME
        defaultPayToShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPayTosByNameIsInShouldWork() throws Exception {
        // Initialize the database
        payToRepository.saveAndFlush(payTo);

        // Get all the payToList where name in DEFAULT_NAME or UPDATED_NAME
        defaultPayToShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the payToList where name equals to UPDATED_NAME
        defaultPayToShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPayTosByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        payToRepository.saveAndFlush(payTo);

        // Get all the payToList where name is not null
        defaultPayToShouldBeFound("name.specified=true");

        // Get all the payToList where name is null
        defaultPayToShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllPayTosByNameContainsSomething() throws Exception {
        // Initialize the database
        payToRepository.saveAndFlush(payTo);

        // Get all the payToList where name contains DEFAULT_NAME
        defaultPayToShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the payToList where name contains UPDATED_NAME
        defaultPayToShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPayTosByNameNotContainsSomething() throws Exception {
        // Initialize the database
        payToRepository.saveAndFlush(payTo);

        // Get all the payToList where name does not contain DEFAULT_NAME
        defaultPayToShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the payToList where name does not contain UPDATED_NAME
        defaultPayToShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllPayTosByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        payToRepository.saveAndFlush(payTo);

        // Get all the payToList where createdBy equals to DEFAULT_CREATED_BY
        defaultPayToShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the payToList where createdBy equals to UPDATED_CREATED_BY
        defaultPayToShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllPayTosByCreatedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        payToRepository.saveAndFlush(payTo);

        // Get all the payToList where createdBy not equals to DEFAULT_CREATED_BY
        defaultPayToShouldNotBeFound("createdBy.notEquals=" + DEFAULT_CREATED_BY);

        // Get all the payToList where createdBy not equals to UPDATED_CREATED_BY
        defaultPayToShouldBeFound("createdBy.notEquals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllPayTosByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        payToRepository.saveAndFlush(payTo);

        // Get all the payToList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultPayToShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the payToList where createdBy equals to UPDATED_CREATED_BY
        defaultPayToShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllPayTosByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        payToRepository.saveAndFlush(payTo);

        // Get all the payToList where createdBy is not null
        defaultPayToShouldBeFound("createdBy.specified=true");

        // Get all the payToList where createdBy is null
        defaultPayToShouldNotBeFound("createdBy.specified=false");
    }
                @Test
    @Transactional
    public void getAllPayTosByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        payToRepository.saveAndFlush(payTo);

        // Get all the payToList where createdBy contains DEFAULT_CREATED_BY
        defaultPayToShouldBeFound("createdBy.contains=" + DEFAULT_CREATED_BY);

        // Get all the payToList where createdBy contains UPDATED_CREATED_BY
        defaultPayToShouldNotBeFound("createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllPayTosByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        payToRepository.saveAndFlush(payTo);

        // Get all the payToList where createdBy does not contain DEFAULT_CREATED_BY
        defaultPayToShouldNotBeFound("createdBy.doesNotContain=" + DEFAULT_CREATED_BY);

        // Get all the payToList where createdBy does not contain UPDATED_CREATED_BY
        defaultPayToShouldBeFound("createdBy.doesNotContain=" + UPDATED_CREATED_BY);
    }


    @Test
    @Transactional
    public void getAllPayTosByCreatedOnIsEqualToSomething() throws Exception {
        // Initialize the database
        payToRepository.saveAndFlush(payTo);

        // Get all the payToList where createdOn equals to DEFAULT_CREATED_ON
        defaultPayToShouldBeFound("createdOn.equals=" + DEFAULT_CREATED_ON);

        // Get all the payToList where createdOn equals to UPDATED_CREATED_ON
        defaultPayToShouldNotBeFound("createdOn.equals=" + UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    public void getAllPayTosByCreatedOnIsNotEqualToSomething() throws Exception {
        // Initialize the database
        payToRepository.saveAndFlush(payTo);

        // Get all the payToList where createdOn not equals to DEFAULT_CREATED_ON
        defaultPayToShouldNotBeFound("createdOn.notEquals=" + DEFAULT_CREATED_ON);

        // Get all the payToList where createdOn not equals to UPDATED_CREATED_ON
        defaultPayToShouldBeFound("createdOn.notEquals=" + UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    public void getAllPayTosByCreatedOnIsInShouldWork() throws Exception {
        // Initialize the database
        payToRepository.saveAndFlush(payTo);

        // Get all the payToList where createdOn in DEFAULT_CREATED_ON or UPDATED_CREATED_ON
        defaultPayToShouldBeFound("createdOn.in=" + DEFAULT_CREATED_ON + "," + UPDATED_CREATED_ON);

        // Get all the payToList where createdOn equals to UPDATED_CREATED_ON
        defaultPayToShouldNotBeFound("createdOn.in=" + UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    public void getAllPayTosByCreatedOnIsNullOrNotNull() throws Exception {
        // Initialize the database
        payToRepository.saveAndFlush(payTo);

        // Get all the payToList where createdOn is not null
        defaultPayToShouldBeFound("createdOn.specified=true");

        // Get all the payToList where createdOn is null
        defaultPayToShouldNotBeFound("createdOn.specified=false");
    }

    @Test
    @Transactional
    public void getAllPayTosByModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        payToRepository.saveAndFlush(payTo);

        // Get all the payToList where modifiedBy equals to DEFAULT_MODIFIED_BY
        defaultPayToShouldBeFound("modifiedBy.equals=" + DEFAULT_MODIFIED_BY);

        // Get all the payToList where modifiedBy equals to UPDATED_MODIFIED_BY
        defaultPayToShouldNotBeFound("modifiedBy.equals=" + UPDATED_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllPayTosByModifiedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        payToRepository.saveAndFlush(payTo);

        // Get all the payToList where modifiedBy not equals to DEFAULT_MODIFIED_BY
        defaultPayToShouldNotBeFound("modifiedBy.notEquals=" + DEFAULT_MODIFIED_BY);

        // Get all the payToList where modifiedBy not equals to UPDATED_MODIFIED_BY
        defaultPayToShouldBeFound("modifiedBy.notEquals=" + UPDATED_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllPayTosByModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        payToRepository.saveAndFlush(payTo);

        // Get all the payToList where modifiedBy in DEFAULT_MODIFIED_BY or UPDATED_MODIFIED_BY
        defaultPayToShouldBeFound("modifiedBy.in=" + DEFAULT_MODIFIED_BY + "," + UPDATED_MODIFIED_BY);

        // Get all the payToList where modifiedBy equals to UPDATED_MODIFIED_BY
        defaultPayToShouldNotBeFound("modifiedBy.in=" + UPDATED_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllPayTosByModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        payToRepository.saveAndFlush(payTo);

        // Get all the payToList where modifiedBy is not null
        defaultPayToShouldBeFound("modifiedBy.specified=true");

        // Get all the payToList where modifiedBy is null
        defaultPayToShouldNotBeFound("modifiedBy.specified=false");
    }
                @Test
    @Transactional
    public void getAllPayTosByModifiedByContainsSomething() throws Exception {
        // Initialize the database
        payToRepository.saveAndFlush(payTo);

        // Get all the payToList where modifiedBy contains DEFAULT_MODIFIED_BY
        defaultPayToShouldBeFound("modifiedBy.contains=" + DEFAULT_MODIFIED_BY);

        // Get all the payToList where modifiedBy contains UPDATED_MODIFIED_BY
        defaultPayToShouldNotBeFound("modifiedBy.contains=" + UPDATED_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllPayTosByModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        payToRepository.saveAndFlush(payTo);

        // Get all the payToList where modifiedBy does not contain DEFAULT_MODIFIED_BY
        defaultPayToShouldNotBeFound("modifiedBy.doesNotContain=" + DEFAULT_MODIFIED_BY);

        // Get all the payToList where modifiedBy does not contain UPDATED_MODIFIED_BY
        defaultPayToShouldBeFound("modifiedBy.doesNotContain=" + UPDATED_MODIFIED_BY);
    }


    @Test
    @Transactional
    public void getAllPayTosByModifiedOnIsEqualToSomething() throws Exception {
        // Initialize the database
        payToRepository.saveAndFlush(payTo);

        // Get all the payToList where modifiedOn equals to DEFAULT_MODIFIED_ON
        defaultPayToShouldBeFound("modifiedOn.equals=" + DEFAULT_MODIFIED_ON);

        // Get all the payToList where modifiedOn equals to UPDATED_MODIFIED_ON
        defaultPayToShouldNotBeFound("modifiedOn.equals=" + UPDATED_MODIFIED_ON);
    }

    @Test
    @Transactional
    public void getAllPayTosByModifiedOnIsNotEqualToSomething() throws Exception {
        // Initialize the database
        payToRepository.saveAndFlush(payTo);

        // Get all the payToList where modifiedOn not equals to DEFAULT_MODIFIED_ON
        defaultPayToShouldNotBeFound("modifiedOn.notEquals=" + DEFAULT_MODIFIED_ON);

        // Get all the payToList where modifiedOn not equals to UPDATED_MODIFIED_ON
        defaultPayToShouldBeFound("modifiedOn.notEquals=" + UPDATED_MODIFIED_ON);
    }

    @Test
    @Transactional
    public void getAllPayTosByModifiedOnIsInShouldWork() throws Exception {
        // Initialize the database
        payToRepository.saveAndFlush(payTo);

        // Get all the payToList where modifiedOn in DEFAULT_MODIFIED_ON or UPDATED_MODIFIED_ON
        defaultPayToShouldBeFound("modifiedOn.in=" + DEFAULT_MODIFIED_ON + "," + UPDATED_MODIFIED_ON);

        // Get all the payToList where modifiedOn equals to UPDATED_MODIFIED_ON
        defaultPayToShouldNotBeFound("modifiedOn.in=" + UPDATED_MODIFIED_ON);
    }

    @Test
    @Transactional
    public void getAllPayTosByModifiedOnIsNullOrNotNull() throws Exception {
        // Initialize the database
        payToRepository.saveAndFlush(payTo);

        // Get all the payToList where modifiedOn is not null
        defaultPayToShouldBeFound("modifiedOn.specified=true");

        // Get all the payToList where modifiedOn is null
        defaultPayToShouldNotBeFound("modifiedOn.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPayToShouldBeFound(String filter) throws Exception {
        restPayToMockMvc.perform(get("/api/pay-tos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(payTo.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].modifiedBy").value(hasItem(DEFAULT_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].modifiedOn").value(hasItem(DEFAULT_MODIFIED_ON.toString())));

        // Check, that the count call also returns 1
        restPayToMockMvc.perform(get("/api/pay-tos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPayToShouldNotBeFound(String filter) throws Exception {
        restPayToMockMvc.perform(get("/api/pay-tos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPayToMockMvc.perform(get("/api/pay-tos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingPayTo() throws Exception {
        // Get the payTo
        restPayToMockMvc.perform(get("/api/pay-tos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePayTo() throws Exception {
        // Initialize the database
        payToRepository.saveAndFlush(payTo);

        int databaseSizeBeforeUpdate = payToRepository.findAll().size();

        // Update the payTo
        PayTo updatedPayTo = payToRepository.findById(payTo.getId()).get();
        // Disconnect from session so that the updates on updatedPayTo are not directly saved in db
        em.detach(updatedPayTo);
        updatedPayTo
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .modifiedBy(UPDATED_MODIFIED_BY)
            .modifiedOn(UPDATED_MODIFIED_ON);
        PayToDTO payToDTO = payToMapper.toDto(updatedPayTo);

        restPayToMockMvc.perform(put("/api/pay-tos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(payToDTO)))
            .andExpect(status().isOk());

        // Validate the PayTo in the database
        List<PayTo> payToList = payToRepository.findAll();
        assertThat(payToList).hasSize(databaseSizeBeforeUpdate);
        PayTo testPayTo = payToList.get(payToList.size() - 1);
        assertThat(testPayTo.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPayTo.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPayTo.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testPayTo.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testPayTo.getModifiedBy()).isEqualTo(UPDATED_MODIFIED_BY);
        assertThat(testPayTo.getModifiedOn()).isEqualTo(UPDATED_MODIFIED_ON);
    }

    @Test
    @Transactional
    public void updateNonExistingPayTo() throws Exception {
        int databaseSizeBeforeUpdate = payToRepository.findAll().size();

        // Create the PayTo
        PayToDTO payToDTO = payToMapper.toDto(payTo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPayToMockMvc.perform(put("/api/pay-tos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(payToDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PayTo in the database
        List<PayTo> payToList = payToRepository.findAll();
        assertThat(payToList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePayTo() throws Exception {
        // Initialize the database
        payToRepository.saveAndFlush(payTo);

        int databaseSizeBeforeDelete = payToRepository.findAll().size();

        // Delete the payTo
        restPayToMockMvc.perform(delete("/api/pay-tos/{id}", payTo.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PayTo> payToList = payToRepository.findAll();
        assertThat(payToList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
