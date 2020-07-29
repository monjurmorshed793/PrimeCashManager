package org.cash.manager.web.rest;

import org.cash.manager.PrimeCashManagerApp;
import org.cash.manager.domain.ExpenseDtl;
import org.cash.manager.domain.Item;
import org.cash.manager.domain.Expense;
import org.cash.manager.repository.ExpenseDtlRepository;
import org.cash.manager.service.ExpenseDtlService;
import org.cash.manager.service.dto.ExpenseDtlDTO;
import org.cash.manager.service.mapper.ExpenseDtlMapper;
import org.cash.manager.service.dto.ExpenseDtlCriteria;
import org.cash.manager.service.ExpenseDtlQueryService;

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
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ExpenseDtlResource} REST controller.
 */
@SpringBootTest(classes = PrimeCashManagerApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class ExpenseDtlResourceIT {

    private static final BigDecimal DEFAULT_QUANTITY = new BigDecimal(1);
    private static final BigDecimal UPDATED_QUANTITY = new BigDecimal(2);
    private static final BigDecimal SMALLER_QUANTITY = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_UNIT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_UNIT_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_UNIT_PRICE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMOUNT = new BigDecimal(1 - 1);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_MODIFIED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFIED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private ExpenseDtlRepository expenseDtlRepository;

    @Autowired
    private ExpenseDtlMapper expenseDtlMapper;

    @Autowired
    private ExpenseDtlService expenseDtlService;

    @Autowired
    private ExpenseDtlQueryService expenseDtlQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExpenseDtlMockMvc;

    private ExpenseDtl expenseDtl;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExpenseDtl createEntity(EntityManager em) {
        ExpenseDtl expenseDtl = new ExpenseDtl()
            .quantity(DEFAULT_QUANTITY)
            .unitPrice(DEFAULT_UNIT_PRICE)
            .amount(DEFAULT_AMOUNT)
            .createdBy(DEFAULT_CREATED_BY)
            .createdOn(DEFAULT_CREATED_ON)
            .modifiedBy(DEFAULT_MODIFIED_BY)
            .modifiedOn(DEFAULT_MODIFIED_ON);
        // Add required entity
        Item item;
        if (TestUtil.findAll(em, Item.class).isEmpty()) {
            item = ItemResourceIT.createEntity(em);
            em.persist(item);
            em.flush();
        } else {
            item = TestUtil.findAll(em, Item.class).get(0);
        }
        expenseDtl.setItem(item);
        return expenseDtl;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExpenseDtl createUpdatedEntity(EntityManager em) {
        ExpenseDtl expenseDtl = new ExpenseDtl()
            .quantity(UPDATED_QUANTITY)
            .unitPrice(UPDATED_UNIT_PRICE)
            .amount(UPDATED_AMOUNT)
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .modifiedBy(UPDATED_MODIFIED_BY)
            .modifiedOn(UPDATED_MODIFIED_ON);
        // Add required entity
        Item item;
        if (TestUtil.findAll(em, Item.class).isEmpty()) {
            item = ItemResourceIT.createUpdatedEntity(em);
            em.persist(item);
            em.flush();
        } else {
            item = TestUtil.findAll(em, Item.class).get(0);
        }
        expenseDtl.setItem(item);
        return expenseDtl;
    }

    @BeforeEach
    public void initTest() {
        expenseDtl = createEntity(em);
    }

    @Test
    @Transactional
    public void createExpenseDtl() throws Exception {
        int databaseSizeBeforeCreate = expenseDtlRepository.findAll().size();

        // Create the ExpenseDtl
        ExpenseDtlDTO expenseDtlDTO = expenseDtlMapper.toDto(expenseDtl);
        restExpenseDtlMockMvc.perform(post("/api/expense-dtls")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(expenseDtlDTO)))
            .andExpect(status().isCreated());

        // Validate the ExpenseDtl in the database
        List<ExpenseDtl> expenseDtlList = expenseDtlRepository.findAll();
        assertThat(expenseDtlList).hasSize(databaseSizeBeforeCreate + 1);
        ExpenseDtl testExpenseDtl = expenseDtlList.get(expenseDtlList.size() - 1);
        assertThat(testExpenseDtl.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testExpenseDtl.getUnitPrice()).isEqualTo(DEFAULT_UNIT_PRICE);
        assertThat(testExpenseDtl.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testExpenseDtl.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testExpenseDtl.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testExpenseDtl.getModifiedBy()).isEqualTo(DEFAULT_MODIFIED_BY);
        assertThat(testExpenseDtl.getModifiedOn()).isEqualTo(DEFAULT_MODIFIED_ON);
    }

    @Test
    @Transactional
    public void createExpenseDtlWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = expenseDtlRepository.findAll().size();

        // Create the ExpenseDtl with an existing ID
        expenseDtl.setId(1L);
        ExpenseDtlDTO expenseDtlDTO = expenseDtlMapper.toDto(expenseDtl);

        // An entity with an existing ID cannot be created, so this API call must fail
        restExpenseDtlMockMvc.perform(post("/api/expense-dtls")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(expenseDtlDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ExpenseDtl in the database
        List<ExpenseDtl> expenseDtlList = expenseDtlRepository.findAll();
        assertThat(expenseDtlList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllExpenseDtls() throws Exception {
        // Initialize the database
        expenseDtlRepository.saveAndFlush(expenseDtl);

        // Get all the expenseDtlList
        restExpenseDtlMockMvc.perform(get("/api/expense-dtls?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(expenseDtl.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY.intValue())))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(DEFAULT_UNIT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].modifiedBy").value(hasItem(DEFAULT_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].modifiedOn").value(hasItem(DEFAULT_MODIFIED_ON.toString())));
    }
    
    @Test
    @Transactional
    public void getExpenseDtl() throws Exception {
        // Initialize the database
        expenseDtlRepository.saveAndFlush(expenseDtl);

        // Get the expenseDtl
        restExpenseDtlMockMvc.perform(get("/api/expense-dtls/{id}", expenseDtl.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(expenseDtl.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY.intValue()))
            .andExpect(jsonPath("$.unitPrice").value(DEFAULT_UNIT_PRICE.intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.modifiedBy").value(DEFAULT_MODIFIED_BY))
            .andExpect(jsonPath("$.modifiedOn").value(DEFAULT_MODIFIED_ON.toString()));
    }


    @Test
    @Transactional
    public void getExpenseDtlsByIdFiltering() throws Exception {
        // Initialize the database
        expenseDtlRepository.saveAndFlush(expenseDtl);

        Long id = expenseDtl.getId();

        defaultExpenseDtlShouldBeFound("id.equals=" + id);
        defaultExpenseDtlShouldNotBeFound("id.notEquals=" + id);

        defaultExpenseDtlShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultExpenseDtlShouldNotBeFound("id.greaterThan=" + id);

        defaultExpenseDtlShouldBeFound("id.lessThanOrEqual=" + id);
        defaultExpenseDtlShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllExpenseDtlsByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        expenseDtlRepository.saveAndFlush(expenseDtl);

        // Get all the expenseDtlList where quantity equals to DEFAULT_QUANTITY
        defaultExpenseDtlShouldBeFound("quantity.equals=" + DEFAULT_QUANTITY);

        // Get all the expenseDtlList where quantity equals to UPDATED_QUANTITY
        defaultExpenseDtlShouldNotBeFound("quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllExpenseDtlsByQuantityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        expenseDtlRepository.saveAndFlush(expenseDtl);

        // Get all the expenseDtlList where quantity not equals to DEFAULT_QUANTITY
        defaultExpenseDtlShouldNotBeFound("quantity.notEquals=" + DEFAULT_QUANTITY);

        // Get all the expenseDtlList where quantity not equals to UPDATED_QUANTITY
        defaultExpenseDtlShouldBeFound("quantity.notEquals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllExpenseDtlsByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        expenseDtlRepository.saveAndFlush(expenseDtl);

        // Get all the expenseDtlList where quantity in DEFAULT_QUANTITY or UPDATED_QUANTITY
        defaultExpenseDtlShouldBeFound("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY);

        // Get all the expenseDtlList where quantity equals to UPDATED_QUANTITY
        defaultExpenseDtlShouldNotBeFound("quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllExpenseDtlsByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        expenseDtlRepository.saveAndFlush(expenseDtl);

        // Get all the expenseDtlList where quantity is not null
        defaultExpenseDtlShouldBeFound("quantity.specified=true");

        // Get all the expenseDtlList where quantity is null
        defaultExpenseDtlShouldNotBeFound("quantity.specified=false");
    }

    @Test
    @Transactional
    public void getAllExpenseDtlsByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        expenseDtlRepository.saveAndFlush(expenseDtl);

        // Get all the expenseDtlList where quantity is greater than or equal to DEFAULT_QUANTITY
        defaultExpenseDtlShouldBeFound("quantity.greaterThanOrEqual=" + DEFAULT_QUANTITY);

        // Get all the expenseDtlList where quantity is greater than or equal to UPDATED_QUANTITY
        defaultExpenseDtlShouldNotBeFound("quantity.greaterThanOrEqual=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllExpenseDtlsByQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        expenseDtlRepository.saveAndFlush(expenseDtl);

        // Get all the expenseDtlList where quantity is less than or equal to DEFAULT_QUANTITY
        defaultExpenseDtlShouldBeFound("quantity.lessThanOrEqual=" + DEFAULT_QUANTITY);

        // Get all the expenseDtlList where quantity is less than or equal to SMALLER_QUANTITY
        defaultExpenseDtlShouldNotBeFound("quantity.lessThanOrEqual=" + SMALLER_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllExpenseDtlsByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        expenseDtlRepository.saveAndFlush(expenseDtl);

        // Get all the expenseDtlList where quantity is less than DEFAULT_QUANTITY
        defaultExpenseDtlShouldNotBeFound("quantity.lessThan=" + DEFAULT_QUANTITY);

        // Get all the expenseDtlList where quantity is less than UPDATED_QUANTITY
        defaultExpenseDtlShouldBeFound("quantity.lessThan=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllExpenseDtlsByQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        expenseDtlRepository.saveAndFlush(expenseDtl);

        // Get all the expenseDtlList where quantity is greater than DEFAULT_QUANTITY
        defaultExpenseDtlShouldNotBeFound("quantity.greaterThan=" + DEFAULT_QUANTITY);

        // Get all the expenseDtlList where quantity is greater than SMALLER_QUANTITY
        defaultExpenseDtlShouldBeFound("quantity.greaterThan=" + SMALLER_QUANTITY);
    }


    @Test
    @Transactional
    public void getAllExpenseDtlsByUnitPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        expenseDtlRepository.saveAndFlush(expenseDtl);

        // Get all the expenseDtlList where unitPrice equals to DEFAULT_UNIT_PRICE
        defaultExpenseDtlShouldBeFound("unitPrice.equals=" + DEFAULT_UNIT_PRICE);

        // Get all the expenseDtlList where unitPrice equals to UPDATED_UNIT_PRICE
        defaultExpenseDtlShouldNotBeFound("unitPrice.equals=" + UPDATED_UNIT_PRICE);
    }

    @Test
    @Transactional
    public void getAllExpenseDtlsByUnitPriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        expenseDtlRepository.saveAndFlush(expenseDtl);

        // Get all the expenseDtlList where unitPrice not equals to DEFAULT_UNIT_PRICE
        defaultExpenseDtlShouldNotBeFound("unitPrice.notEquals=" + DEFAULT_UNIT_PRICE);

        // Get all the expenseDtlList where unitPrice not equals to UPDATED_UNIT_PRICE
        defaultExpenseDtlShouldBeFound("unitPrice.notEquals=" + UPDATED_UNIT_PRICE);
    }

    @Test
    @Transactional
    public void getAllExpenseDtlsByUnitPriceIsInShouldWork() throws Exception {
        // Initialize the database
        expenseDtlRepository.saveAndFlush(expenseDtl);

        // Get all the expenseDtlList where unitPrice in DEFAULT_UNIT_PRICE or UPDATED_UNIT_PRICE
        defaultExpenseDtlShouldBeFound("unitPrice.in=" + DEFAULT_UNIT_PRICE + "," + UPDATED_UNIT_PRICE);

        // Get all the expenseDtlList where unitPrice equals to UPDATED_UNIT_PRICE
        defaultExpenseDtlShouldNotBeFound("unitPrice.in=" + UPDATED_UNIT_PRICE);
    }

    @Test
    @Transactional
    public void getAllExpenseDtlsByUnitPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        expenseDtlRepository.saveAndFlush(expenseDtl);

        // Get all the expenseDtlList where unitPrice is not null
        defaultExpenseDtlShouldBeFound("unitPrice.specified=true");

        // Get all the expenseDtlList where unitPrice is null
        defaultExpenseDtlShouldNotBeFound("unitPrice.specified=false");
    }

    @Test
    @Transactional
    public void getAllExpenseDtlsByUnitPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        expenseDtlRepository.saveAndFlush(expenseDtl);

        // Get all the expenseDtlList where unitPrice is greater than or equal to DEFAULT_UNIT_PRICE
        defaultExpenseDtlShouldBeFound("unitPrice.greaterThanOrEqual=" + DEFAULT_UNIT_PRICE);

        // Get all the expenseDtlList where unitPrice is greater than or equal to UPDATED_UNIT_PRICE
        defaultExpenseDtlShouldNotBeFound("unitPrice.greaterThanOrEqual=" + UPDATED_UNIT_PRICE);
    }

    @Test
    @Transactional
    public void getAllExpenseDtlsByUnitPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        expenseDtlRepository.saveAndFlush(expenseDtl);

        // Get all the expenseDtlList where unitPrice is less than or equal to DEFAULT_UNIT_PRICE
        defaultExpenseDtlShouldBeFound("unitPrice.lessThanOrEqual=" + DEFAULT_UNIT_PRICE);

        // Get all the expenseDtlList where unitPrice is less than or equal to SMALLER_UNIT_PRICE
        defaultExpenseDtlShouldNotBeFound("unitPrice.lessThanOrEqual=" + SMALLER_UNIT_PRICE);
    }

    @Test
    @Transactional
    public void getAllExpenseDtlsByUnitPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        expenseDtlRepository.saveAndFlush(expenseDtl);

        // Get all the expenseDtlList where unitPrice is less than DEFAULT_UNIT_PRICE
        defaultExpenseDtlShouldNotBeFound("unitPrice.lessThan=" + DEFAULT_UNIT_PRICE);

        // Get all the expenseDtlList where unitPrice is less than UPDATED_UNIT_PRICE
        defaultExpenseDtlShouldBeFound("unitPrice.lessThan=" + UPDATED_UNIT_PRICE);
    }

    @Test
    @Transactional
    public void getAllExpenseDtlsByUnitPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        expenseDtlRepository.saveAndFlush(expenseDtl);

        // Get all the expenseDtlList where unitPrice is greater than DEFAULT_UNIT_PRICE
        defaultExpenseDtlShouldNotBeFound("unitPrice.greaterThan=" + DEFAULT_UNIT_PRICE);

        // Get all the expenseDtlList where unitPrice is greater than SMALLER_UNIT_PRICE
        defaultExpenseDtlShouldBeFound("unitPrice.greaterThan=" + SMALLER_UNIT_PRICE);
    }


    @Test
    @Transactional
    public void getAllExpenseDtlsByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        expenseDtlRepository.saveAndFlush(expenseDtl);

        // Get all the expenseDtlList where amount equals to DEFAULT_AMOUNT
        defaultExpenseDtlShouldBeFound("amount.equals=" + DEFAULT_AMOUNT);

        // Get all the expenseDtlList where amount equals to UPDATED_AMOUNT
        defaultExpenseDtlShouldNotBeFound("amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllExpenseDtlsByAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        expenseDtlRepository.saveAndFlush(expenseDtl);

        // Get all the expenseDtlList where amount not equals to DEFAULT_AMOUNT
        defaultExpenseDtlShouldNotBeFound("amount.notEquals=" + DEFAULT_AMOUNT);

        // Get all the expenseDtlList where amount not equals to UPDATED_AMOUNT
        defaultExpenseDtlShouldBeFound("amount.notEquals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllExpenseDtlsByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        expenseDtlRepository.saveAndFlush(expenseDtl);

        // Get all the expenseDtlList where amount in DEFAULT_AMOUNT or UPDATED_AMOUNT
        defaultExpenseDtlShouldBeFound("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT);

        // Get all the expenseDtlList where amount equals to UPDATED_AMOUNT
        defaultExpenseDtlShouldNotBeFound("amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllExpenseDtlsByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        expenseDtlRepository.saveAndFlush(expenseDtl);

        // Get all the expenseDtlList where amount is not null
        defaultExpenseDtlShouldBeFound("amount.specified=true");

        // Get all the expenseDtlList where amount is null
        defaultExpenseDtlShouldNotBeFound("amount.specified=false");
    }

    @Test
    @Transactional
    public void getAllExpenseDtlsByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        expenseDtlRepository.saveAndFlush(expenseDtl);

        // Get all the expenseDtlList where amount is greater than or equal to DEFAULT_AMOUNT
        defaultExpenseDtlShouldBeFound("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT);

        // Get all the expenseDtlList where amount is greater than or equal to UPDATED_AMOUNT
        defaultExpenseDtlShouldNotBeFound("amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllExpenseDtlsByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        expenseDtlRepository.saveAndFlush(expenseDtl);

        // Get all the expenseDtlList where amount is less than or equal to DEFAULT_AMOUNT
        defaultExpenseDtlShouldBeFound("amount.lessThanOrEqual=" + DEFAULT_AMOUNT);

        // Get all the expenseDtlList where amount is less than or equal to SMALLER_AMOUNT
        defaultExpenseDtlShouldNotBeFound("amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllExpenseDtlsByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        expenseDtlRepository.saveAndFlush(expenseDtl);

        // Get all the expenseDtlList where amount is less than DEFAULT_AMOUNT
        defaultExpenseDtlShouldNotBeFound("amount.lessThan=" + DEFAULT_AMOUNT);

        // Get all the expenseDtlList where amount is less than UPDATED_AMOUNT
        defaultExpenseDtlShouldBeFound("amount.lessThan=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllExpenseDtlsByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        expenseDtlRepository.saveAndFlush(expenseDtl);

        // Get all the expenseDtlList where amount is greater than DEFAULT_AMOUNT
        defaultExpenseDtlShouldNotBeFound("amount.greaterThan=" + DEFAULT_AMOUNT);

        // Get all the expenseDtlList where amount is greater than SMALLER_AMOUNT
        defaultExpenseDtlShouldBeFound("amount.greaterThan=" + SMALLER_AMOUNT);
    }


    @Test
    @Transactional
    public void getAllExpenseDtlsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        expenseDtlRepository.saveAndFlush(expenseDtl);

        // Get all the expenseDtlList where createdBy equals to DEFAULT_CREATED_BY
        defaultExpenseDtlShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the expenseDtlList where createdBy equals to UPDATED_CREATED_BY
        defaultExpenseDtlShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllExpenseDtlsByCreatedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        expenseDtlRepository.saveAndFlush(expenseDtl);

        // Get all the expenseDtlList where createdBy not equals to DEFAULT_CREATED_BY
        defaultExpenseDtlShouldNotBeFound("createdBy.notEquals=" + DEFAULT_CREATED_BY);

        // Get all the expenseDtlList where createdBy not equals to UPDATED_CREATED_BY
        defaultExpenseDtlShouldBeFound("createdBy.notEquals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllExpenseDtlsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        expenseDtlRepository.saveAndFlush(expenseDtl);

        // Get all the expenseDtlList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultExpenseDtlShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the expenseDtlList where createdBy equals to UPDATED_CREATED_BY
        defaultExpenseDtlShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllExpenseDtlsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        expenseDtlRepository.saveAndFlush(expenseDtl);

        // Get all the expenseDtlList where createdBy is not null
        defaultExpenseDtlShouldBeFound("createdBy.specified=true");

        // Get all the expenseDtlList where createdBy is null
        defaultExpenseDtlShouldNotBeFound("createdBy.specified=false");
    }
                @Test
    @Transactional
    public void getAllExpenseDtlsByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        expenseDtlRepository.saveAndFlush(expenseDtl);

        // Get all the expenseDtlList where createdBy contains DEFAULT_CREATED_BY
        defaultExpenseDtlShouldBeFound("createdBy.contains=" + DEFAULT_CREATED_BY);

        // Get all the expenseDtlList where createdBy contains UPDATED_CREATED_BY
        defaultExpenseDtlShouldNotBeFound("createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllExpenseDtlsByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        expenseDtlRepository.saveAndFlush(expenseDtl);

        // Get all the expenseDtlList where createdBy does not contain DEFAULT_CREATED_BY
        defaultExpenseDtlShouldNotBeFound("createdBy.doesNotContain=" + DEFAULT_CREATED_BY);

        // Get all the expenseDtlList where createdBy does not contain UPDATED_CREATED_BY
        defaultExpenseDtlShouldBeFound("createdBy.doesNotContain=" + UPDATED_CREATED_BY);
    }


    @Test
    @Transactional
    public void getAllExpenseDtlsByCreatedOnIsEqualToSomething() throws Exception {
        // Initialize the database
        expenseDtlRepository.saveAndFlush(expenseDtl);

        // Get all the expenseDtlList where createdOn equals to DEFAULT_CREATED_ON
        defaultExpenseDtlShouldBeFound("createdOn.equals=" + DEFAULT_CREATED_ON);

        // Get all the expenseDtlList where createdOn equals to UPDATED_CREATED_ON
        defaultExpenseDtlShouldNotBeFound("createdOn.equals=" + UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    public void getAllExpenseDtlsByCreatedOnIsNotEqualToSomething() throws Exception {
        // Initialize the database
        expenseDtlRepository.saveAndFlush(expenseDtl);

        // Get all the expenseDtlList where createdOn not equals to DEFAULT_CREATED_ON
        defaultExpenseDtlShouldNotBeFound("createdOn.notEquals=" + DEFAULT_CREATED_ON);

        // Get all the expenseDtlList where createdOn not equals to UPDATED_CREATED_ON
        defaultExpenseDtlShouldBeFound("createdOn.notEquals=" + UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    public void getAllExpenseDtlsByCreatedOnIsInShouldWork() throws Exception {
        // Initialize the database
        expenseDtlRepository.saveAndFlush(expenseDtl);

        // Get all the expenseDtlList where createdOn in DEFAULT_CREATED_ON or UPDATED_CREATED_ON
        defaultExpenseDtlShouldBeFound("createdOn.in=" + DEFAULT_CREATED_ON + "," + UPDATED_CREATED_ON);

        // Get all the expenseDtlList where createdOn equals to UPDATED_CREATED_ON
        defaultExpenseDtlShouldNotBeFound("createdOn.in=" + UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    public void getAllExpenseDtlsByCreatedOnIsNullOrNotNull() throws Exception {
        // Initialize the database
        expenseDtlRepository.saveAndFlush(expenseDtl);

        // Get all the expenseDtlList where createdOn is not null
        defaultExpenseDtlShouldBeFound("createdOn.specified=true");

        // Get all the expenseDtlList where createdOn is null
        defaultExpenseDtlShouldNotBeFound("createdOn.specified=false");
    }

    @Test
    @Transactional
    public void getAllExpenseDtlsByModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        expenseDtlRepository.saveAndFlush(expenseDtl);

        // Get all the expenseDtlList where modifiedBy equals to DEFAULT_MODIFIED_BY
        defaultExpenseDtlShouldBeFound("modifiedBy.equals=" + DEFAULT_MODIFIED_BY);

        // Get all the expenseDtlList where modifiedBy equals to UPDATED_MODIFIED_BY
        defaultExpenseDtlShouldNotBeFound("modifiedBy.equals=" + UPDATED_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllExpenseDtlsByModifiedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        expenseDtlRepository.saveAndFlush(expenseDtl);

        // Get all the expenseDtlList where modifiedBy not equals to DEFAULT_MODIFIED_BY
        defaultExpenseDtlShouldNotBeFound("modifiedBy.notEquals=" + DEFAULT_MODIFIED_BY);

        // Get all the expenseDtlList where modifiedBy not equals to UPDATED_MODIFIED_BY
        defaultExpenseDtlShouldBeFound("modifiedBy.notEquals=" + UPDATED_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllExpenseDtlsByModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        expenseDtlRepository.saveAndFlush(expenseDtl);

        // Get all the expenseDtlList where modifiedBy in DEFAULT_MODIFIED_BY or UPDATED_MODIFIED_BY
        defaultExpenseDtlShouldBeFound("modifiedBy.in=" + DEFAULT_MODIFIED_BY + "," + UPDATED_MODIFIED_BY);

        // Get all the expenseDtlList where modifiedBy equals to UPDATED_MODIFIED_BY
        defaultExpenseDtlShouldNotBeFound("modifiedBy.in=" + UPDATED_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllExpenseDtlsByModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        expenseDtlRepository.saveAndFlush(expenseDtl);

        // Get all the expenseDtlList where modifiedBy is not null
        defaultExpenseDtlShouldBeFound("modifiedBy.specified=true");

        // Get all the expenseDtlList where modifiedBy is null
        defaultExpenseDtlShouldNotBeFound("modifiedBy.specified=false");
    }
                @Test
    @Transactional
    public void getAllExpenseDtlsByModifiedByContainsSomething() throws Exception {
        // Initialize the database
        expenseDtlRepository.saveAndFlush(expenseDtl);

        // Get all the expenseDtlList where modifiedBy contains DEFAULT_MODIFIED_BY
        defaultExpenseDtlShouldBeFound("modifiedBy.contains=" + DEFAULT_MODIFIED_BY);

        // Get all the expenseDtlList where modifiedBy contains UPDATED_MODIFIED_BY
        defaultExpenseDtlShouldNotBeFound("modifiedBy.contains=" + UPDATED_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllExpenseDtlsByModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        expenseDtlRepository.saveAndFlush(expenseDtl);

        // Get all the expenseDtlList where modifiedBy does not contain DEFAULT_MODIFIED_BY
        defaultExpenseDtlShouldNotBeFound("modifiedBy.doesNotContain=" + DEFAULT_MODIFIED_BY);

        // Get all the expenseDtlList where modifiedBy does not contain UPDATED_MODIFIED_BY
        defaultExpenseDtlShouldBeFound("modifiedBy.doesNotContain=" + UPDATED_MODIFIED_BY);
    }


    @Test
    @Transactional
    public void getAllExpenseDtlsByModifiedOnIsEqualToSomething() throws Exception {
        // Initialize the database
        expenseDtlRepository.saveAndFlush(expenseDtl);

        // Get all the expenseDtlList where modifiedOn equals to DEFAULT_MODIFIED_ON
        defaultExpenseDtlShouldBeFound("modifiedOn.equals=" + DEFAULT_MODIFIED_ON);

        // Get all the expenseDtlList where modifiedOn equals to UPDATED_MODIFIED_ON
        defaultExpenseDtlShouldNotBeFound("modifiedOn.equals=" + UPDATED_MODIFIED_ON);
    }

    @Test
    @Transactional
    public void getAllExpenseDtlsByModifiedOnIsNotEqualToSomething() throws Exception {
        // Initialize the database
        expenseDtlRepository.saveAndFlush(expenseDtl);

        // Get all the expenseDtlList where modifiedOn not equals to DEFAULT_MODIFIED_ON
        defaultExpenseDtlShouldNotBeFound("modifiedOn.notEquals=" + DEFAULT_MODIFIED_ON);

        // Get all the expenseDtlList where modifiedOn not equals to UPDATED_MODIFIED_ON
        defaultExpenseDtlShouldBeFound("modifiedOn.notEquals=" + UPDATED_MODIFIED_ON);
    }

    @Test
    @Transactional
    public void getAllExpenseDtlsByModifiedOnIsInShouldWork() throws Exception {
        // Initialize the database
        expenseDtlRepository.saveAndFlush(expenseDtl);

        // Get all the expenseDtlList where modifiedOn in DEFAULT_MODIFIED_ON or UPDATED_MODIFIED_ON
        defaultExpenseDtlShouldBeFound("modifiedOn.in=" + DEFAULT_MODIFIED_ON + "," + UPDATED_MODIFIED_ON);

        // Get all the expenseDtlList where modifiedOn equals to UPDATED_MODIFIED_ON
        defaultExpenseDtlShouldNotBeFound("modifiedOn.in=" + UPDATED_MODIFIED_ON);
    }

    @Test
    @Transactional
    public void getAllExpenseDtlsByModifiedOnIsNullOrNotNull() throws Exception {
        // Initialize the database
        expenseDtlRepository.saveAndFlush(expenseDtl);

        // Get all the expenseDtlList where modifiedOn is not null
        defaultExpenseDtlShouldBeFound("modifiedOn.specified=true");

        // Get all the expenseDtlList where modifiedOn is null
        defaultExpenseDtlShouldNotBeFound("modifiedOn.specified=false");
    }

    @Test
    @Transactional
    public void getAllExpenseDtlsByItemIsEqualToSomething() throws Exception {
        // Get already existing entity
        Item item = expenseDtl.getItem();
        expenseDtlRepository.saveAndFlush(expenseDtl);
        Long itemId = item.getId();

        // Get all the expenseDtlList where item equals to itemId
        defaultExpenseDtlShouldBeFound("itemId.equals=" + itemId);

        // Get all the expenseDtlList where item equals to itemId + 1
        defaultExpenseDtlShouldNotBeFound("itemId.equals=" + (itemId + 1));
    }


    @Test
    @Transactional
    public void getAllExpenseDtlsByExpenseIsEqualToSomething() throws Exception {
        // Initialize the database
        expenseDtlRepository.saveAndFlush(expenseDtl);
        Expense expense = ExpenseResourceIT.createEntity(em);
        em.persist(expense);
        em.flush();
        expenseDtl.setExpense(expense);
        expenseDtlRepository.saveAndFlush(expenseDtl);
        Long expenseId = expense.getId();

        // Get all the expenseDtlList where expense equals to expenseId
        defaultExpenseDtlShouldBeFound("expenseId.equals=" + expenseId);

        // Get all the expenseDtlList where expense equals to expenseId + 1
        defaultExpenseDtlShouldNotBeFound("expenseId.equals=" + (expenseId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultExpenseDtlShouldBeFound(String filter) throws Exception {
        restExpenseDtlMockMvc.perform(get("/api/expense-dtls?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(expenseDtl.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY.intValue())))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(DEFAULT_UNIT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].modifiedBy").value(hasItem(DEFAULT_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].modifiedOn").value(hasItem(DEFAULT_MODIFIED_ON.toString())));

        // Check, that the count call also returns 1
        restExpenseDtlMockMvc.perform(get("/api/expense-dtls/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultExpenseDtlShouldNotBeFound(String filter) throws Exception {
        restExpenseDtlMockMvc.perform(get("/api/expense-dtls?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restExpenseDtlMockMvc.perform(get("/api/expense-dtls/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingExpenseDtl() throws Exception {
        // Get the expenseDtl
        restExpenseDtlMockMvc.perform(get("/api/expense-dtls/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExpenseDtl() throws Exception {
        // Initialize the database
        expenseDtlRepository.saveAndFlush(expenseDtl);

        int databaseSizeBeforeUpdate = expenseDtlRepository.findAll().size();

        // Update the expenseDtl
        ExpenseDtl updatedExpenseDtl = expenseDtlRepository.findById(expenseDtl.getId()).get();
        // Disconnect from session so that the updates on updatedExpenseDtl are not directly saved in db
        em.detach(updatedExpenseDtl);
        updatedExpenseDtl
            .quantity(UPDATED_QUANTITY)
            .unitPrice(UPDATED_UNIT_PRICE)
            .amount(UPDATED_AMOUNT)
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .modifiedBy(UPDATED_MODIFIED_BY)
            .modifiedOn(UPDATED_MODIFIED_ON);
        ExpenseDtlDTO expenseDtlDTO = expenseDtlMapper.toDto(updatedExpenseDtl);

        restExpenseDtlMockMvc.perform(put("/api/expense-dtls")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(expenseDtlDTO)))
            .andExpect(status().isOk());

        // Validate the ExpenseDtl in the database
        List<ExpenseDtl> expenseDtlList = expenseDtlRepository.findAll();
        assertThat(expenseDtlList).hasSize(databaseSizeBeforeUpdate);
        ExpenseDtl testExpenseDtl = expenseDtlList.get(expenseDtlList.size() - 1);
        assertThat(testExpenseDtl.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testExpenseDtl.getUnitPrice()).isEqualTo(UPDATED_UNIT_PRICE);
        assertThat(testExpenseDtl.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testExpenseDtl.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testExpenseDtl.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testExpenseDtl.getModifiedBy()).isEqualTo(UPDATED_MODIFIED_BY);
        assertThat(testExpenseDtl.getModifiedOn()).isEqualTo(UPDATED_MODIFIED_ON);
    }

    @Test
    @Transactional
    public void updateNonExistingExpenseDtl() throws Exception {
        int databaseSizeBeforeUpdate = expenseDtlRepository.findAll().size();

        // Create the ExpenseDtl
        ExpenseDtlDTO expenseDtlDTO = expenseDtlMapper.toDto(expenseDtl);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExpenseDtlMockMvc.perform(put("/api/expense-dtls")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(expenseDtlDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ExpenseDtl in the database
        List<ExpenseDtl> expenseDtlList = expenseDtlRepository.findAll();
        assertThat(expenseDtlList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteExpenseDtl() throws Exception {
        // Initialize the database
        expenseDtlRepository.saveAndFlush(expenseDtl);

        int databaseSizeBeforeDelete = expenseDtlRepository.findAll().size();

        // Delete the expenseDtl
        restExpenseDtlMockMvc.perform(delete("/api/expense-dtls/{id}", expenseDtl.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ExpenseDtl> expenseDtlList = expenseDtlRepository.findAll();
        assertThat(expenseDtlList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
