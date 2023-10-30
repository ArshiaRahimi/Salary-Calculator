package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.TblEmployeeSalary;
import com.mycompany.myapp.repository.TblEmployeeSalaryRepository;
import com.mycompany.myapp.service.criteria.TblEmployeeSalaryCriteria;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TblEmployeeSalaryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TblEmployeeSalaryResourceIT {

    private static final ZonedDateTime DEFAULT_DATE_CALCULATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_CALCULATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DATE_CALCULATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Integer DEFAULT_EMPLOYEE_ID = 1;
    private static final Integer UPDATED_EMPLOYEE_ID = 2;
    private static final Integer SMALLER_EMPLOYEE_ID = 1 - 1;

    private static final String DEFAULT_UNDERTIME = "AAAAAAAAAA";
    private static final String UPDATED_UNDERTIME = "BBBBBBBBBB";

    private static final String DEFAULT_OVERTIME = "AAAAAAAAAA";
    private static final String UPDATED_OVERTIME = "BBBBBBBBBB";

    private static final String DEFAULT_OVERTIME_OFFDAY = "AAAAAAAAAA";
    private static final String UPDATED_OVERTIME_OFFDAY = "BBBBBBBBBB";

    private static final String DEFAULT_TOTAL_SALARY = "AAAAAAAAAA";
    private static final String UPDATED_TOTAL_SALARY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tbl-employee-salaries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TblEmployeeSalaryRepository tblEmployeeSalaryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTblEmployeeSalaryMockMvc;

    private TblEmployeeSalary tblEmployeeSalary;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TblEmployeeSalary createEntity(EntityManager em) {
        TblEmployeeSalary tblEmployeeSalary = new TblEmployeeSalary()
            .dateCalculated(DEFAULT_DATE_CALCULATED)
            .employeeId(DEFAULT_EMPLOYEE_ID)
            .undertime(DEFAULT_UNDERTIME)
            .overtime(DEFAULT_OVERTIME)
            .overtimeOffday(DEFAULT_OVERTIME_OFFDAY)
            .totalSalary(DEFAULT_TOTAL_SALARY);
        return tblEmployeeSalary;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TblEmployeeSalary createUpdatedEntity(EntityManager em) {
        TblEmployeeSalary tblEmployeeSalary = new TblEmployeeSalary()
            .dateCalculated(UPDATED_DATE_CALCULATED)
            .employeeId(UPDATED_EMPLOYEE_ID)
            .undertime(UPDATED_UNDERTIME)
            .overtime(UPDATED_OVERTIME)
            .overtimeOffday(UPDATED_OVERTIME_OFFDAY)
            .totalSalary(UPDATED_TOTAL_SALARY);
        return tblEmployeeSalary;
    }

    @BeforeEach
    public void initTest() {
        tblEmployeeSalary = createEntity(em);
    }

    @Test
    @Transactional
    void createTblEmployeeSalary() throws Exception {
        int databaseSizeBeforeCreate = tblEmployeeSalaryRepository.findAll().size();
        // Create the TblEmployeeSalary
        restTblEmployeeSalaryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tblEmployeeSalary))
            )
            .andExpect(status().isCreated());

        // Validate the TblEmployeeSalary in the database
        List<TblEmployeeSalary> tblEmployeeSalaryList = tblEmployeeSalaryRepository.findAll();
        assertThat(tblEmployeeSalaryList).hasSize(databaseSizeBeforeCreate + 1);
        TblEmployeeSalary testTblEmployeeSalary = tblEmployeeSalaryList.get(tblEmployeeSalaryList.size() - 1);
        assertThat(testTblEmployeeSalary.getDateCalculated()).isEqualTo(DEFAULT_DATE_CALCULATED);
        assertThat(testTblEmployeeSalary.getEmployeeId()).isEqualTo(DEFAULT_EMPLOYEE_ID);
        assertThat(testTblEmployeeSalary.getUndertime()).isEqualTo(DEFAULT_UNDERTIME);
        assertThat(testTblEmployeeSalary.getOvertime()).isEqualTo(DEFAULT_OVERTIME);
        assertThat(testTblEmployeeSalary.getOvertimeOffday()).isEqualTo(DEFAULT_OVERTIME_OFFDAY);
        assertThat(testTblEmployeeSalary.getTotalSalary()).isEqualTo(DEFAULT_TOTAL_SALARY);
    }

    @Test
    @Transactional
    void createTblEmployeeSalaryWithExistingId() throws Exception {
        // Create the TblEmployeeSalary with an existing ID
        tblEmployeeSalary.setId(1L);

        int databaseSizeBeforeCreate = tblEmployeeSalaryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTblEmployeeSalaryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tblEmployeeSalary))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblEmployeeSalary in the database
        List<TblEmployeeSalary> tblEmployeeSalaryList = tblEmployeeSalaryRepository.findAll();
        assertThat(tblEmployeeSalaryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTblEmployeeSalaries() throws Exception {
        // Initialize the database
        tblEmployeeSalaryRepository.saveAndFlush(tblEmployeeSalary);

        // Get all the tblEmployeeSalaryList
        restTblEmployeeSalaryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tblEmployeeSalary.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateCalculated").value(hasItem(sameInstant(DEFAULT_DATE_CALCULATED))))
            .andExpect(jsonPath("$.[*].employeeId").value(hasItem(DEFAULT_EMPLOYEE_ID)))
            .andExpect(jsonPath("$.[*].undertime").value(hasItem(DEFAULT_UNDERTIME)))
            .andExpect(jsonPath("$.[*].overtime").value(hasItem(DEFAULT_OVERTIME)))
            .andExpect(jsonPath("$.[*].overtimeOffday").value(hasItem(DEFAULT_OVERTIME_OFFDAY)))
            .andExpect(jsonPath("$.[*].totalSalary").value(hasItem(DEFAULT_TOTAL_SALARY)));
    }

    @Test
    @Transactional
    void getTblEmployeeSalary() throws Exception {
        // Initialize the database
        tblEmployeeSalaryRepository.saveAndFlush(tblEmployeeSalary);

        // Get the tblEmployeeSalary
        restTblEmployeeSalaryMockMvc
            .perform(get(ENTITY_API_URL_ID, tblEmployeeSalary.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tblEmployeeSalary.getId().intValue()))
            .andExpect(jsonPath("$.dateCalculated").value(sameInstant(DEFAULT_DATE_CALCULATED)))
            .andExpect(jsonPath("$.employeeId").value(DEFAULT_EMPLOYEE_ID))
            .andExpect(jsonPath("$.undertime").value(DEFAULT_UNDERTIME))
            .andExpect(jsonPath("$.overtime").value(DEFAULT_OVERTIME))
            .andExpect(jsonPath("$.overtimeOffday").value(DEFAULT_OVERTIME_OFFDAY))
            .andExpect(jsonPath("$.totalSalary").value(DEFAULT_TOTAL_SALARY));
    }

    @Test
    @Transactional
    void getTblEmployeeSalariesByIdFiltering() throws Exception {
        // Initialize the database
        tblEmployeeSalaryRepository.saveAndFlush(tblEmployeeSalary);

        Long id = tblEmployeeSalary.getId();

        defaultTblEmployeeSalaryShouldBeFound("id.equals=" + id);
        defaultTblEmployeeSalaryShouldNotBeFound("id.notEquals=" + id);

        defaultTblEmployeeSalaryShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTblEmployeeSalaryShouldNotBeFound("id.greaterThan=" + id);

        defaultTblEmployeeSalaryShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTblEmployeeSalaryShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTblEmployeeSalariesByDateCalculatedIsEqualToSomething() throws Exception {
        // Initialize the database
        tblEmployeeSalaryRepository.saveAndFlush(tblEmployeeSalary);

        // Get all the tblEmployeeSalaryList where dateCalculated equals to DEFAULT_DATE_CALCULATED
        defaultTblEmployeeSalaryShouldBeFound("dateCalculated.equals=" + DEFAULT_DATE_CALCULATED);

        // Get all the tblEmployeeSalaryList where dateCalculated equals to UPDATED_DATE_CALCULATED
        defaultTblEmployeeSalaryShouldNotBeFound("dateCalculated.equals=" + UPDATED_DATE_CALCULATED);
    }

    @Test
    @Transactional
    void getAllTblEmployeeSalariesByDateCalculatedIsInShouldWork() throws Exception {
        // Initialize the database
        tblEmployeeSalaryRepository.saveAndFlush(tblEmployeeSalary);

        // Get all the tblEmployeeSalaryList where dateCalculated in DEFAULT_DATE_CALCULATED or UPDATED_DATE_CALCULATED
        defaultTblEmployeeSalaryShouldBeFound("dateCalculated.in=" + DEFAULT_DATE_CALCULATED + "," + UPDATED_DATE_CALCULATED);

        // Get all the tblEmployeeSalaryList where dateCalculated equals to UPDATED_DATE_CALCULATED
        defaultTblEmployeeSalaryShouldNotBeFound("dateCalculated.in=" + UPDATED_DATE_CALCULATED);
    }

    @Test
    @Transactional
    void getAllTblEmployeeSalariesByDateCalculatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblEmployeeSalaryRepository.saveAndFlush(tblEmployeeSalary);

        // Get all the tblEmployeeSalaryList where dateCalculated is not null
        defaultTblEmployeeSalaryShouldBeFound("dateCalculated.specified=true");

        // Get all the tblEmployeeSalaryList where dateCalculated is null
        defaultTblEmployeeSalaryShouldNotBeFound("dateCalculated.specified=false");
    }

    @Test
    @Transactional
    void getAllTblEmployeeSalariesByDateCalculatedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tblEmployeeSalaryRepository.saveAndFlush(tblEmployeeSalary);

        // Get all the tblEmployeeSalaryList where dateCalculated is greater than or equal to DEFAULT_DATE_CALCULATED
        defaultTblEmployeeSalaryShouldBeFound("dateCalculated.greaterThanOrEqual=" + DEFAULT_DATE_CALCULATED);

        // Get all the tblEmployeeSalaryList where dateCalculated is greater than or equal to UPDATED_DATE_CALCULATED
        defaultTblEmployeeSalaryShouldNotBeFound("dateCalculated.greaterThanOrEqual=" + UPDATED_DATE_CALCULATED);
    }

    @Test
    @Transactional
    void getAllTblEmployeeSalariesByDateCalculatedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tblEmployeeSalaryRepository.saveAndFlush(tblEmployeeSalary);

        // Get all the tblEmployeeSalaryList where dateCalculated is less than or equal to DEFAULT_DATE_CALCULATED
        defaultTblEmployeeSalaryShouldBeFound("dateCalculated.lessThanOrEqual=" + DEFAULT_DATE_CALCULATED);

        // Get all the tblEmployeeSalaryList where dateCalculated is less than or equal to SMALLER_DATE_CALCULATED
        defaultTblEmployeeSalaryShouldNotBeFound("dateCalculated.lessThanOrEqual=" + SMALLER_DATE_CALCULATED);
    }

    @Test
    @Transactional
    void getAllTblEmployeeSalariesByDateCalculatedIsLessThanSomething() throws Exception {
        // Initialize the database
        tblEmployeeSalaryRepository.saveAndFlush(tblEmployeeSalary);

        // Get all the tblEmployeeSalaryList where dateCalculated is less than DEFAULT_DATE_CALCULATED
        defaultTblEmployeeSalaryShouldNotBeFound("dateCalculated.lessThan=" + DEFAULT_DATE_CALCULATED);

        // Get all the tblEmployeeSalaryList where dateCalculated is less than UPDATED_DATE_CALCULATED
        defaultTblEmployeeSalaryShouldBeFound("dateCalculated.lessThan=" + UPDATED_DATE_CALCULATED);
    }

    @Test
    @Transactional
    void getAllTblEmployeeSalariesByDateCalculatedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tblEmployeeSalaryRepository.saveAndFlush(tblEmployeeSalary);

        // Get all the tblEmployeeSalaryList where dateCalculated is greater than DEFAULT_DATE_CALCULATED
        defaultTblEmployeeSalaryShouldNotBeFound("dateCalculated.greaterThan=" + DEFAULT_DATE_CALCULATED);

        // Get all the tblEmployeeSalaryList where dateCalculated is greater than SMALLER_DATE_CALCULATED
        defaultTblEmployeeSalaryShouldBeFound("dateCalculated.greaterThan=" + SMALLER_DATE_CALCULATED);
    }

    @Test
    @Transactional
    void getAllTblEmployeeSalariesByEmployeeIdIsEqualToSomething() throws Exception {
        // Initialize the database
        tblEmployeeSalaryRepository.saveAndFlush(tblEmployeeSalary);

        // Get all the tblEmployeeSalaryList where employeeId equals to DEFAULT_EMPLOYEE_ID
        defaultTblEmployeeSalaryShouldBeFound("employeeId.equals=" + DEFAULT_EMPLOYEE_ID);

        // Get all the tblEmployeeSalaryList where employeeId equals to UPDATED_EMPLOYEE_ID
        defaultTblEmployeeSalaryShouldNotBeFound("employeeId.equals=" + UPDATED_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllTblEmployeeSalariesByEmployeeIdIsInShouldWork() throws Exception {
        // Initialize the database
        tblEmployeeSalaryRepository.saveAndFlush(tblEmployeeSalary);

        // Get all the tblEmployeeSalaryList where employeeId in DEFAULT_EMPLOYEE_ID or UPDATED_EMPLOYEE_ID
        defaultTblEmployeeSalaryShouldBeFound("employeeId.in=" + DEFAULT_EMPLOYEE_ID + "," + UPDATED_EMPLOYEE_ID);

        // Get all the tblEmployeeSalaryList where employeeId equals to UPDATED_EMPLOYEE_ID
        defaultTblEmployeeSalaryShouldNotBeFound("employeeId.in=" + UPDATED_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllTblEmployeeSalariesByEmployeeIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblEmployeeSalaryRepository.saveAndFlush(tblEmployeeSalary);

        // Get all the tblEmployeeSalaryList where employeeId is not null
        defaultTblEmployeeSalaryShouldBeFound("employeeId.specified=true");

        // Get all the tblEmployeeSalaryList where employeeId is null
        defaultTblEmployeeSalaryShouldNotBeFound("employeeId.specified=false");
    }

    @Test
    @Transactional
    void getAllTblEmployeeSalariesByEmployeeIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tblEmployeeSalaryRepository.saveAndFlush(tblEmployeeSalary);

        // Get all the tblEmployeeSalaryList where employeeId is greater than or equal to DEFAULT_EMPLOYEE_ID
        defaultTblEmployeeSalaryShouldBeFound("employeeId.greaterThanOrEqual=" + DEFAULT_EMPLOYEE_ID);

        // Get all the tblEmployeeSalaryList where employeeId is greater than or equal to UPDATED_EMPLOYEE_ID
        defaultTblEmployeeSalaryShouldNotBeFound("employeeId.greaterThanOrEqual=" + UPDATED_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllTblEmployeeSalariesByEmployeeIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tblEmployeeSalaryRepository.saveAndFlush(tblEmployeeSalary);

        // Get all the tblEmployeeSalaryList where employeeId is less than or equal to DEFAULT_EMPLOYEE_ID
        defaultTblEmployeeSalaryShouldBeFound("employeeId.lessThanOrEqual=" + DEFAULT_EMPLOYEE_ID);

        // Get all the tblEmployeeSalaryList where employeeId is less than or equal to SMALLER_EMPLOYEE_ID
        defaultTblEmployeeSalaryShouldNotBeFound("employeeId.lessThanOrEqual=" + SMALLER_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllTblEmployeeSalariesByEmployeeIdIsLessThanSomething() throws Exception {
        // Initialize the database
        tblEmployeeSalaryRepository.saveAndFlush(tblEmployeeSalary);

        // Get all the tblEmployeeSalaryList where employeeId is less than DEFAULT_EMPLOYEE_ID
        defaultTblEmployeeSalaryShouldNotBeFound("employeeId.lessThan=" + DEFAULT_EMPLOYEE_ID);

        // Get all the tblEmployeeSalaryList where employeeId is less than UPDATED_EMPLOYEE_ID
        defaultTblEmployeeSalaryShouldBeFound("employeeId.lessThan=" + UPDATED_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllTblEmployeeSalariesByEmployeeIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tblEmployeeSalaryRepository.saveAndFlush(tblEmployeeSalary);

        // Get all the tblEmployeeSalaryList where employeeId is greater than DEFAULT_EMPLOYEE_ID
        defaultTblEmployeeSalaryShouldNotBeFound("employeeId.greaterThan=" + DEFAULT_EMPLOYEE_ID);

        // Get all the tblEmployeeSalaryList where employeeId is greater than SMALLER_EMPLOYEE_ID
        defaultTblEmployeeSalaryShouldBeFound("employeeId.greaterThan=" + SMALLER_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllTblEmployeeSalariesByUndertimeIsEqualToSomething() throws Exception {
        // Initialize the database
        tblEmployeeSalaryRepository.saveAndFlush(tblEmployeeSalary);

        // Get all the tblEmployeeSalaryList where undertime equals to DEFAULT_UNDERTIME
        defaultTblEmployeeSalaryShouldBeFound("undertime.equals=" + DEFAULT_UNDERTIME);

        // Get all the tblEmployeeSalaryList where undertime equals to UPDATED_UNDERTIME
        defaultTblEmployeeSalaryShouldNotBeFound("undertime.equals=" + UPDATED_UNDERTIME);
    }

    @Test
    @Transactional
    void getAllTblEmployeeSalariesByUndertimeIsInShouldWork() throws Exception {
        // Initialize the database
        tblEmployeeSalaryRepository.saveAndFlush(tblEmployeeSalary);

        // Get all the tblEmployeeSalaryList where undertime in DEFAULT_UNDERTIME or UPDATED_UNDERTIME
        defaultTblEmployeeSalaryShouldBeFound("undertime.in=" + DEFAULT_UNDERTIME + "," + UPDATED_UNDERTIME);

        // Get all the tblEmployeeSalaryList where undertime equals to UPDATED_UNDERTIME
        defaultTblEmployeeSalaryShouldNotBeFound("undertime.in=" + UPDATED_UNDERTIME);
    }

    @Test
    @Transactional
    void getAllTblEmployeeSalariesByUndertimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblEmployeeSalaryRepository.saveAndFlush(tblEmployeeSalary);

        // Get all the tblEmployeeSalaryList where undertime is not null
        defaultTblEmployeeSalaryShouldBeFound("undertime.specified=true");

        // Get all the tblEmployeeSalaryList where undertime is null
        defaultTblEmployeeSalaryShouldNotBeFound("undertime.specified=false");
    }

    @Test
    @Transactional
    void getAllTblEmployeeSalariesByUndertimeContainsSomething() throws Exception {
        // Initialize the database
        tblEmployeeSalaryRepository.saveAndFlush(tblEmployeeSalary);

        // Get all the tblEmployeeSalaryList where undertime contains DEFAULT_UNDERTIME
        defaultTblEmployeeSalaryShouldBeFound("undertime.contains=" + DEFAULT_UNDERTIME);

        // Get all the tblEmployeeSalaryList where undertime contains UPDATED_UNDERTIME
        defaultTblEmployeeSalaryShouldNotBeFound("undertime.contains=" + UPDATED_UNDERTIME);
    }

    @Test
    @Transactional
    void getAllTblEmployeeSalariesByUndertimeNotContainsSomething() throws Exception {
        // Initialize the database
        tblEmployeeSalaryRepository.saveAndFlush(tblEmployeeSalary);

        // Get all the tblEmployeeSalaryList where undertime does not contain DEFAULT_UNDERTIME
        defaultTblEmployeeSalaryShouldNotBeFound("undertime.doesNotContain=" + DEFAULT_UNDERTIME);

        // Get all the tblEmployeeSalaryList where undertime does not contain UPDATED_UNDERTIME
        defaultTblEmployeeSalaryShouldBeFound("undertime.doesNotContain=" + UPDATED_UNDERTIME);
    }

    @Test
    @Transactional
    void getAllTblEmployeeSalariesByOvertimeIsEqualToSomething() throws Exception {
        // Initialize the database
        tblEmployeeSalaryRepository.saveAndFlush(tblEmployeeSalary);

        // Get all the tblEmployeeSalaryList where overtime equals to DEFAULT_OVERTIME
        defaultTblEmployeeSalaryShouldBeFound("overtime.equals=" + DEFAULT_OVERTIME);

        // Get all the tblEmployeeSalaryList where overtime equals to UPDATED_OVERTIME
        defaultTblEmployeeSalaryShouldNotBeFound("overtime.equals=" + UPDATED_OVERTIME);
    }

    @Test
    @Transactional
    void getAllTblEmployeeSalariesByOvertimeIsInShouldWork() throws Exception {
        // Initialize the database
        tblEmployeeSalaryRepository.saveAndFlush(tblEmployeeSalary);

        // Get all the tblEmployeeSalaryList where overtime in DEFAULT_OVERTIME or UPDATED_OVERTIME
        defaultTblEmployeeSalaryShouldBeFound("overtime.in=" + DEFAULT_OVERTIME + "," + UPDATED_OVERTIME);

        // Get all the tblEmployeeSalaryList where overtime equals to UPDATED_OVERTIME
        defaultTblEmployeeSalaryShouldNotBeFound("overtime.in=" + UPDATED_OVERTIME);
    }

    @Test
    @Transactional
    void getAllTblEmployeeSalariesByOvertimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblEmployeeSalaryRepository.saveAndFlush(tblEmployeeSalary);

        // Get all the tblEmployeeSalaryList where overtime is not null
        defaultTblEmployeeSalaryShouldBeFound("overtime.specified=true");

        // Get all the tblEmployeeSalaryList where overtime is null
        defaultTblEmployeeSalaryShouldNotBeFound("overtime.specified=false");
    }

    @Test
    @Transactional
    void getAllTblEmployeeSalariesByOvertimeContainsSomething() throws Exception {
        // Initialize the database
        tblEmployeeSalaryRepository.saveAndFlush(tblEmployeeSalary);

        // Get all the tblEmployeeSalaryList where overtime contains DEFAULT_OVERTIME
        defaultTblEmployeeSalaryShouldBeFound("overtime.contains=" + DEFAULT_OVERTIME);

        // Get all the tblEmployeeSalaryList where overtime contains UPDATED_OVERTIME
        defaultTblEmployeeSalaryShouldNotBeFound("overtime.contains=" + UPDATED_OVERTIME);
    }

    @Test
    @Transactional
    void getAllTblEmployeeSalariesByOvertimeNotContainsSomething() throws Exception {
        // Initialize the database
        tblEmployeeSalaryRepository.saveAndFlush(tblEmployeeSalary);

        // Get all the tblEmployeeSalaryList where overtime does not contain DEFAULT_OVERTIME
        defaultTblEmployeeSalaryShouldNotBeFound("overtime.doesNotContain=" + DEFAULT_OVERTIME);

        // Get all the tblEmployeeSalaryList where overtime does not contain UPDATED_OVERTIME
        defaultTblEmployeeSalaryShouldBeFound("overtime.doesNotContain=" + UPDATED_OVERTIME);
    }

    @Test
    @Transactional
    void getAllTblEmployeeSalariesByOvertimeOffdayIsEqualToSomething() throws Exception {
        // Initialize the database
        tblEmployeeSalaryRepository.saveAndFlush(tblEmployeeSalary);

        // Get all the tblEmployeeSalaryList where overtimeOffday equals to DEFAULT_OVERTIME_OFFDAY
        defaultTblEmployeeSalaryShouldBeFound("overtimeOffday.equals=" + DEFAULT_OVERTIME_OFFDAY);

        // Get all the tblEmployeeSalaryList where overtimeOffday equals to UPDATED_OVERTIME_OFFDAY
        defaultTblEmployeeSalaryShouldNotBeFound("overtimeOffday.equals=" + UPDATED_OVERTIME_OFFDAY);
    }

    @Test
    @Transactional
    void getAllTblEmployeeSalariesByOvertimeOffdayIsInShouldWork() throws Exception {
        // Initialize the database
        tblEmployeeSalaryRepository.saveAndFlush(tblEmployeeSalary);

        // Get all the tblEmployeeSalaryList where overtimeOffday in DEFAULT_OVERTIME_OFFDAY or UPDATED_OVERTIME_OFFDAY
        defaultTblEmployeeSalaryShouldBeFound("overtimeOffday.in=" + DEFAULT_OVERTIME_OFFDAY + "," + UPDATED_OVERTIME_OFFDAY);

        // Get all the tblEmployeeSalaryList where overtimeOffday equals to UPDATED_OVERTIME_OFFDAY
        defaultTblEmployeeSalaryShouldNotBeFound("overtimeOffday.in=" + UPDATED_OVERTIME_OFFDAY);
    }

    @Test
    @Transactional
    void getAllTblEmployeeSalariesByOvertimeOffdayIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblEmployeeSalaryRepository.saveAndFlush(tblEmployeeSalary);

        // Get all the tblEmployeeSalaryList where overtimeOffday is not null
        defaultTblEmployeeSalaryShouldBeFound("overtimeOffday.specified=true");

        // Get all the tblEmployeeSalaryList where overtimeOffday is null
        defaultTblEmployeeSalaryShouldNotBeFound("overtimeOffday.specified=false");
    }

    @Test
    @Transactional
    void getAllTblEmployeeSalariesByOvertimeOffdayContainsSomething() throws Exception {
        // Initialize the database
        tblEmployeeSalaryRepository.saveAndFlush(tblEmployeeSalary);

        // Get all the tblEmployeeSalaryList where overtimeOffday contains DEFAULT_OVERTIME_OFFDAY
        defaultTblEmployeeSalaryShouldBeFound("overtimeOffday.contains=" + DEFAULT_OVERTIME_OFFDAY);

        // Get all the tblEmployeeSalaryList where overtimeOffday contains UPDATED_OVERTIME_OFFDAY
        defaultTblEmployeeSalaryShouldNotBeFound("overtimeOffday.contains=" + UPDATED_OVERTIME_OFFDAY);
    }

    @Test
    @Transactional
    void getAllTblEmployeeSalariesByOvertimeOffdayNotContainsSomething() throws Exception {
        // Initialize the database
        tblEmployeeSalaryRepository.saveAndFlush(tblEmployeeSalary);

        // Get all the tblEmployeeSalaryList where overtimeOffday does not contain DEFAULT_OVERTIME_OFFDAY
        defaultTblEmployeeSalaryShouldNotBeFound("overtimeOffday.doesNotContain=" + DEFAULT_OVERTIME_OFFDAY);

        // Get all the tblEmployeeSalaryList where overtimeOffday does not contain UPDATED_OVERTIME_OFFDAY
        defaultTblEmployeeSalaryShouldBeFound("overtimeOffday.doesNotContain=" + UPDATED_OVERTIME_OFFDAY);
    }

    @Test
    @Transactional
    void getAllTblEmployeeSalariesByTotalSalaryIsEqualToSomething() throws Exception {
        // Initialize the database
        tblEmployeeSalaryRepository.saveAndFlush(tblEmployeeSalary);

        // Get all the tblEmployeeSalaryList where totalSalary equals to DEFAULT_TOTAL_SALARY
        defaultTblEmployeeSalaryShouldBeFound("totalSalary.equals=" + DEFAULT_TOTAL_SALARY);

        // Get all the tblEmployeeSalaryList where totalSalary equals to UPDATED_TOTAL_SALARY
        defaultTblEmployeeSalaryShouldNotBeFound("totalSalary.equals=" + UPDATED_TOTAL_SALARY);
    }

    @Test
    @Transactional
    void getAllTblEmployeeSalariesByTotalSalaryIsInShouldWork() throws Exception {
        // Initialize the database
        tblEmployeeSalaryRepository.saveAndFlush(tblEmployeeSalary);

        // Get all the tblEmployeeSalaryList where totalSalary in DEFAULT_TOTAL_SALARY or UPDATED_TOTAL_SALARY
        defaultTblEmployeeSalaryShouldBeFound("totalSalary.in=" + DEFAULT_TOTAL_SALARY + "," + UPDATED_TOTAL_SALARY);

        // Get all the tblEmployeeSalaryList where totalSalary equals to UPDATED_TOTAL_SALARY
        defaultTblEmployeeSalaryShouldNotBeFound("totalSalary.in=" + UPDATED_TOTAL_SALARY);
    }

    @Test
    @Transactional
    void getAllTblEmployeeSalariesByTotalSalaryIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblEmployeeSalaryRepository.saveAndFlush(tblEmployeeSalary);

        // Get all the tblEmployeeSalaryList where totalSalary is not null
        defaultTblEmployeeSalaryShouldBeFound("totalSalary.specified=true");

        // Get all the tblEmployeeSalaryList where totalSalary is null
        defaultTblEmployeeSalaryShouldNotBeFound("totalSalary.specified=false");
    }

    @Test
    @Transactional
    void getAllTblEmployeeSalariesByTotalSalaryContainsSomething() throws Exception {
        // Initialize the database
        tblEmployeeSalaryRepository.saveAndFlush(tblEmployeeSalary);

        // Get all the tblEmployeeSalaryList where totalSalary contains DEFAULT_TOTAL_SALARY
        defaultTblEmployeeSalaryShouldBeFound("totalSalary.contains=" + DEFAULT_TOTAL_SALARY);

        // Get all the tblEmployeeSalaryList where totalSalary contains UPDATED_TOTAL_SALARY
        defaultTblEmployeeSalaryShouldNotBeFound("totalSalary.contains=" + UPDATED_TOTAL_SALARY);
    }

    @Test
    @Transactional
    void getAllTblEmployeeSalariesByTotalSalaryNotContainsSomething() throws Exception {
        // Initialize the database
        tblEmployeeSalaryRepository.saveAndFlush(tblEmployeeSalary);

        // Get all the tblEmployeeSalaryList where totalSalary does not contain DEFAULT_TOTAL_SALARY
        defaultTblEmployeeSalaryShouldNotBeFound("totalSalary.doesNotContain=" + DEFAULT_TOTAL_SALARY);

        // Get all the tblEmployeeSalaryList where totalSalary does not contain UPDATED_TOTAL_SALARY
        defaultTblEmployeeSalaryShouldBeFound("totalSalary.doesNotContain=" + UPDATED_TOTAL_SALARY);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTblEmployeeSalaryShouldBeFound(String filter) throws Exception {
        restTblEmployeeSalaryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tblEmployeeSalary.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateCalculated").value(hasItem(sameInstant(DEFAULT_DATE_CALCULATED))))
            .andExpect(jsonPath("$.[*].employeeId").value(hasItem(DEFAULT_EMPLOYEE_ID)))
            .andExpect(jsonPath("$.[*].undertime").value(hasItem(DEFAULT_UNDERTIME)))
            .andExpect(jsonPath("$.[*].overtime").value(hasItem(DEFAULT_OVERTIME)))
            .andExpect(jsonPath("$.[*].overtimeOffday").value(hasItem(DEFAULT_OVERTIME_OFFDAY)))
            .andExpect(jsonPath("$.[*].totalSalary").value(hasItem(DEFAULT_TOTAL_SALARY)));

        // Check, that the count call also returns 1
        restTblEmployeeSalaryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTblEmployeeSalaryShouldNotBeFound(String filter) throws Exception {
        restTblEmployeeSalaryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTblEmployeeSalaryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTblEmployeeSalary() throws Exception {
        // Get the tblEmployeeSalary
        restTblEmployeeSalaryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTblEmployeeSalary() throws Exception {
        // Initialize the database
        tblEmployeeSalaryRepository.saveAndFlush(tblEmployeeSalary);

        int databaseSizeBeforeUpdate = tblEmployeeSalaryRepository.findAll().size();

        // Update the tblEmployeeSalary
        TblEmployeeSalary updatedTblEmployeeSalary = tblEmployeeSalaryRepository.findById(tblEmployeeSalary.getId()).get();
        // Disconnect from session so that the updates on updatedTblEmployeeSalary are not directly saved in db
        em.detach(updatedTblEmployeeSalary);
        updatedTblEmployeeSalary
            .dateCalculated(UPDATED_DATE_CALCULATED)
            .employeeId(UPDATED_EMPLOYEE_ID)
            .undertime(UPDATED_UNDERTIME)
            .overtime(UPDATED_OVERTIME)
            .overtimeOffday(UPDATED_OVERTIME_OFFDAY)
            .totalSalary(UPDATED_TOTAL_SALARY);

        restTblEmployeeSalaryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTblEmployeeSalary.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTblEmployeeSalary))
            )
            .andExpect(status().isOk());

        // Validate the TblEmployeeSalary in the database
        List<TblEmployeeSalary> tblEmployeeSalaryList = tblEmployeeSalaryRepository.findAll();
        assertThat(tblEmployeeSalaryList).hasSize(databaseSizeBeforeUpdate);
        TblEmployeeSalary testTblEmployeeSalary = tblEmployeeSalaryList.get(tblEmployeeSalaryList.size() - 1);
        assertThat(testTblEmployeeSalary.getDateCalculated()).isEqualTo(UPDATED_DATE_CALCULATED);
        assertThat(testTblEmployeeSalary.getEmployeeId()).isEqualTo(UPDATED_EMPLOYEE_ID);
        assertThat(testTblEmployeeSalary.getUndertime()).isEqualTo(UPDATED_UNDERTIME);
        assertThat(testTblEmployeeSalary.getOvertime()).isEqualTo(UPDATED_OVERTIME);
        assertThat(testTblEmployeeSalary.getOvertimeOffday()).isEqualTo(UPDATED_OVERTIME_OFFDAY);
        assertThat(testTblEmployeeSalary.getTotalSalary()).isEqualTo(UPDATED_TOTAL_SALARY);
    }

    @Test
    @Transactional
    void putNonExistingTblEmployeeSalary() throws Exception {
        int databaseSizeBeforeUpdate = tblEmployeeSalaryRepository.findAll().size();
        tblEmployeeSalary.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTblEmployeeSalaryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tblEmployeeSalary.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tblEmployeeSalary))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblEmployeeSalary in the database
        List<TblEmployeeSalary> tblEmployeeSalaryList = tblEmployeeSalaryRepository.findAll();
        assertThat(tblEmployeeSalaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTblEmployeeSalary() throws Exception {
        int databaseSizeBeforeUpdate = tblEmployeeSalaryRepository.findAll().size();
        tblEmployeeSalary.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblEmployeeSalaryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tblEmployeeSalary))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblEmployeeSalary in the database
        List<TblEmployeeSalary> tblEmployeeSalaryList = tblEmployeeSalaryRepository.findAll();
        assertThat(tblEmployeeSalaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTblEmployeeSalary() throws Exception {
        int databaseSizeBeforeUpdate = tblEmployeeSalaryRepository.findAll().size();
        tblEmployeeSalary.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblEmployeeSalaryMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tblEmployeeSalary))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TblEmployeeSalary in the database
        List<TblEmployeeSalary> tblEmployeeSalaryList = tblEmployeeSalaryRepository.findAll();
        assertThat(tblEmployeeSalaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTblEmployeeSalaryWithPatch() throws Exception {
        // Initialize the database
        tblEmployeeSalaryRepository.saveAndFlush(tblEmployeeSalary);

        int databaseSizeBeforeUpdate = tblEmployeeSalaryRepository.findAll().size();

        // Update the tblEmployeeSalary using partial update
        TblEmployeeSalary partialUpdatedTblEmployeeSalary = new TblEmployeeSalary();
        partialUpdatedTblEmployeeSalary.setId(tblEmployeeSalary.getId());

        partialUpdatedTblEmployeeSalary
            .dateCalculated(UPDATED_DATE_CALCULATED)
            .employeeId(UPDATED_EMPLOYEE_ID)
            .overtimeOffday(UPDATED_OVERTIME_OFFDAY)
            .totalSalary(UPDATED_TOTAL_SALARY);

        restTblEmployeeSalaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTblEmployeeSalary.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTblEmployeeSalary))
            )
            .andExpect(status().isOk());

        // Validate the TblEmployeeSalary in the database
        List<TblEmployeeSalary> tblEmployeeSalaryList = tblEmployeeSalaryRepository.findAll();
        assertThat(tblEmployeeSalaryList).hasSize(databaseSizeBeforeUpdate);
        TblEmployeeSalary testTblEmployeeSalary = tblEmployeeSalaryList.get(tblEmployeeSalaryList.size() - 1);
        assertThat(testTblEmployeeSalary.getDateCalculated()).isEqualTo(UPDATED_DATE_CALCULATED);
        assertThat(testTblEmployeeSalary.getEmployeeId()).isEqualTo(UPDATED_EMPLOYEE_ID);
        assertThat(testTblEmployeeSalary.getUndertime()).isEqualTo(DEFAULT_UNDERTIME);
        assertThat(testTblEmployeeSalary.getOvertime()).isEqualTo(DEFAULT_OVERTIME);
        assertThat(testTblEmployeeSalary.getOvertimeOffday()).isEqualTo(UPDATED_OVERTIME_OFFDAY);
        assertThat(testTblEmployeeSalary.getTotalSalary()).isEqualTo(UPDATED_TOTAL_SALARY);
    }

    @Test
    @Transactional
    void fullUpdateTblEmployeeSalaryWithPatch() throws Exception {
        // Initialize the database
        tblEmployeeSalaryRepository.saveAndFlush(tblEmployeeSalary);

        int databaseSizeBeforeUpdate = tblEmployeeSalaryRepository.findAll().size();

        // Update the tblEmployeeSalary using partial update
        TblEmployeeSalary partialUpdatedTblEmployeeSalary = new TblEmployeeSalary();
        partialUpdatedTblEmployeeSalary.setId(tblEmployeeSalary.getId());

        partialUpdatedTblEmployeeSalary
            .dateCalculated(UPDATED_DATE_CALCULATED)
            .employeeId(UPDATED_EMPLOYEE_ID)
            .undertime(UPDATED_UNDERTIME)
            .overtime(UPDATED_OVERTIME)
            .overtimeOffday(UPDATED_OVERTIME_OFFDAY)
            .totalSalary(UPDATED_TOTAL_SALARY);

        restTblEmployeeSalaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTblEmployeeSalary.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTblEmployeeSalary))
            )
            .andExpect(status().isOk());

        // Validate the TblEmployeeSalary in the database
        List<TblEmployeeSalary> tblEmployeeSalaryList = tblEmployeeSalaryRepository.findAll();
        assertThat(tblEmployeeSalaryList).hasSize(databaseSizeBeforeUpdate);
        TblEmployeeSalary testTblEmployeeSalary = tblEmployeeSalaryList.get(tblEmployeeSalaryList.size() - 1);
        assertThat(testTblEmployeeSalary.getDateCalculated()).isEqualTo(UPDATED_DATE_CALCULATED);
        assertThat(testTblEmployeeSalary.getEmployeeId()).isEqualTo(UPDATED_EMPLOYEE_ID);
        assertThat(testTblEmployeeSalary.getUndertime()).isEqualTo(UPDATED_UNDERTIME);
        assertThat(testTblEmployeeSalary.getOvertime()).isEqualTo(UPDATED_OVERTIME);
        assertThat(testTblEmployeeSalary.getOvertimeOffday()).isEqualTo(UPDATED_OVERTIME_OFFDAY);
        assertThat(testTblEmployeeSalary.getTotalSalary()).isEqualTo(UPDATED_TOTAL_SALARY);
    }

    @Test
    @Transactional
    void patchNonExistingTblEmployeeSalary() throws Exception {
        int databaseSizeBeforeUpdate = tblEmployeeSalaryRepository.findAll().size();
        tblEmployeeSalary.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTblEmployeeSalaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tblEmployeeSalary.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tblEmployeeSalary))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblEmployeeSalary in the database
        List<TblEmployeeSalary> tblEmployeeSalaryList = tblEmployeeSalaryRepository.findAll();
        assertThat(tblEmployeeSalaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTblEmployeeSalary() throws Exception {
        int databaseSizeBeforeUpdate = tblEmployeeSalaryRepository.findAll().size();
        tblEmployeeSalary.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblEmployeeSalaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tblEmployeeSalary))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblEmployeeSalary in the database
        List<TblEmployeeSalary> tblEmployeeSalaryList = tblEmployeeSalaryRepository.findAll();
        assertThat(tblEmployeeSalaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTblEmployeeSalary() throws Exception {
        int databaseSizeBeforeUpdate = tblEmployeeSalaryRepository.findAll().size();
        tblEmployeeSalary.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblEmployeeSalaryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tblEmployeeSalary))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TblEmployeeSalary in the database
        List<TblEmployeeSalary> tblEmployeeSalaryList = tblEmployeeSalaryRepository.findAll();
        assertThat(tblEmployeeSalaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTblEmployeeSalary() throws Exception {
        // Initialize the database
        tblEmployeeSalaryRepository.saveAndFlush(tblEmployeeSalary);

        int databaseSizeBeforeDelete = tblEmployeeSalaryRepository.findAll().size();

        // Delete the tblEmployeeSalary
        restTblEmployeeSalaryMockMvc
            .perform(delete(ENTITY_API_URL_ID, tblEmployeeSalary.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TblEmployeeSalary> tblEmployeeSalaryList = tblEmployeeSalaryRepository.findAll();
        assertThat(tblEmployeeSalaryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
