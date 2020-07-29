package org.cash.manager.web.rest;

import org.cash.manager.PrimeCashManagerApp;
import org.cash.manager.domain.Expense;
import org.cash.manager.domain.ExpenseDtl;
import org.cash.manager.domain.PayTo;
import org.cash.manager.repository.ExpenseRepository;
import org.cash.manager.service.ExpenseService;
import org.cash.manager.service.dto.ExpenseDTO;
import org.cash.manager.service.mapper.ExpenseMapper;
import org.cash.manager.service.dto.ExpenseCriteria;
import org.cash.manager.service.ExpenseQueryService;

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

import org.cash.manager.domain.enumeration.MonthType;
/**
 * Integration tests for the {@link ExpenseResource} REST controller.
 */
@SpringBootTest(classes = PrimeCashManagerApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class ExpenseResourceIT {

    private static final String DEFAULT_LOGIN_ID = "AAAAAAAAAA";
    private static final String UPDATED_LOGIN_ID = "BBBBBBBBBB";

    private static final Integer DEFAULT_VOUCHER_NO = 1;
    private static final Integer UPDATED_VOUCHER_NO = 2;
    private static final Integer SMALLER_VOUCHER_NO = 1 - 1;

    private static final LocalDate DEFAULT_VOUCHER_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_VOUCHER_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_VOUCHER_DATE = LocalDate.ofEpochDay(-1L);

    private static final MonthType DEFAULT_MONTH = MonthType.JANUARY;
    private static final MonthType UPDATED_MONTH = MonthType.FEBRUARY;

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_TOTAL_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_TOTAL_AMOUNT = new BigDecimal(1 - 1);

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
    private ExpenseRepository expenseRepository;

    @Autowired
    private ExpenseMapper expenseMapper;

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private ExpenseQueryService expenseQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExpenseMockMvc;

    private Expense expense;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Expense createEntity(EntityManager em) {
        Expense expense = new Expense()
            .loginId(DEFAULT_LOGIN_ID)
            .voucherNo(DEFAULT_VOUCHER_NO)
            .voucherDate(DEFAULT_VOUCHER_DATE)
            .month(DEFAULT_MONTH)
            .notes(DEFAULT_NOTES)
            .totalAmount(DEFAULT_TOTAL_AMOUNT)
            .isPosted(DEFAULT_IS_POSTED)
            .postDate(DEFAULT_POST_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .createdOn(DEFAULT_CREATED_ON)
            .modifiedBy(DEFAULT_MODIFIED_BY)
            .modifiedOn(DEFAULT_MODIFIED_ON);
        // Add required entity
        PayTo payTo;
        if (TestUtil.findAll(em, PayTo.class).isEmpty()) {
            payTo = PayToResourceIT.createEntity(em);
            em.persist(payTo);
            em.flush();
        } else {
            payTo = TestUtil.findAll(em, PayTo.class).get(0);
        }
        expense.setPayTo(payTo);
        return expense;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Expense createUpdatedEntity(EntityManager em) {
        Expense expense = new Expense()
            .loginId(UPDATED_LOGIN_ID)
            .voucherNo(UPDATED_VOUCHER_NO)
            .voucherDate(UPDATED_VOUCHER_DATE)
            .month(UPDATED_MONTH)
            .notes(UPDATED_NOTES)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .isPosted(UPDATED_IS_POSTED)
            .postDate(UPDATED_POST_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .modifiedBy(UPDATED_MODIFIED_BY)
            .modifiedOn(UPDATED_MODIFIED_ON);
        // Add required entity
        PayTo payTo;
        if (TestUtil.findAll(em, PayTo.class).isEmpty()) {
            payTo = PayToResourceIT.createUpdatedEntity(em);
            em.persist(payTo);
            em.flush();
        } else {
            payTo = TestUtil.findAll(em, PayTo.class).get(0);
        }
        expense.setPayTo(payTo);
        return expense;
    }

    @BeforeEach
    public void initTest() {
        expense = createEntity(em);
    }

    @Test
    @Transactional
    public void createExpense() throws Exception {
        int databaseSizeBeforeCreate = expenseRepository.findAll().size();

        // Create the Expense
        ExpenseDTO expenseDTO = expenseMapper.toDto(expense);
        restExpenseMockMvc.perform(post("/api/expenses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(expenseDTO)))
            .andExpect(status().isCreated());

        // Validate the Expense in the database
        List<Expense> expenseList = expenseRepository.findAll();
        assertThat(expenseList).hasSize(databaseSizeBeforeCreate + 1);
        Expense testExpense = expenseList.get(expenseList.size() - 1);
        assertThat(testExpense.getLoginId()).isEqualTo(DEFAULT_LOGIN_ID);
        assertThat(testExpense.getVoucherNo()).isEqualTo(DEFAULT_VOUCHER_NO);
        assertThat(testExpense.getVoucherDate()).isEqualTo(DEFAULT_VOUCHER_DATE);
        assertThat(testExpense.getMonth()).isEqualTo(DEFAULT_MONTH);
        assertThat(testExpense.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testExpense.getTotalAmount()).isEqualTo(DEFAULT_TOTAL_AMOUNT);
        assertThat(testExpense.isIsPosted()).isEqualTo(DEFAULT_IS_POSTED);
        assertThat(testExpense.getPostDate()).isEqualTo(DEFAULT_POST_DATE);
        assertThat(testExpense.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testExpense.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testExpense.getModifiedBy()).isEqualTo(DEFAULT_MODIFIED_BY);
        assertThat(testExpense.getModifiedOn()).isEqualTo(DEFAULT_MODIFIED_ON);
    }

    @Test
    @Transactional
    public void createExpenseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = expenseRepository.findAll().size();

        // Create the Expense with an existing ID
        expense.setId(1L);
        ExpenseDTO expenseDTO = expenseMapper.toDto(expense);

        // An entity with an existing ID cannot be created, so this API call must fail
        restExpenseMockMvc.perform(post("/api/expenses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(expenseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Expense in the database
        List<Expense> expenseList = expenseRepository.findAll();
        assertThat(expenseList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkLoginIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = expenseRepository.findAll().size();
        // set the field null
        expense.setLoginId(null);

        // Create the Expense, which fails.
        ExpenseDTO expenseDTO = expenseMapper.toDto(expense);

        restExpenseMockMvc.perform(post("/api/expenses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(expenseDTO)))
            .andExpect(status().isBadRequest());

        List<Expense> expenseList = expenseRepository.findAll();
        assertThat(expenseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkVoucherDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = expenseRepository.findAll().size();
        // set the field null
        expense.setVoucherDate(null);

        // Create the Expense, which fails.
        ExpenseDTO expenseDTO = expenseMapper.toDto(expense);

        restExpenseMockMvc.perform(post("/api/expenses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(expenseDTO)))
            .andExpect(status().isBadRequest());

        List<Expense> expenseList = expenseRepository.findAll();
        assertThat(expenseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMonthIsRequired() throws Exception {
        int databaseSizeBeforeTest = expenseRepository.findAll().size();
        // set the field null
        expense.setMonth(null);

        // Create the Expense, which fails.
        ExpenseDTO expenseDTO = expenseMapper.toDto(expense);

        restExpenseMockMvc.perform(post("/api/expenses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(expenseDTO)))
            .andExpect(status().isBadRequest());

        List<Expense> expenseList = expenseRepository.findAll();
        assertThat(expenseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllExpenses() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList
        restExpenseMockMvc.perform(get("/api/expenses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(expense.getId().intValue())))
            .andExpect(jsonPath("$.[*].loginId").value(hasItem(DEFAULT_LOGIN_ID)))
            .andExpect(jsonPath("$.[*].voucherNo").value(hasItem(DEFAULT_VOUCHER_NO)))
            .andExpect(jsonPath("$.[*].voucherDate").value(hasItem(DEFAULT_VOUCHER_DATE.toString())))
            .andExpect(jsonPath("$.[*].month").value(hasItem(DEFAULT_MONTH.toString())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())))
            .andExpect(jsonPath("$.[*].totalAmount").value(hasItem(DEFAULT_TOTAL_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].isPosted").value(hasItem(DEFAULT_IS_POSTED.booleanValue())))
            .andExpect(jsonPath("$.[*].postDate").value(hasItem(DEFAULT_POST_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].modifiedBy").value(hasItem(DEFAULT_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].modifiedOn").value(hasItem(DEFAULT_MODIFIED_ON.toString())));
    }
    
    @Test
    @Transactional
    public void getExpense() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get the expense
        restExpenseMockMvc.perform(get("/api/expenses/{id}", expense.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(expense.getId().intValue()))
            .andExpect(jsonPath("$.loginId").value(DEFAULT_LOGIN_ID))
            .andExpect(jsonPath("$.voucherNo").value(DEFAULT_VOUCHER_NO))
            .andExpect(jsonPath("$.voucherDate").value(DEFAULT_VOUCHER_DATE.toString()))
            .andExpect(jsonPath("$.month").value(DEFAULT_MONTH.toString()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES.toString()))
            .andExpect(jsonPath("$.totalAmount").value(DEFAULT_TOTAL_AMOUNT.intValue()))
            .andExpect(jsonPath("$.isPosted").value(DEFAULT_IS_POSTED.booleanValue()))
            .andExpect(jsonPath("$.postDate").value(DEFAULT_POST_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.modifiedBy").value(DEFAULT_MODIFIED_BY))
            .andExpect(jsonPath("$.modifiedOn").value(DEFAULT_MODIFIED_ON.toString()));
    }


    @Test
    @Transactional
    public void getExpensesByIdFiltering() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        Long id = expense.getId();

        defaultExpenseShouldBeFound("id.equals=" + id);
        defaultExpenseShouldNotBeFound("id.notEquals=" + id);

        defaultExpenseShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultExpenseShouldNotBeFound("id.greaterThan=" + id);

        defaultExpenseShouldBeFound("id.lessThanOrEqual=" + id);
        defaultExpenseShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllExpensesByLoginIdIsEqualToSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where loginId equals to DEFAULT_LOGIN_ID
        defaultExpenseShouldBeFound("loginId.equals=" + DEFAULT_LOGIN_ID);

        // Get all the expenseList where loginId equals to UPDATED_LOGIN_ID
        defaultExpenseShouldNotBeFound("loginId.equals=" + UPDATED_LOGIN_ID);
    }

    @Test
    @Transactional
    public void getAllExpensesByLoginIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where loginId not equals to DEFAULT_LOGIN_ID
        defaultExpenseShouldNotBeFound("loginId.notEquals=" + DEFAULT_LOGIN_ID);

        // Get all the expenseList where loginId not equals to UPDATED_LOGIN_ID
        defaultExpenseShouldBeFound("loginId.notEquals=" + UPDATED_LOGIN_ID);
    }

    @Test
    @Transactional
    public void getAllExpensesByLoginIdIsInShouldWork() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where loginId in DEFAULT_LOGIN_ID or UPDATED_LOGIN_ID
        defaultExpenseShouldBeFound("loginId.in=" + DEFAULT_LOGIN_ID + "," + UPDATED_LOGIN_ID);

        // Get all the expenseList where loginId equals to UPDATED_LOGIN_ID
        defaultExpenseShouldNotBeFound("loginId.in=" + UPDATED_LOGIN_ID);
    }

    @Test
    @Transactional
    public void getAllExpensesByLoginIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where loginId is not null
        defaultExpenseShouldBeFound("loginId.specified=true");

        // Get all the expenseList where loginId is null
        defaultExpenseShouldNotBeFound("loginId.specified=false");
    }
                @Test
    @Transactional
    public void getAllExpensesByLoginIdContainsSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where loginId contains DEFAULT_LOGIN_ID
        defaultExpenseShouldBeFound("loginId.contains=" + DEFAULT_LOGIN_ID);

        // Get all the expenseList where loginId contains UPDATED_LOGIN_ID
        defaultExpenseShouldNotBeFound("loginId.contains=" + UPDATED_LOGIN_ID);
    }

    @Test
    @Transactional
    public void getAllExpensesByLoginIdNotContainsSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where loginId does not contain DEFAULT_LOGIN_ID
        defaultExpenseShouldNotBeFound("loginId.doesNotContain=" + DEFAULT_LOGIN_ID);

        // Get all the expenseList where loginId does not contain UPDATED_LOGIN_ID
        defaultExpenseShouldBeFound("loginId.doesNotContain=" + UPDATED_LOGIN_ID);
    }


    @Test
    @Transactional
    public void getAllExpensesByVoucherNoIsEqualToSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where voucherNo equals to DEFAULT_VOUCHER_NO
        defaultExpenseShouldBeFound("voucherNo.equals=" + DEFAULT_VOUCHER_NO);

        // Get all the expenseList where voucherNo equals to UPDATED_VOUCHER_NO
        defaultExpenseShouldNotBeFound("voucherNo.equals=" + UPDATED_VOUCHER_NO);
    }

    @Test
    @Transactional
    public void getAllExpensesByVoucherNoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where voucherNo not equals to DEFAULT_VOUCHER_NO
        defaultExpenseShouldNotBeFound("voucherNo.notEquals=" + DEFAULT_VOUCHER_NO);

        // Get all the expenseList where voucherNo not equals to UPDATED_VOUCHER_NO
        defaultExpenseShouldBeFound("voucherNo.notEquals=" + UPDATED_VOUCHER_NO);
    }

    @Test
    @Transactional
    public void getAllExpensesByVoucherNoIsInShouldWork() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where voucherNo in DEFAULT_VOUCHER_NO or UPDATED_VOUCHER_NO
        defaultExpenseShouldBeFound("voucherNo.in=" + DEFAULT_VOUCHER_NO + "," + UPDATED_VOUCHER_NO);

        // Get all the expenseList where voucherNo equals to UPDATED_VOUCHER_NO
        defaultExpenseShouldNotBeFound("voucherNo.in=" + UPDATED_VOUCHER_NO);
    }

    @Test
    @Transactional
    public void getAllExpensesByVoucherNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where voucherNo is not null
        defaultExpenseShouldBeFound("voucherNo.specified=true");

        // Get all the expenseList where voucherNo is null
        defaultExpenseShouldNotBeFound("voucherNo.specified=false");
    }

    @Test
    @Transactional
    public void getAllExpensesByVoucherNoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where voucherNo is greater than or equal to DEFAULT_VOUCHER_NO
        defaultExpenseShouldBeFound("voucherNo.greaterThanOrEqual=" + DEFAULT_VOUCHER_NO);

        // Get all the expenseList where voucherNo is greater than or equal to UPDATED_VOUCHER_NO
        defaultExpenseShouldNotBeFound("voucherNo.greaterThanOrEqual=" + UPDATED_VOUCHER_NO);
    }

    @Test
    @Transactional
    public void getAllExpensesByVoucherNoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where voucherNo is less than or equal to DEFAULT_VOUCHER_NO
        defaultExpenseShouldBeFound("voucherNo.lessThanOrEqual=" + DEFAULT_VOUCHER_NO);

        // Get all the expenseList where voucherNo is less than or equal to SMALLER_VOUCHER_NO
        defaultExpenseShouldNotBeFound("voucherNo.lessThanOrEqual=" + SMALLER_VOUCHER_NO);
    }

    @Test
    @Transactional
    public void getAllExpensesByVoucherNoIsLessThanSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where voucherNo is less than DEFAULT_VOUCHER_NO
        defaultExpenseShouldNotBeFound("voucherNo.lessThan=" + DEFAULT_VOUCHER_NO);

        // Get all the expenseList where voucherNo is less than UPDATED_VOUCHER_NO
        defaultExpenseShouldBeFound("voucherNo.lessThan=" + UPDATED_VOUCHER_NO);
    }

    @Test
    @Transactional
    public void getAllExpensesByVoucherNoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where voucherNo is greater than DEFAULT_VOUCHER_NO
        defaultExpenseShouldNotBeFound("voucherNo.greaterThan=" + DEFAULT_VOUCHER_NO);

        // Get all the expenseList where voucherNo is greater than SMALLER_VOUCHER_NO
        defaultExpenseShouldBeFound("voucherNo.greaterThan=" + SMALLER_VOUCHER_NO);
    }


    @Test
    @Transactional
    public void getAllExpensesByVoucherDateIsEqualToSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where voucherDate equals to DEFAULT_VOUCHER_DATE
        defaultExpenseShouldBeFound("voucherDate.equals=" + DEFAULT_VOUCHER_DATE);

        // Get all the expenseList where voucherDate equals to UPDATED_VOUCHER_DATE
        defaultExpenseShouldNotBeFound("voucherDate.equals=" + UPDATED_VOUCHER_DATE);
    }

    @Test
    @Transactional
    public void getAllExpensesByVoucherDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where voucherDate not equals to DEFAULT_VOUCHER_DATE
        defaultExpenseShouldNotBeFound("voucherDate.notEquals=" + DEFAULT_VOUCHER_DATE);

        // Get all the expenseList where voucherDate not equals to UPDATED_VOUCHER_DATE
        defaultExpenseShouldBeFound("voucherDate.notEquals=" + UPDATED_VOUCHER_DATE);
    }

    @Test
    @Transactional
    public void getAllExpensesByVoucherDateIsInShouldWork() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where voucherDate in DEFAULT_VOUCHER_DATE or UPDATED_VOUCHER_DATE
        defaultExpenseShouldBeFound("voucherDate.in=" + DEFAULT_VOUCHER_DATE + "," + UPDATED_VOUCHER_DATE);

        // Get all the expenseList where voucherDate equals to UPDATED_VOUCHER_DATE
        defaultExpenseShouldNotBeFound("voucherDate.in=" + UPDATED_VOUCHER_DATE);
    }

    @Test
    @Transactional
    public void getAllExpensesByVoucherDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where voucherDate is not null
        defaultExpenseShouldBeFound("voucherDate.specified=true");

        // Get all the expenseList where voucherDate is null
        defaultExpenseShouldNotBeFound("voucherDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllExpensesByVoucherDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where voucherDate is greater than or equal to DEFAULT_VOUCHER_DATE
        defaultExpenseShouldBeFound("voucherDate.greaterThanOrEqual=" + DEFAULT_VOUCHER_DATE);

        // Get all the expenseList where voucherDate is greater than or equal to UPDATED_VOUCHER_DATE
        defaultExpenseShouldNotBeFound("voucherDate.greaterThanOrEqual=" + UPDATED_VOUCHER_DATE);
    }

    @Test
    @Transactional
    public void getAllExpensesByVoucherDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where voucherDate is less than or equal to DEFAULT_VOUCHER_DATE
        defaultExpenseShouldBeFound("voucherDate.lessThanOrEqual=" + DEFAULT_VOUCHER_DATE);

        // Get all the expenseList where voucherDate is less than or equal to SMALLER_VOUCHER_DATE
        defaultExpenseShouldNotBeFound("voucherDate.lessThanOrEqual=" + SMALLER_VOUCHER_DATE);
    }

    @Test
    @Transactional
    public void getAllExpensesByVoucherDateIsLessThanSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where voucherDate is less than DEFAULT_VOUCHER_DATE
        defaultExpenseShouldNotBeFound("voucherDate.lessThan=" + DEFAULT_VOUCHER_DATE);

        // Get all the expenseList where voucherDate is less than UPDATED_VOUCHER_DATE
        defaultExpenseShouldBeFound("voucherDate.lessThan=" + UPDATED_VOUCHER_DATE);
    }

    @Test
    @Transactional
    public void getAllExpensesByVoucherDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where voucherDate is greater than DEFAULT_VOUCHER_DATE
        defaultExpenseShouldNotBeFound("voucherDate.greaterThan=" + DEFAULT_VOUCHER_DATE);

        // Get all the expenseList where voucherDate is greater than SMALLER_VOUCHER_DATE
        defaultExpenseShouldBeFound("voucherDate.greaterThan=" + SMALLER_VOUCHER_DATE);
    }


    @Test
    @Transactional
    public void getAllExpensesByMonthIsEqualToSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where month equals to DEFAULT_MONTH
        defaultExpenseShouldBeFound("month.equals=" + DEFAULT_MONTH);

        // Get all the expenseList where month equals to UPDATED_MONTH
        defaultExpenseShouldNotBeFound("month.equals=" + UPDATED_MONTH);
    }

    @Test
    @Transactional
    public void getAllExpensesByMonthIsNotEqualToSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where month not equals to DEFAULT_MONTH
        defaultExpenseShouldNotBeFound("month.notEquals=" + DEFAULT_MONTH);

        // Get all the expenseList where month not equals to UPDATED_MONTH
        defaultExpenseShouldBeFound("month.notEquals=" + UPDATED_MONTH);
    }

    @Test
    @Transactional
    public void getAllExpensesByMonthIsInShouldWork() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where month in DEFAULT_MONTH or UPDATED_MONTH
        defaultExpenseShouldBeFound("month.in=" + DEFAULT_MONTH + "," + UPDATED_MONTH);

        // Get all the expenseList where month equals to UPDATED_MONTH
        defaultExpenseShouldNotBeFound("month.in=" + UPDATED_MONTH);
    }

    @Test
    @Transactional
    public void getAllExpensesByMonthIsNullOrNotNull() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where month is not null
        defaultExpenseShouldBeFound("month.specified=true");

        // Get all the expenseList where month is null
        defaultExpenseShouldNotBeFound("month.specified=false");
    }

    @Test
    @Transactional
    public void getAllExpensesByTotalAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where totalAmount equals to DEFAULT_TOTAL_AMOUNT
        defaultExpenseShouldBeFound("totalAmount.equals=" + DEFAULT_TOTAL_AMOUNT);

        // Get all the expenseList where totalAmount equals to UPDATED_TOTAL_AMOUNT
        defaultExpenseShouldNotBeFound("totalAmount.equals=" + UPDATED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllExpensesByTotalAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where totalAmount not equals to DEFAULT_TOTAL_AMOUNT
        defaultExpenseShouldNotBeFound("totalAmount.notEquals=" + DEFAULT_TOTAL_AMOUNT);

        // Get all the expenseList where totalAmount not equals to UPDATED_TOTAL_AMOUNT
        defaultExpenseShouldBeFound("totalAmount.notEquals=" + UPDATED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllExpensesByTotalAmountIsInShouldWork() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where totalAmount in DEFAULT_TOTAL_AMOUNT or UPDATED_TOTAL_AMOUNT
        defaultExpenseShouldBeFound("totalAmount.in=" + DEFAULT_TOTAL_AMOUNT + "," + UPDATED_TOTAL_AMOUNT);

        // Get all the expenseList where totalAmount equals to UPDATED_TOTAL_AMOUNT
        defaultExpenseShouldNotBeFound("totalAmount.in=" + UPDATED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllExpensesByTotalAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where totalAmount is not null
        defaultExpenseShouldBeFound("totalAmount.specified=true");

        // Get all the expenseList where totalAmount is null
        defaultExpenseShouldNotBeFound("totalAmount.specified=false");
    }

    @Test
    @Transactional
    public void getAllExpensesByTotalAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where totalAmount is greater than or equal to DEFAULT_TOTAL_AMOUNT
        defaultExpenseShouldBeFound("totalAmount.greaterThanOrEqual=" + DEFAULT_TOTAL_AMOUNT);

        // Get all the expenseList where totalAmount is greater than or equal to UPDATED_TOTAL_AMOUNT
        defaultExpenseShouldNotBeFound("totalAmount.greaterThanOrEqual=" + UPDATED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllExpensesByTotalAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where totalAmount is less than or equal to DEFAULT_TOTAL_AMOUNT
        defaultExpenseShouldBeFound("totalAmount.lessThanOrEqual=" + DEFAULT_TOTAL_AMOUNT);

        // Get all the expenseList where totalAmount is less than or equal to SMALLER_TOTAL_AMOUNT
        defaultExpenseShouldNotBeFound("totalAmount.lessThanOrEqual=" + SMALLER_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllExpensesByTotalAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where totalAmount is less than DEFAULT_TOTAL_AMOUNT
        defaultExpenseShouldNotBeFound("totalAmount.lessThan=" + DEFAULT_TOTAL_AMOUNT);

        // Get all the expenseList where totalAmount is less than UPDATED_TOTAL_AMOUNT
        defaultExpenseShouldBeFound("totalAmount.lessThan=" + UPDATED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllExpensesByTotalAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where totalAmount is greater than DEFAULT_TOTAL_AMOUNT
        defaultExpenseShouldNotBeFound("totalAmount.greaterThan=" + DEFAULT_TOTAL_AMOUNT);

        // Get all the expenseList where totalAmount is greater than SMALLER_TOTAL_AMOUNT
        defaultExpenseShouldBeFound("totalAmount.greaterThan=" + SMALLER_TOTAL_AMOUNT);
    }


    @Test
    @Transactional
    public void getAllExpensesByIsPostedIsEqualToSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where isPosted equals to DEFAULT_IS_POSTED
        defaultExpenseShouldBeFound("isPosted.equals=" + DEFAULT_IS_POSTED);

        // Get all the expenseList where isPosted equals to UPDATED_IS_POSTED
        defaultExpenseShouldNotBeFound("isPosted.equals=" + UPDATED_IS_POSTED);
    }

    @Test
    @Transactional
    public void getAllExpensesByIsPostedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where isPosted not equals to DEFAULT_IS_POSTED
        defaultExpenseShouldNotBeFound("isPosted.notEquals=" + DEFAULT_IS_POSTED);

        // Get all the expenseList where isPosted not equals to UPDATED_IS_POSTED
        defaultExpenseShouldBeFound("isPosted.notEquals=" + UPDATED_IS_POSTED);
    }

    @Test
    @Transactional
    public void getAllExpensesByIsPostedIsInShouldWork() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where isPosted in DEFAULT_IS_POSTED or UPDATED_IS_POSTED
        defaultExpenseShouldBeFound("isPosted.in=" + DEFAULT_IS_POSTED + "," + UPDATED_IS_POSTED);

        // Get all the expenseList where isPosted equals to UPDATED_IS_POSTED
        defaultExpenseShouldNotBeFound("isPosted.in=" + UPDATED_IS_POSTED);
    }

    @Test
    @Transactional
    public void getAllExpensesByIsPostedIsNullOrNotNull() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where isPosted is not null
        defaultExpenseShouldBeFound("isPosted.specified=true");

        // Get all the expenseList where isPosted is null
        defaultExpenseShouldNotBeFound("isPosted.specified=false");
    }

    @Test
    @Transactional
    public void getAllExpensesByPostDateIsEqualToSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where postDate equals to DEFAULT_POST_DATE
        defaultExpenseShouldBeFound("postDate.equals=" + DEFAULT_POST_DATE);

        // Get all the expenseList where postDate equals to UPDATED_POST_DATE
        defaultExpenseShouldNotBeFound("postDate.equals=" + UPDATED_POST_DATE);
    }

    @Test
    @Transactional
    public void getAllExpensesByPostDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where postDate not equals to DEFAULT_POST_DATE
        defaultExpenseShouldNotBeFound("postDate.notEquals=" + DEFAULT_POST_DATE);

        // Get all the expenseList where postDate not equals to UPDATED_POST_DATE
        defaultExpenseShouldBeFound("postDate.notEquals=" + UPDATED_POST_DATE);
    }

    @Test
    @Transactional
    public void getAllExpensesByPostDateIsInShouldWork() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where postDate in DEFAULT_POST_DATE or UPDATED_POST_DATE
        defaultExpenseShouldBeFound("postDate.in=" + DEFAULT_POST_DATE + "," + UPDATED_POST_DATE);

        // Get all the expenseList where postDate equals to UPDATED_POST_DATE
        defaultExpenseShouldNotBeFound("postDate.in=" + UPDATED_POST_DATE);
    }

    @Test
    @Transactional
    public void getAllExpensesByPostDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where postDate is not null
        defaultExpenseShouldBeFound("postDate.specified=true");

        // Get all the expenseList where postDate is null
        defaultExpenseShouldNotBeFound("postDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllExpensesByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where createdBy equals to DEFAULT_CREATED_BY
        defaultExpenseShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the expenseList where createdBy equals to UPDATED_CREATED_BY
        defaultExpenseShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllExpensesByCreatedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where createdBy not equals to DEFAULT_CREATED_BY
        defaultExpenseShouldNotBeFound("createdBy.notEquals=" + DEFAULT_CREATED_BY);

        // Get all the expenseList where createdBy not equals to UPDATED_CREATED_BY
        defaultExpenseShouldBeFound("createdBy.notEquals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllExpensesByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultExpenseShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the expenseList where createdBy equals to UPDATED_CREATED_BY
        defaultExpenseShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllExpensesByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where createdBy is not null
        defaultExpenseShouldBeFound("createdBy.specified=true");

        // Get all the expenseList where createdBy is null
        defaultExpenseShouldNotBeFound("createdBy.specified=false");
    }
                @Test
    @Transactional
    public void getAllExpensesByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where createdBy contains DEFAULT_CREATED_BY
        defaultExpenseShouldBeFound("createdBy.contains=" + DEFAULT_CREATED_BY);

        // Get all the expenseList where createdBy contains UPDATED_CREATED_BY
        defaultExpenseShouldNotBeFound("createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllExpensesByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where createdBy does not contain DEFAULT_CREATED_BY
        defaultExpenseShouldNotBeFound("createdBy.doesNotContain=" + DEFAULT_CREATED_BY);

        // Get all the expenseList where createdBy does not contain UPDATED_CREATED_BY
        defaultExpenseShouldBeFound("createdBy.doesNotContain=" + UPDATED_CREATED_BY);
    }


    @Test
    @Transactional
    public void getAllExpensesByCreatedOnIsEqualToSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where createdOn equals to DEFAULT_CREATED_ON
        defaultExpenseShouldBeFound("createdOn.equals=" + DEFAULT_CREATED_ON);

        // Get all the expenseList where createdOn equals to UPDATED_CREATED_ON
        defaultExpenseShouldNotBeFound("createdOn.equals=" + UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    public void getAllExpensesByCreatedOnIsNotEqualToSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where createdOn not equals to DEFAULT_CREATED_ON
        defaultExpenseShouldNotBeFound("createdOn.notEquals=" + DEFAULT_CREATED_ON);

        // Get all the expenseList where createdOn not equals to UPDATED_CREATED_ON
        defaultExpenseShouldBeFound("createdOn.notEquals=" + UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    public void getAllExpensesByCreatedOnIsInShouldWork() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where createdOn in DEFAULT_CREATED_ON or UPDATED_CREATED_ON
        defaultExpenseShouldBeFound("createdOn.in=" + DEFAULT_CREATED_ON + "," + UPDATED_CREATED_ON);

        // Get all the expenseList where createdOn equals to UPDATED_CREATED_ON
        defaultExpenseShouldNotBeFound("createdOn.in=" + UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    public void getAllExpensesByCreatedOnIsNullOrNotNull() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where createdOn is not null
        defaultExpenseShouldBeFound("createdOn.specified=true");

        // Get all the expenseList where createdOn is null
        defaultExpenseShouldNotBeFound("createdOn.specified=false");
    }

    @Test
    @Transactional
    public void getAllExpensesByModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where modifiedBy equals to DEFAULT_MODIFIED_BY
        defaultExpenseShouldBeFound("modifiedBy.equals=" + DEFAULT_MODIFIED_BY);

        // Get all the expenseList where modifiedBy equals to UPDATED_MODIFIED_BY
        defaultExpenseShouldNotBeFound("modifiedBy.equals=" + UPDATED_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllExpensesByModifiedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where modifiedBy not equals to DEFAULT_MODIFIED_BY
        defaultExpenseShouldNotBeFound("modifiedBy.notEquals=" + DEFAULT_MODIFIED_BY);

        // Get all the expenseList where modifiedBy not equals to UPDATED_MODIFIED_BY
        defaultExpenseShouldBeFound("modifiedBy.notEquals=" + UPDATED_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllExpensesByModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where modifiedBy in DEFAULT_MODIFIED_BY or UPDATED_MODIFIED_BY
        defaultExpenseShouldBeFound("modifiedBy.in=" + DEFAULT_MODIFIED_BY + "," + UPDATED_MODIFIED_BY);

        // Get all the expenseList where modifiedBy equals to UPDATED_MODIFIED_BY
        defaultExpenseShouldNotBeFound("modifiedBy.in=" + UPDATED_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllExpensesByModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where modifiedBy is not null
        defaultExpenseShouldBeFound("modifiedBy.specified=true");

        // Get all the expenseList where modifiedBy is null
        defaultExpenseShouldNotBeFound("modifiedBy.specified=false");
    }
                @Test
    @Transactional
    public void getAllExpensesByModifiedByContainsSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where modifiedBy contains DEFAULT_MODIFIED_BY
        defaultExpenseShouldBeFound("modifiedBy.contains=" + DEFAULT_MODIFIED_BY);

        // Get all the expenseList where modifiedBy contains UPDATED_MODIFIED_BY
        defaultExpenseShouldNotBeFound("modifiedBy.contains=" + UPDATED_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllExpensesByModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where modifiedBy does not contain DEFAULT_MODIFIED_BY
        defaultExpenseShouldNotBeFound("modifiedBy.doesNotContain=" + DEFAULT_MODIFIED_BY);

        // Get all the expenseList where modifiedBy does not contain UPDATED_MODIFIED_BY
        defaultExpenseShouldBeFound("modifiedBy.doesNotContain=" + UPDATED_MODIFIED_BY);
    }


    @Test
    @Transactional
    public void getAllExpensesByModifiedOnIsEqualToSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where modifiedOn equals to DEFAULT_MODIFIED_ON
        defaultExpenseShouldBeFound("modifiedOn.equals=" + DEFAULT_MODIFIED_ON);

        // Get all the expenseList where modifiedOn equals to UPDATED_MODIFIED_ON
        defaultExpenseShouldNotBeFound("modifiedOn.equals=" + UPDATED_MODIFIED_ON);
    }

    @Test
    @Transactional
    public void getAllExpensesByModifiedOnIsNotEqualToSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where modifiedOn not equals to DEFAULT_MODIFIED_ON
        defaultExpenseShouldNotBeFound("modifiedOn.notEquals=" + DEFAULT_MODIFIED_ON);

        // Get all the expenseList where modifiedOn not equals to UPDATED_MODIFIED_ON
        defaultExpenseShouldBeFound("modifiedOn.notEquals=" + UPDATED_MODIFIED_ON);
    }

    @Test
    @Transactional
    public void getAllExpensesByModifiedOnIsInShouldWork() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where modifiedOn in DEFAULT_MODIFIED_ON or UPDATED_MODIFIED_ON
        defaultExpenseShouldBeFound("modifiedOn.in=" + DEFAULT_MODIFIED_ON + "," + UPDATED_MODIFIED_ON);

        // Get all the expenseList where modifiedOn equals to UPDATED_MODIFIED_ON
        defaultExpenseShouldNotBeFound("modifiedOn.in=" + UPDATED_MODIFIED_ON);
    }

    @Test
    @Transactional
    public void getAllExpensesByModifiedOnIsNullOrNotNull() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where modifiedOn is not null
        defaultExpenseShouldBeFound("modifiedOn.specified=true");

        // Get all the expenseList where modifiedOn is null
        defaultExpenseShouldNotBeFound("modifiedOn.specified=false");
    }

    @Test
    @Transactional
    public void getAllExpensesByExpanseDtlIsEqualToSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);
        ExpenseDtl expanseDtl = ExpenseDtlResourceIT.createEntity(em);
        em.persist(expanseDtl);
        em.flush();
        expense.addExpanseDtl(expanseDtl);
        expenseRepository.saveAndFlush(expense);
        Long expanseDtlId = expanseDtl.getId();

        // Get all the expenseList where expanseDtl equals to expanseDtlId
        defaultExpenseShouldBeFound("expanseDtlId.equals=" + expanseDtlId);

        // Get all the expenseList where expanseDtl equals to expanseDtlId + 1
        defaultExpenseShouldNotBeFound("expanseDtlId.equals=" + (expanseDtlId + 1));
    }


    @Test
    @Transactional
    public void getAllExpensesByPayToIsEqualToSomething() throws Exception {
        // Get already existing entity
        PayTo payTo = expense.getPayTo();
        expenseRepository.saveAndFlush(expense);
        Long payToId = payTo.getId();

        // Get all the expenseList where payTo equals to payToId
        defaultExpenseShouldBeFound("payToId.equals=" + payToId);

        // Get all the expenseList where payTo equals to payToId + 1
        defaultExpenseShouldNotBeFound("payToId.equals=" + (payToId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultExpenseShouldBeFound(String filter) throws Exception {
        restExpenseMockMvc.perform(get("/api/expenses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(expense.getId().intValue())))
            .andExpect(jsonPath("$.[*].loginId").value(hasItem(DEFAULT_LOGIN_ID)))
            .andExpect(jsonPath("$.[*].voucherNo").value(hasItem(DEFAULT_VOUCHER_NO)))
            .andExpect(jsonPath("$.[*].voucherDate").value(hasItem(DEFAULT_VOUCHER_DATE.toString())))
            .andExpect(jsonPath("$.[*].month").value(hasItem(DEFAULT_MONTH.toString())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())))
            .andExpect(jsonPath("$.[*].totalAmount").value(hasItem(DEFAULT_TOTAL_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].isPosted").value(hasItem(DEFAULT_IS_POSTED.booleanValue())))
            .andExpect(jsonPath("$.[*].postDate").value(hasItem(DEFAULT_POST_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].modifiedBy").value(hasItem(DEFAULT_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].modifiedOn").value(hasItem(DEFAULT_MODIFIED_ON.toString())));

        // Check, that the count call also returns 1
        restExpenseMockMvc.perform(get("/api/expenses/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultExpenseShouldNotBeFound(String filter) throws Exception {
        restExpenseMockMvc.perform(get("/api/expenses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restExpenseMockMvc.perform(get("/api/expenses/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingExpense() throws Exception {
        // Get the expense
        restExpenseMockMvc.perform(get("/api/expenses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExpense() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        int databaseSizeBeforeUpdate = expenseRepository.findAll().size();

        // Update the expense
        Expense updatedExpense = expenseRepository.findById(expense.getId()).get();
        // Disconnect from session so that the updates on updatedExpense are not directly saved in db
        em.detach(updatedExpense);
        updatedExpense
            .loginId(UPDATED_LOGIN_ID)
            .voucherNo(UPDATED_VOUCHER_NO)
            .voucherDate(UPDATED_VOUCHER_DATE)
            .month(UPDATED_MONTH)
            .notes(UPDATED_NOTES)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .isPosted(UPDATED_IS_POSTED)
            .postDate(UPDATED_POST_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .modifiedBy(UPDATED_MODIFIED_BY)
            .modifiedOn(UPDATED_MODIFIED_ON);
        ExpenseDTO expenseDTO = expenseMapper.toDto(updatedExpense);

        restExpenseMockMvc.perform(put("/api/expenses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(expenseDTO)))
            .andExpect(status().isOk());

        // Validate the Expense in the database
        List<Expense> expenseList = expenseRepository.findAll();
        assertThat(expenseList).hasSize(databaseSizeBeforeUpdate);
        Expense testExpense = expenseList.get(expenseList.size() - 1);
        assertThat(testExpense.getLoginId()).isEqualTo(UPDATED_LOGIN_ID);
        assertThat(testExpense.getVoucherNo()).isEqualTo(UPDATED_VOUCHER_NO);
        assertThat(testExpense.getVoucherDate()).isEqualTo(UPDATED_VOUCHER_DATE);
        assertThat(testExpense.getMonth()).isEqualTo(UPDATED_MONTH);
        assertThat(testExpense.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testExpense.getTotalAmount()).isEqualTo(UPDATED_TOTAL_AMOUNT);
        assertThat(testExpense.isIsPosted()).isEqualTo(UPDATED_IS_POSTED);
        assertThat(testExpense.getPostDate()).isEqualTo(UPDATED_POST_DATE);
        assertThat(testExpense.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testExpense.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testExpense.getModifiedBy()).isEqualTo(UPDATED_MODIFIED_BY);
        assertThat(testExpense.getModifiedOn()).isEqualTo(UPDATED_MODIFIED_ON);
    }

    @Test
    @Transactional
    public void updateNonExistingExpense() throws Exception {
        int databaseSizeBeforeUpdate = expenseRepository.findAll().size();

        // Create the Expense
        ExpenseDTO expenseDTO = expenseMapper.toDto(expense);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExpenseMockMvc.perform(put("/api/expenses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(expenseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Expense in the database
        List<Expense> expenseList = expenseRepository.findAll();
        assertThat(expenseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteExpense() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        int databaseSizeBeforeDelete = expenseRepository.findAll().size();

        // Delete the expense
        restExpenseMockMvc.perform(delete("/api/expenses/{id}", expense.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Expense> expenseList = expenseRepository.findAll();
        assertThat(expenseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
