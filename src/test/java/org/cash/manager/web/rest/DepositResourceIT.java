package org.cash.manager.web.rest;

import org.cash.manager.PrimeCashManagerApp;
import org.cash.manager.domain.Deposit;
import org.cash.manager.repository.DepositRepository;
import org.cash.manager.service.DepositService;
import org.cash.manager.service.dto.DepositDTO;
import org.cash.manager.service.mapper.DepositMapper;
import org.cash.manager.service.dto.DepositCriteria;
import org.cash.manager.service.DepositQueryService;

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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.cash.manager.domain.enumeration.DepositMedium;
/**
 * Integration tests for the {@link DepositResource} REST controller.
 */
@SpringBootTest(classes = PrimeCashManagerApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class DepositResourceIT {

    private static final String DEFAULT_LOGIN_ID = "AAAAAAAAAA";
    private static final String UPDATED_LOGIN_ID = "BBBBBBBBBB";

    private static final Integer DEFAULT_DEPOSIT_NO = 1;
    private static final Integer UPDATED_DEPOSIT_NO = 2;
    private static final Integer SMALLER_DEPOSIT_NO = 1 - 1;

    private static final String DEFAULT_DEPOSIT_BY = "AAAAAAAAAA";
    private static final String UPDATED_DEPOSIT_BY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DEPOSIT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DEPOSIT_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DEPOSIT_DATE = LocalDate.ofEpochDay(-1L);

    private static final DepositMedium DEFAULT_MEDIUM = DepositMedium.ATM;
    private static final DepositMedium UPDATED_MEDIUM = DepositMedium.BANK;

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMOUNT = new BigDecimal(1 - 1);

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_POSTED = false;
    private static final Boolean UPDATED_IS_POSTED = true;

    private static final Instant DEFAULT_POST_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_POST_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_MODIFIED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFIED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private DepositRepository depositRepository;

    @Autowired
    private DepositMapper depositMapper;

    @Autowired
    private DepositService depositService;

    @Autowired
    private DepositQueryService depositQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDepositMockMvc;

    private Deposit deposit;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Deposit createEntity(EntityManager em) {
        Deposit deposit = new Deposit()
            .loginId(DEFAULT_LOGIN_ID)
            .depositNo(DEFAULT_DEPOSIT_NO)
            .depositBy(DEFAULT_DEPOSIT_BY)
            .depositDate(DEFAULT_DEPOSIT_DATE)
            .medium(DEFAULT_MEDIUM)
            .amount(DEFAULT_AMOUNT)
            .note(DEFAULT_NOTE)
            .isPosted(DEFAULT_IS_POSTED)
            .postDate(DEFAULT_POST_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .createdOn(DEFAULT_CREATED_ON)
            .modifiedBy(DEFAULT_MODIFIED_BY)
            .modifiedOn(DEFAULT_MODIFIED_ON);
        return deposit;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Deposit createUpdatedEntity(EntityManager em) {
        Deposit deposit = new Deposit()
            .loginId(UPDATED_LOGIN_ID)
            .depositNo(UPDATED_DEPOSIT_NO)
            .depositBy(UPDATED_DEPOSIT_BY)
            .depositDate(UPDATED_DEPOSIT_DATE)
            .medium(UPDATED_MEDIUM)
            .amount(UPDATED_AMOUNT)
            .note(UPDATED_NOTE)
            .isPosted(UPDATED_IS_POSTED)
            .postDate(UPDATED_POST_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .modifiedBy(UPDATED_MODIFIED_BY)
            .modifiedOn(UPDATED_MODIFIED_ON);
        return deposit;
    }

    @BeforeEach
    public void initTest() {
        deposit = createEntity(em);
    }

    @Test
    @Transactional
    public void createDeposit() throws Exception {
        int databaseSizeBeforeCreate = depositRepository.findAll().size();

        // Create the Deposit
        DepositDTO depositDTO = depositMapper.toDto(deposit);
        restDepositMockMvc.perform(post("/api/deposits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(depositDTO)))
            .andExpect(status().isCreated());

        // Validate the Deposit in the database
        List<Deposit> depositList = depositRepository.findAll();
        assertThat(depositList).hasSize(databaseSizeBeforeCreate + 1);
        Deposit testDeposit = depositList.get(depositList.size() - 1);
        assertThat(testDeposit.getLoginId()).isEqualTo(DEFAULT_LOGIN_ID);
        assertThat(testDeposit.getDepositNo()).isEqualTo(DEFAULT_DEPOSIT_NO);
        assertThat(testDeposit.getDepositBy()).isEqualTo(DEFAULT_DEPOSIT_BY);
        assertThat(testDeposit.getDepositDate()).isEqualTo(DEFAULT_DEPOSIT_DATE);
        assertThat(testDeposit.getMedium()).isEqualTo(DEFAULT_MEDIUM);
        assertThat(testDeposit.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testDeposit.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testDeposit.isIsPosted()).isEqualTo(DEFAULT_IS_POSTED);
        assertThat(testDeposit.getPostDate()).isEqualTo(DEFAULT_POST_DATE);
        assertThat(testDeposit.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testDeposit.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testDeposit.getModifiedBy()).isEqualTo(DEFAULT_MODIFIED_BY);
        assertThat(testDeposit.getModifiedOn()).isEqualTo(DEFAULT_MODIFIED_ON);
    }

    @Test
    @Transactional
    public void createDepositWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = depositRepository.findAll().size();

        // Create the Deposit with an existing ID
        deposit.setId(1L);
        DepositDTO depositDTO = depositMapper.toDto(deposit);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDepositMockMvc.perform(post("/api/deposits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(depositDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Deposit in the database
        List<Deposit> depositList = depositRepository.findAll();
        assertThat(depositList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkLoginIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = depositRepository.findAll().size();
        // set the field null
        deposit.setLoginId(null);

        // Create the Deposit, which fails.
        DepositDTO depositDTO = depositMapper.toDto(deposit);

        restDepositMockMvc.perform(post("/api/deposits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(depositDTO)))
            .andExpect(status().isBadRequest());

        List<Deposit> depositList = depositRepository.findAll();
        assertThat(depositList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDepositByIsRequired() throws Exception {
        int databaseSizeBeforeTest = depositRepository.findAll().size();
        // set the field null
        deposit.setDepositBy(null);

        // Create the Deposit, which fails.
        DepositDTO depositDTO = depositMapper.toDto(deposit);

        restDepositMockMvc.perform(post("/api/deposits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(depositDTO)))
            .andExpect(status().isBadRequest());

        List<Deposit> depositList = depositRepository.findAll();
        assertThat(depositList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDepositDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = depositRepository.findAll().size();
        // set the field null
        deposit.setDepositDate(null);

        // Create the Deposit, which fails.
        DepositDTO depositDTO = depositMapper.toDto(deposit);

        restDepositMockMvc.perform(post("/api/deposits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(depositDTO)))
            .andExpect(status().isBadRequest());

        List<Deposit> depositList = depositRepository.findAll();
        assertThat(depositList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMediumIsRequired() throws Exception {
        int databaseSizeBeforeTest = depositRepository.findAll().size();
        // set the field null
        deposit.setMedium(null);

        // Create the Deposit, which fails.
        DepositDTO depositDTO = depositMapper.toDto(deposit);

        restDepositMockMvc.perform(post("/api/deposits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(depositDTO)))
            .andExpect(status().isBadRequest());

        List<Deposit> depositList = depositRepository.findAll();
        assertThat(depositList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = depositRepository.findAll().size();
        // set the field null
        deposit.setAmount(null);

        // Create the Deposit, which fails.
        DepositDTO depositDTO = depositMapper.toDto(deposit);

        restDepositMockMvc.perform(post("/api/deposits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(depositDTO)))
            .andExpect(status().isBadRequest());

        List<Deposit> depositList = depositRepository.findAll();
        assertThat(depositList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDeposits() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get all the depositList
        restDepositMockMvc.perform(get("/api/deposits?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deposit.getId().intValue())))
            .andExpect(jsonPath("$.[*].loginId").value(hasItem(DEFAULT_LOGIN_ID)))
            .andExpect(jsonPath("$.[*].depositNo").value(hasItem(DEFAULT_DEPOSIT_NO)))
            .andExpect(jsonPath("$.[*].depositBy").value(hasItem(DEFAULT_DEPOSIT_BY)))
            .andExpect(jsonPath("$.[*].depositDate").value(hasItem(DEFAULT_DEPOSIT_DATE.toString())))
            .andExpect(jsonPath("$.[*].medium").value(hasItem(DEFAULT_MEDIUM.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE.toString())))
            .andExpect(jsonPath("$.[*].isPosted").value(hasItem(DEFAULT_IS_POSTED.booleanValue())))
            .andExpect(jsonPath("$.[*].postDate").value(hasItem(DEFAULT_POST_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].modifiedBy").value(hasItem(DEFAULT_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].modifiedOn").value(hasItem(DEFAULT_MODIFIED_ON.toString())));
    }
    
    @Test
    @Transactional
    public void getDeposit() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get the deposit
        restDepositMockMvc.perform(get("/api/deposits/{id}", deposit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(deposit.getId().intValue()))
            .andExpect(jsonPath("$.loginId").value(DEFAULT_LOGIN_ID))
            .andExpect(jsonPath("$.depositNo").value(DEFAULT_DEPOSIT_NO))
            .andExpect(jsonPath("$.depositBy").value(DEFAULT_DEPOSIT_BY))
            .andExpect(jsonPath("$.depositDate").value(DEFAULT_DEPOSIT_DATE.toString()))
            .andExpect(jsonPath("$.medium").value(DEFAULT_MEDIUM.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE.toString()))
            .andExpect(jsonPath("$.isPosted").value(DEFAULT_IS_POSTED.booleanValue()))
            .andExpect(jsonPath("$.postDate").value(DEFAULT_POST_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.modifiedBy").value(DEFAULT_MODIFIED_BY))
            .andExpect(jsonPath("$.modifiedOn").value(DEFAULT_MODIFIED_ON.toString()));
    }


    @Test
    @Transactional
    public void getDepositsByIdFiltering() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        Long id = deposit.getId();

        defaultDepositShouldBeFound("id.equals=" + id);
        defaultDepositShouldNotBeFound("id.notEquals=" + id);

        defaultDepositShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDepositShouldNotBeFound("id.greaterThan=" + id);

        defaultDepositShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDepositShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllDepositsByLoginIdIsEqualToSomething() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get all the depositList where loginId equals to DEFAULT_LOGIN_ID
        defaultDepositShouldBeFound("loginId.equals=" + DEFAULT_LOGIN_ID);

        // Get all the depositList where loginId equals to UPDATED_LOGIN_ID
        defaultDepositShouldNotBeFound("loginId.equals=" + UPDATED_LOGIN_ID);
    }

    @Test
    @Transactional
    public void getAllDepositsByLoginIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get all the depositList where loginId not equals to DEFAULT_LOGIN_ID
        defaultDepositShouldNotBeFound("loginId.notEquals=" + DEFAULT_LOGIN_ID);

        // Get all the depositList where loginId not equals to UPDATED_LOGIN_ID
        defaultDepositShouldBeFound("loginId.notEquals=" + UPDATED_LOGIN_ID);
    }

    @Test
    @Transactional
    public void getAllDepositsByLoginIdIsInShouldWork() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get all the depositList where loginId in DEFAULT_LOGIN_ID or UPDATED_LOGIN_ID
        defaultDepositShouldBeFound("loginId.in=" + DEFAULT_LOGIN_ID + "," + UPDATED_LOGIN_ID);

        // Get all the depositList where loginId equals to UPDATED_LOGIN_ID
        defaultDepositShouldNotBeFound("loginId.in=" + UPDATED_LOGIN_ID);
    }

    @Test
    @Transactional
    public void getAllDepositsByLoginIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get all the depositList where loginId is not null
        defaultDepositShouldBeFound("loginId.specified=true");

        // Get all the depositList where loginId is null
        defaultDepositShouldNotBeFound("loginId.specified=false");
    }
                @Test
    @Transactional
    public void getAllDepositsByLoginIdContainsSomething() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get all the depositList where loginId contains DEFAULT_LOGIN_ID
        defaultDepositShouldBeFound("loginId.contains=" + DEFAULT_LOGIN_ID);

        // Get all the depositList where loginId contains UPDATED_LOGIN_ID
        defaultDepositShouldNotBeFound("loginId.contains=" + UPDATED_LOGIN_ID);
    }

    @Test
    @Transactional
    public void getAllDepositsByLoginIdNotContainsSomething() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get all the depositList where loginId does not contain DEFAULT_LOGIN_ID
        defaultDepositShouldNotBeFound("loginId.doesNotContain=" + DEFAULT_LOGIN_ID);

        // Get all the depositList where loginId does not contain UPDATED_LOGIN_ID
        defaultDepositShouldBeFound("loginId.doesNotContain=" + UPDATED_LOGIN_ID);
    }


    @Test
    @Transactional
    public void getAllDepositsByDepositNoIsEqualToSomething() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get all the depositList where depositNo equals to DEFAULT_DEPOSIT_NO
        defaultDepositShouldBeFound("depositNo.equals=" + DEFAULT_DEPOSIT_NO);

        // Get all the depositList where depositNo equals to UPDATED_DEPOSIT_NO
        defaultDepositShouldNotBeFound("depositNo.equals=" + UPDATED_DEPOSIT_NO);
    }

    @Test
    @Transactional
    public void getAllDepositsByDepositNoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get all the depositList where depositNo not equals to DEFAULT_DEPOSIT_NO
        defaultDepositShouldNotBeFound("depositNo.notEquals=" + DEFAULT_DEPOSIT_NO);

        // Get all the depositList where depositNo not equals to UPDATED_DEPOSIT_NO
        defaultDepositShouldBeFound("depositNo.notEquals=" + UPDATED_DEPOSIT_NO);
    }

    @Test
    @Transactional
    public void getAllDepositsByDepositNoIsInShouldWork() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get all the depositList where depositNo in DEFAULT_DEPOSIT_NO or UPDATED_DEPOSIT_NO
        defaultDepositShouldBeFound("depositNo.in=" + DEFAULT_DEPOSIT_NO + "," + UPDATED_DEPOSIT_NO);

        // Get all the depositList where depositNo equals to UPDATED_DEPOSIT_NO
        defaultDepositShouldNotBeFound("depositNo.in=" + UPDATED_DEPOSIT_NO);
    }

    @Test
    @Transactional
    public void getAllDepositsByDepositNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get all the depositList where depositNo is not null
        defaultDepositShouldBeFound("depositNo.specified=true");

        // Get all the depositList where depositNo is null
        defaultDepositShouldNotBeFound("depositNo.specified=false");
    }

    @Test
    @Transactional
    public void getAllDepositsByDepositNoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get all the depositList where depositNo is greater than or equal to DEFAULT_DEPOSIT_NO
        defaultDepositShouldBeFound("depositNo.greaterThanOrEqual=" + DEFAULT_DEPOSIT_NO);

        // Get all the depositList where depositNo is greater than or equal to UPDATED_DEPOSIT_NO
        defaultDepositShouldNotBeFound("depositNo.greaterThanOrEqual=" + UPDATED_DEPOSIT_NO);
    }

    @Test
    @Transactional
    public void getAllDepositsByDepositNoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get all the depositList where depositNo is less than or equal to DEFAULT_DEPOSIT_NO
        defaultDepositShouldBeFound("depositNo.lessThanOrEqual=" + DEFAULT_DEPOSIT_NO);

        // Get all the depositList where depositNo is less than or equal to SMALLER_DEPOSIT_NO
        defaultDepositShouldNotBeFound("depositNo.lessThanOrEqual=" + SMALLER_DEPOSIT_NO);
    }

    @Test
    @Transactional
    public void getAllDepositsByDepositNoIsLessThanSomething() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get all the depositList where depositNo is less than DEFAULT_DEPOSIT_NO
        defaultDepositShouldNotBeFound("depositNo.lessThan=" + DEFAULT_DEPOSIT_NO);

        // Get all the depositList where depositNo is less than UPDATED_DEPOSIT_NO
        defaultDepositShouldBeFound("depositNo.lessThan=" + UPDATED_DEPOSIT_NO);
    }

    @Test
    @Transactional
    public void getAllDepositsByDepositNoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get all the depositList where depositNo is greater than DEFAULT_DEPOSIT_NO
        defaultDepositShouldNotBeFound("depositNo.greaterThan=" + DEFAULT_DEPOSIT_NO);

        // Get all the depositList where depositNo is greater than SMALLER_DEPOSIT_NO
        defaultDepositShouldBeFound("depositNo.greaterThan=" + SMALLER_DEPOSIT_NO);
    }


    @Test
    @Transactional
    public void getAllDepositsByDepositByIsEqualToSomething() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get all the depositList where depositBy equals to DEFAULT_DEPOSIT_BY
        defaultDepositShouldBeFound("depositBy.equals=" + DEFAULT_DEPOSIT_BY);

        // Get all the depositList where depositBy equals to UPDATED_DEPOSIT_BY
        defaultDepositShouldNotBeFound("depositBy.equals=" + UPDATED_DEPOSIT_BY);
    }

    @Test
    @Transactional
    public void getAllDepositsByDepositByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get all the depositList where depositBy not equals to DEFAULT_DEPOSIT_BY
        defaultDepositShouldNotBeFound("depositBy.notEquals=" + DEFAULT_DEPOSIT_BY);

        // Get all the depositList where depositBy not equals to UPDATED_DEPOSIT_BY
        defaultDepositShouldBeFound("depositBy.notEquals=" + UPDATED_DEPOSIT_BY);
    }

    @Test
    @Transactional
    public void getAllDepositsByDepositByIsInShouldWork() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get all the depositList where depositBy in DEFAULT_DEPOSIT_BY or UPDATED_DEPOSIT_BY
        defaultDepositShouldBeFound("depositBy.in=" + DEFAULT_DEPOSIT_BY + "," + UPDATED_DEPOSIT_BY);

        // Get all the depositList where depositBy equals to UPDATED_DEPOSIT_BY
        defaultDepositShouldNotBeFound("depositBy.in=" + UPDATED_DEPOSIT_BY);
    }

    @Test
    @Transactional
    public void getAllDepositsByDepositByIsNullOrNotNull() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get all the depositList where depositBy is not null
        defaultDepositShouldBeFound("depositBy.specified=true");

        // Get all the depositList where depositBy is null
        defaultDepositShouldNotBeFound("depositBy.specified=false");
    }
                @Test
    @Transactional
    public void getAllDepositsByDepositByContainsSomething() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get all the depositList where depositBy contains DEFAULT_DEPOSIT_BY
        defaultDepositShouldBeFound("depositBy.contains=" + DEFAULT_DEPOSIT_BY);

        // Get all the depositList where depositBy contains UPDATED_DEPOSIT_BY
        defaultDepositShouldNotBeFound("depositBy.contains=" + UPDATED_DEPOSIT_BY);
    }

    @Test
    @Transactional
    public void getAllDepositsByDepositByNotContainsSomething() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get all the depositList where depositBy does not contain DEFAULT_DEPOSIT_BY
        defaultDepositShouldNotBeFound("depositBy.doesNotContain=" + DEFAULT_DEPOSIT_BY);

        // Get all the depositList where depositBy does not contain UPDATED_DEPOSIT_BY
        defaultDepositShouldBeFound("depositBy.doesNotContain=" + UPDATED_DEPOSIT_BY);
    }


    @Test
    @Transactional
    public void getAllDepositsByDepositDateIsEqualToSomething() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get all the depositList where depositDate equals to DEFAULT_DEPOSIT_DATE
        defaultDepositShouldBeFound("depositDate.equals=" + DEFAULT_DEPOSIT_DATE);

        // Get all the depositList where depositDate equals to UPDATED_DEPOSIT_DATE
        defaultDepositShouldNotBeFound("depositDate.equals=" + UPDATED_DEPOSIT_DATE);
    }

    @Test
    @Transactional
    public void getAllDepositsByDepositDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get all the depositList where depositDate not equals to DEFAULT_DEPOSIT_DATE
        defaultDepositShouldNotBeFound("depositDate.notEquals=" + DEFAULT_DEPOSIT_DATE);

        // Get all the depositList where depositDate not equals to UPDATED_DEPOSIT_DATE
        defaultDepositShouldBeFound("depositDate.notEquals=" + UPDATED_DEPOSIT_DATE);
    }

    @Test
    @Transactional
    public void getAllDepositsByDepositDateIsInShouldWork() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get all the depositList where depositDate in DEFAULT_DEPOSIT_DATE or UPDATED_DEPOSIT_DATE
        defaultDepositShouldBeFound("depositDate.in=" + DEFAULT_DEPOSIT_DATE + "," + UPDATED_DEPOSIT_DATE);

        // Get all the depositList where depositDate equals to UPDATED_DEPOSIT_DATE
        defaultDepositShouldNotBeFound("depositDate.in=" + UPDATED_DEPOSIT_DATE);
    }

    @Test
    @Transactional
    public void getAllDepositsByDepositDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get all the depositList where depositDate is not null
        defaultDepositShouldBeFound("depositDate.specified=true");

        // Get all the depositList where depositDate is null
        defaultDepositShouldNotBeFound("depositDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllDepositsByDepositDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get all the depositList where depositDate is greater than or equal to DEFAULT_DEPOSIT_DATE
        defaultDepositShouldBeFound("depositDate.greaterThanOrEqual=" + DEFAULT_DEPOSIT_DATE);

        // Get all the depositList where depositDate is greater than or equal to UPDATED_DEPOSIT_DATE
        defaultDepositShouldNotBeFound("depositDate.greaterThanOrEqual=" + UPDATED_DEPOSIT_DATE);
    }

    @Test
    @Transactional
    public void getAllDepositsByDepositDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get all the depositList where depositDate is less than or equal to DEFAULT_DEPOSIT_DATE
        defaultDepositShouldBeFound("depositDate.lessThanOrEqual=" + DEFAULT_DEPOSIT_DATE);

        // Get all the depositList where depositDate is less than or equal to SMALLER_DEPOSIT_DATE
        defaultDepositShouldNotBeFound("depositDate.lessThanOrEqual=" + SMALLER_DEPOSIT_DATE);
    }

    @Test
    @Transactional
    public void getAllDepositsByDepositDateIsLessThanSomething() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get all the depositList where depositDate is less than DEFAULT_DEPOSIT_DATE
        defaultDepositShouldNotBeFound("depositDate.lessThan=" + DEFAULT_DEPOSIT_DATE);

        // Get all the depositList where depositDate is less than UPDATED_DEPOSIT_DATE
        defaultDepositShouldBeFound("depositDate.lessThan=" + UPDATED_DEPOSIT_DATE);
    }

    @Test
    @Transactional
    public void getAllDepositsByDepositDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get all the depositList where depositDate is greater than DEFAULT_DEPOSIT_DATE
        defaultDepositShouldNotBeFound("depositDate.greaterThan=" + DEFAULT_DEPOSIT_DATE);

        // Get all the depositList where depositDate is greater than SMALLER_DEPOSIT_DATE
        defaultDepositShouldBeFound("depositDate.greaterThan=" + SMALLER_DEPOSIT_DATE);
    }


    @Test
    @Transactional
    public void getAllDepositsByMediumIsEqualToSomething() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get all the depositList where medium equals to DEFAULT_MEDIUM
        defaultDepositShouldBeFound("medium.equals=" + DEFAULT_MEDIUM);

        // Get all the depositList where medium equals to UPDATED_MEDIUM
        defaultDepositShouldNotBeFound("medium.equals=" + UPDATED_MEDIUM);
    }

    @Test
    @Transactional
    public void getAllDepositsByMediumIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get all the depositList where medium not equals to DEFAULT_MEDIUM
        defaultDepositShouldNotBeFound("medium.notEquals=" + DEFAULT_MEDIUM);

        // Get all the depositList where medium not equals to UPDATED_MEDIUM
        defaultDepositShouldBeFound("medium.notEquals=" + UPDATED_MEDIUM);
    }

    @Test
    @Transactional
    public void getAllDepositsByMediumIsInShouldWork() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get all the depositList where medium in DEFAULT_MEDIUM or UPDATED_MEDIUM
        defaultDepositShouldBeFound("medium.in=" + DEFAULT_MEDIUM + "," + UPDATED_MEDIUM);

        // Get all the depositList where medium equals to UPDATED_MEDIUM
        defaultDepositShouldNotBeFound("medium.in=" + UPDATED_MEDIUM);
    }

    @Test
    @Transactional
    public void getAllDepositsByMediumIsNullOrNotNull() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get all the depositList where medium is not null
        defaultDepositShouldBeFound("medium.specified=true");

        // Get all the depositList where medium is null
        defaultDepositShouldNotBeFound("medium.specified=false");
    }

    @Test
    @Transactional
    public void getAllDepositsByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get all the depositList where amount equals to DEFAULT_AMOUNT
        defaultDepositShouldBeFound("amount.equals=" + DEFAULT_AMOUNT);

        // Get all the depositList where amount equals to UPDATED_AMOUNT
        defaultDepositShouldNotBeFound("amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllDepositsByAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get all the depositList where amount not equals to DEFAULT_AMOUNT
        defaultDepositShouldNotBeFound("amount.notEquals=" + DEFAULT_AMOUNT);

        // Get all the depositList where amount not equals to UPDATED_AMOUNT
        defaultDepositShouldBeFound("amount.notEquals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllDepositsByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get all the depositList where amount in DEFAULT_AMOUNT or UPDATED_AMOUNT
        defaultDepositShouldBeFound("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT);

        // Get all the depositList where amount equals to UPDATED_AMOUNT
        defaultDepositShouldNotBeFound("amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllDepositsByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get all the depositList where amount is not null
        defaultDepositShouldBeFound("amount.specified=true");

        // Get all the depositList where amount is null
        defaultDepositShouldNotBeFound("amount.specified=false");
    }

    @Test
    @Transactional
    public void getAllDepositsByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get all the depositList where amount is greater than or equal to DEFAULT_AMOUNT
        defaultDepositShouldBeFound("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT);

        // Get all the depositList where amount is greater than or equal to UPDATED_AMOUNT
        defaultDepositShouldNotBeFound("amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllDepositsByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get all the depositList where amount is less than or equal to DEFAULT_AMOUNT
        defaultDepositShouldBeFound("amount.lessThanOrEqual=" + DEFAULT_AMOUNT);

        // Get all the depositList where amount is less than or equal to SMALLER_AMOUNT
        defaultDepositShouldNotBeFound("amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllDepositsByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get all the depositList where amount is less than DEFAULT_AMOUNT
        defaultDepositShouldNotBeFound("amount.lessThan=" + DEFAULT_AMOUNT);

        // Get all the depositList where amount is less than UPDATED_AMOUNT
        defaultDepositShouldBeFound("amount.lessThan=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllDepositsByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get all the depositList where amount is greater than DEFAULT_AMOUNT
        defaultDepositShouldNotBeFound("amount.greaterThan=" + DEFAULT_AMOUNT);

        // Get all the depositList where amount is greater than SMALLER_AMOUNT
        defaultDepositShouldBeFound("amount.greaterThan=" + SMALLER_AMOUNT);
    }


    @Test
    @Transactional
    public void getAllDepositsByIsPostedIsEqualToSomething() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get all the depositList where isPosted equals to DEFAULT_IS_POSTED
        defaultDepositShouldBeFound("isPosted.equals=" + DEFAULT_IS_POSTED);

        // Get all the depositList where isPosted equals to UPDATED_IS_POSTED
        defaultDepositShouldNotBeFound("isPosted.equals=" + UPDATED_IS_POSTED);
    }

    @Test
    @Transactional
    public void getAllDepositsByIsPostedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get all the depositList where isPosted not equals to DEFAULT_IS_POSTED
        defaultDepositShouldNotBeFound("isPosted.notEquals=" + DEFAULT_IS_POSTED);

        // Get all the depositList where isPosted not equals to UPDATED_IS_POSTED
        defaultDepositShouldBeFound("isPosted.notEquals=" + UPDATED_IS_POSTED);
    }

    @Test
    @Transactional
    public void getAllDepositsByIsPostedIsInShouldWork() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get all the depositList where isPosted in DEFAULT_IS_POSTED or UPDATED_IS_POSTED
        defaultDepositShouldBeFound("isPosted.in=" + DEFAULT_IS_POSTED + "," + UPDATED_IS_POSTED);

        // Get all the depositList where isPosted equals to UPDATED_IS_POSTED
        defaultDepositShouldNotBeFound("isPosted.in=" + UPDATED_IS_POSTED);
    }

    @Test
    @Transactional
    public void getAllDepositsByIsPostedIsNullOrNotNull() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get all the depositList where isPosted is not null
        defaultDepositShouldBeFound("isPosted.specified=true");

        // Get all the depositList where isPosted is null
        defaultDepositShouldNotBeFound("isPosted.specified=false");
    }

    @Test
    @Transactional
    public void getAllDepositsByPostDateIsEqualToSomething() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get all the depositList where postDate equals to DEFAULT_POST_DATE
        defaultDepositShouldBeFound("postDate.equals=" + DEFAULT_POST_DATE);

        // Get all the depositList where postDate equals to UPDATED_POST_DATE
        defaultDepositShouldNotBeFound("postDate.equals=" + UPDATED_POST_DATE);
    }

    @Test
    @Transactional
    public void getAllDepositsByPostDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get all the depositList where postDate not equals to DEFAULT_POST_DATE
        defaultDepositShouldNotBeFound("postDate.notEquals=" + DEFAULT_POST_DATE);

        // Get all the depositList where postDate not equals to UPDATED_POST_DATE
        defaultDepositShouldBeFound("postDate.notEquals=" + UPDATED_POST_DATE);
    }

    @Test
    @Transactional
    public void getAllDepositsByPostDateIsInShouldWork() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get all the depositList where postDate in DEFAULT_POST_DATE or UPDATED_POST_DATE
        defaultDepositShouldBeFound("postDate.in=" + DEFAULT_POST_DATE + "," + UPDATED_POST_DATE);

        // Get all the depositList where postDate equals to UPDATED_POST_DATE
        defaultDepositShouldNotBeFound("postDate.in=" + UPDATED_POST_DATE);
    }

    @Test
    @Transactional
    public void getAllDepositsByPostDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get all the depositList where postDate is not null
        defaultDepositShouldBeFound("postDate.specified=true");

        // Get all the depositList where postDate is null
        defaultDepositShouldNotBeFound("postDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllDepositsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get all the depositList where createdBy equals to DEFAULT_CREATED_BY
        defaultDepositShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the depositList where createdBy equals to UPDATED_CREATED_BY
        defaultDepositShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllDepositsByCreatedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get all the depositList where createdBy not equals to DEFAULT_CREATED_BY
        defaultDepositShouldNotBeFound("createdBy.notEquals=" + DEFAULT_CREATED_BY);

        // Get all the depositList where createdBy not equals to UPDATED_CREATED_BY
        defaultDepositShouldBeFound("createdBy.notEquals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllDepositsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get all the depositList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultDepositShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the depositList where createdBy equals to UPDATED_CREATED_BY
        defaultDepositShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllDepositsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get all the depositList where createdBy is not null
        defaultDepositShouldBeFound("createdBy.specified=true");

        // Get all the depositList where createdBy is null
        defaultDepositShouldNotBeFound("createdBy.specified=false");
    }
                @Test
    @Transactional
    public void getAllDepositsByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get all the depositList where createdBy contains DEFAULT_CREATED_BY
        defaultDepositShouldBeFound("createdBy.contains=" + DEFAULT_CREATED_BY);

        // Get all the depositList where createdBy contains UPDATED_CREATED_BY
        defaultDepositShouldNotBeFound("createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllDepositsByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get all the depositList where createdBy does not contain DEFAULT_CREATED_BY
        defaultDepositShouldNotBeFound("createdBy.doesNotContain=" + DEFAULT_CREATED_BY);

        // Get all the depositList where createdBy does not contain UPDATED_CREATED_BY
        defaultDepositShouldBeFound("createdBy.doesNotContain=" + UPDATED_CREATED_BY);
    }


    @Test
    @Transactional
    public void getAllDepositsByCreatedOnIsEqualToSomething() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get all the depositList where createdOn equals to DEFAULT_CREATED_ON
        defaultDepositShouldBeFound("createdOn.equals=" + DEFAULT_CREATED_ON);

        // Get all the depositList where createdOn equals to UPDATED_CREATED_ON
        defaultDepositShouldNotBeFound("createdOn.equals=" + UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    public void getAllDepositsByCreatedOnIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get all the depositList where createdOn not equals to DEFAULT_CREATED_ON
        defaultDepositShouldNotBeFound("createdOn.notEquals=" + DEFAULT_CREATED_ON);

        // Get all the depositList where createdOn not equals to UPDATED_CREATED_ON
        defaultDepositShouldBeFound("createdOn.notEquals=" + UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    public void getAllDepositsByCreatedOnIsInShouldWork() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get all the depositList where createdOn in DEFAULT_CREATED_ON or UPDATED_CREATED_ON
        defaultDepositShouldBeFound("createdOn.in=" + DEFAULT_CREATED_ON + "," + UPDATED_CREATED_ON);

        // Get all the depositList where createdOn equals to UPDATED_CREATED_ON
        defaultDepositShouldNotBeFound("createdOn.in=" + UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    public void getAllDepositsByCreatedOnIsNullOrNotNull() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get all the depositList where createdOn is not null
        defaultDepositShouldBeFound("createdOn.specified=true");

        // Get all the depositList where createdOn is null
        defaultDepositShouldNotBeFound("createdOn.specified=false");
    }

    @Test
    @Transactional
    public void getAllDepositsByModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get all the depositList where modifiedBy equals to DEFAULT_MODIFIED_BY
        defaultDepositShouldBeFound("modifiedBy.equals=" + DEFAULT_MODIFIED_BY);

        // Get all the depositList where modifiedBy equals to UPDATED_MODIFIED_BY
        defaultDepositShouldNotBeFound("modifiedBy.equals=" + UPDATED_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllDepositsByModifiedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get all the depositList where modifiedBy not equals to DEFAULT_MODIFIED_BY
        defaultDepositShouldNotBeFound("modifiedBy.notEquals=" + DEFAULT_MODIFIED_BY);

        // Get all the depositList where modifiedBy not equals to UPDATED_MODIFIED_BY
        defaultDepositShouldBeFound("modifiedBy.notEquals=" + UPDATED_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllDepositsByModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get all the depositList where modifiedBy in DEFAULT_MODIFIED_BY or UPDATED_MODIFIED_BY
        defaultDepositShouldBeFound("modifiedBy.in=" + DEFAULT_MODIFIED_BY + "," + UPDATED_MODIFIED_BY);

        // Get all the depositList where modifiedBy equals to UPDATED_MODIFIED_BY
        defaultDepositShouldNotBeFound("modifiedBy.in=" + UPDATED_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllDepositsByModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get all the depositList where modifiedBy is not null
        defaultDepositShouldBeFound("modifiedBy.specified=true");

        // Get all the depositList where modifiedBy is null
        defaultDepositShouldNotBeFound("modifiedBy.specified=false");
    }
                @Test
    @Transactional
    public void getAllDepositsByModifiedByContainsSomething() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get all the depositList where modifiedBy contains DEFAULT_MODIFIED_BY
        defaultDepositShouldBeFound("modifiedBy.contains=" + DEFAULT_MODIFIED_BY);

        // Get all the depositList where modifiedBy contains UPDATED_MODIFIED_BY
        defaultDepositShouldNotBeFound("modifiedBy.contains=" + UPDATED_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllDepositsByModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get all the depositList where modifiedBy does not contain DEFAULT_MODIFIED_BY
        defaultDepositShouldNotBeFound("modifiedBy.doesNotContain=" + DEFAULT_MODIFIED_BY);

        // Get all the depositList where modifiedBy does not contain UPDATED_MODIFIED_BY
        defaultDepositShouldBeFound("modifiedBy.doesNotContain=" + UPDATED_MODIFIED_BY);
    }


    @Test
    @Transactional
    public void getAllDepositsByModifiedOnIsEqualToSomething() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get all the depositList where modifiedOn equals to DEFAULT_MODIFIED_ON
        defaultDepositShouldBeFound("modifiedOn.equals=" + DEFAULT_MODIFIED_ON);

        // Get all the depositList where modifiedOn equals to UPDATED_MODIFIED_ON
        defaultDepositShouldNotBeFound("modifiedOn.equals=" + UPDATED_MODIFIED_ON);
    }

    @Test
    @Transactional
    public void getAllDepositsByModifiedOnIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get all the depositList where modifiedOn not equals to DEFAULT_MODIFIED_ON
        defaultDepositShouldNotBeFound("modifiedOn.notEquals=" + DEFAULT_MODIFIED_ON);

        // Get all the depositList where modifiedOn not equals to UPDATED_MODIFIED_ON
        defaultDepositShouldBeFound("modifiedOn.notEquals=" + UPDATED_MODIFIED_ON);
    }

    @Test
    @Transactional
    public void getAllDepositsByModifiedOnIsInShouldWork() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get all the depositList where modifiedOn in DEFAULT_MODIFIED_ON or UPDATED_MODIFIED_ON
        defaultDepositShouldBeFound("modifiedOn.in=" + DEFAULT_MODIFIED_ON + "," + UPDATED_MODIFIED_ON);

        // Get all the depositList where modifiedOn equals to UPDATED_MODIFIED_ON
        defaultDepositShouldNotBeFound("modifiedOn.in=" + UPDATED_MODIFIED_ON);
    }

    @Test
    @Transactional
    public void getAllDepositsByModifiedOnIsNullOrNotNull() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get all the depositList where modifiedOn is not null
        defaultDepositShouldBeFound("modifiedOn.specified=true");

        // Get all the depositList where modifiedOn is null
        defaultDepositShouldNotBeFound("modifiedOn.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDepositShouldBeFound(String filter) throws Exception {
        restDepositMockMvc.perform(get("/api/deposits?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deposit.getId().intValue())))
            .andExpect(jsonPath("$.[*].loginId").value(hasItem(DEFAULT_LOGIN_ID)))
            .andExpect(jsonPath("$.[*].depositNo").value(hasItem(DEFAULT_DEPOSIT_NO)))
            .andExpect(jsonPath("$.[*].depositBy").value(hasItem(DEFAULT_DEPOSIT_BY)))
            .andExpect(jsonPath("$.[*].depositDate").value(hasItem(DEFAULT_DEPOSIT_DATE.toString())))
            .andExpect(jsonPath("$.[*].medium").value(hasItem(DEFAULT_MEDIUM.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE.toString())))
            .andExpect(jsonPath("$.[*].isPosted").value(hasItem(DEFAULT_IS_POSTED.booleanValue())))
            .andExpect(jsonPath("$.[*].postDate").value(hasItem(DEFAULT_POST_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].modifiedBy").value(hasItem(DEFAULT_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].modifiedOn").value(hasItem(DEFAULT_MODIFIED_ON.toString())));

        // Check, that the count call also returns 1
        restDepositMockMvc.perform(get("/api/deposits/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDepositShouldNotBeFound(String filter) throws Exception {
        restDepositMockMvc.perform(get("/api/deposits?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDepositMockMvc.perform(get("/api/deposits/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingDeposit() throws Exception {
        // Get the deposit
        restDepositMockMvc.perform(get("/api/deposits/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDeposit() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        int databaseSizeBeforeUpdate = depositRepository.findAll().size();

        // Update the deposit
        Deposit updatedDeposit = depositRepository.findById(deposit.getId()).get();
        // Disconnect from session so that the updates on updatedDeposit are not directly saved in db
        em.detach(updatedDeposit);
        updatedDeposit
            .loginId(UPDATED_LOGIN_ID)
            .depositNo(UPDATED_DEPOSIT_NO)
            .depositBy(UPDATED_DEPOSIT_BY)
            .depositDate(UPDATED_DEPOSIT_DATE)
            .medium(UPDATED_MEDIUM)
            .amount(UPDATED_AMOUNT)
            .note(UPDATED_NOTE)
            .isPosted(UPDATED_IS_POSTED)
            .postDate(UPDATED_POST_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .modifiedBy(UPDATED_MODIFIED_BY)
            .modifiedOn(UPDATED_MODIFIED_ON);
        DepositDTO depositDTO = depositMapper.toDto(updatedDeposit);

        restDepositMockMvc.perform(put("/api/deposits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(depositDTO)))
            .andExpect(status().isOk());

        // Validate the Deposit in the database
        List<Deposit> depositList = depositRepository.findAll();
        assertThat(depositList).hasSize(databaseSizeBeforeUpdate);
        Deposit testDeposit = depositList.get(depositList.size() - 1);
        assertThat(testDeposit.getLoginId()).isEqualTo(UPDATED_LOGIN_ID);
        assertThat(testDeposit.getDepositNo()).isEqualTo(UPDATED_DEPOSIT_NO);
        assertThat(testDeposit.getDepositBy()).isEqualTo(UPDATED_DEPOSIT_BY);
        assertThat(testDeposit.getDepositDate()).isEqualTo(UPDATED_DEPOSIT_DATE);
        assertThat(testDeposit.getMedium()).isEqualTo(UPDATED_MEDIUM);
        assertThat(testDeposit.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testDeposit.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testDeposit.isIsPosted()).isEqualTo(UPDATED_IS_POSTED);
        assertThat(testDeposit.getPostDate()).isEqualTo(UPDATED_POST_DATE);
        assertThat(testDeposit.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testDeposit.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testDeposit.getModifiedBy()).isEqualTo(UPDATED_MODIFIED_BY);
        assertThat(testDeposit.getModifiedOn()).isEqualTo(UPDATED_MODIFIED_ON);
    }

    @Test
    @Transactional
    public void updateNonExistingDeposit() throws Exception {
        int databaseSizeBeforeUpdate = depositRepository.findAll().size();

        // Create the Deposit
        DepositDTO depositDTO = depositMapper.toDto(deposit);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepositMockMvc.perform(put("/api/deposits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(depositDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Deposit in the database
        List<Deposit> depositList = depositRepository.findAll();
        assertThat(depositList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDeposit() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        int databaseSizeBeforeDelete = depositRepository.findAll().size();

        // Delete the deposit
        restDepositMockMvc.perform(delete("/api/deposits/{id}", deposit.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Deposit> depositList = depositRepository.findAll();
        assertThat(depositList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
