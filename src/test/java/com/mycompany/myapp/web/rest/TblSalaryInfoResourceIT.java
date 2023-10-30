package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.TblSalaryInfo;
import com.mycompany.myapp.repository.TblSalaryInfoRepository;
import com.mycompany.myapp.service.criteria.TblSalaryInfoCriteria;
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
 * Integration tests for the {@link TblSalaryInfoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TblSalaryInfoResourceIT {

    private static final Integer DEFAULT_EMPLOYEE_ID = 1;
    private static final Integer UPDATED_EMPLOYEE_ID = 2;
    private static final Integer SMALLER_EMPLOYEE_ID = 1 - 1;

    private static final Integer DEFAULT_BASE_SALARY = 1;
    private static final Integer UPDATED_BASE_SALARY = 2;
    private static final Integer SMALLER_BASE_SALARY = 1 - 1;

    private static final Integer DEFAULT_HOUSING_RIGHTS = 1;
    private static final Integer UPDATED_HOUSING_RIGHTS = 2;
    private static final Integer SMALLER_HOUSING_RIGHTS = 1 - 1;

    private static final Integer DEFAULT_INTERNET_RIGHTS = 1;
    private static final Integer UPDATED_INTERNET_RIGHTS = 2;
    private static final Integer SMALLER_INTERNET_RIGHTS = 1 - 1;

    private static final Integer DEFAULT_GROCERIES_RIGHTS = 1;
    private static final Integer UPDATED_GROCERIES_RIGHTS = 2;
    private static final Integer SMALLER_GROCERIES_RIGHTS = 1 - 1;

    private static final String ENTITY_API_URL = "/api/tbl-salary-infos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TblSalaryInfoRepository tblSalaryInfoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTblSalaryInfoMockMvc;

    private TblSalaryInfo tblSalaryInfo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TblSalaryInfo createEntity(EntityManager em) {
        TblSalaryInfo tblSalaryInfo = new TblSalaryInfo()
            .employeeId(DEFAULT_EMPLOYEE_ID)
            .baseSalary(DEFAULT_BASE_SALARY)
            .housingRights(DEFAULT_HOUSING_RIGHTS)
            .internetRights(DEFAULT_INTERNET_RIGHTS)
            .groceriesRights(DEFAULT_GROCERIES_RIGHTS);
        return tblSalaryInfo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TblSalaryInfo createUpdatedEntity(EntityManager em) {
        TblSalaryInfo tblSalaryInfo = new TblSalaryInfo()
            .employeeId(UPDATED_EMPLOYEE_ID)
            .baseSalary(UPDATED_BASE_SALARY)
            .housingRights(UPDATED_HOUSING_RIGHTS)
            .internetRights(UPDATED_INTERNET_RIGHTS)
            .groceriesRights(UPDATED_GROCERIES_RIGHTS);
        return tblSalaryInfo;
    }

    @BeforeEach
    public void initTest() {
        tblSalaryInfo = createEntity(em);
    }

    @Test
    @Transactional
    void createTblSalaryInfo() throws Exception {
        int databaseSizeBeforeCreate = tblSalaryInfoRepository.findAll().size();
        // Create the TblSalaryInfo
        restTblSalaryInfoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tblSalaryInfo)))
            .andExpect(status().isCreated());

        // Validate the TblSalaryInfo in the database
        List<TblSalaryInfo> tblSalaryInfoList = tblSalaryInfoRepository.findAll();
        assertThat(tblSalaryInfoList).hasSize(databaseSizeBeforeCreate + 1);
        TblSalaryInfo testTblSalaryInfo = tblSalaryInfoList.get(tblSalaryInfoList.size() - 1);
        assertThat(testTblSalaryInfo.getEmployeeId()).isEqualTo(DEFAULT_EMPLOYEE_ID);
        assertThat(testTblSalaryInfo.getBaseSalary()).isEqualTo(DEFAULT_BASE_SALARY);
        assertThat(testTblSalaryInfo.getHousingRights()).isEqualTo(DEFAULT_HOUSING_RIGHTS);
        assertThat(testTblSalaryInfo.getInternetRights()).isEqualTo(DEFAULT_INTERNET_RIGHTS);
        assertThat(testTblSalaryInfo.getGroceriesRights()).isEqualTo(DEFAULT_GROCERIES_RIGHTS);
    }

    @Test
    @Transactional
    void createTblSalaryInfoWithExistingId() throws Exception {
        // Create the TblSalaryInfo with an existing ID
        tblSalaryInfo.setId(1L);

        int databaseSizeBeforeCreate = tblSalaryInfoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTblSalaryInfoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tblSalaryInfo)))
            .andExpect(status().isBadRequest());

        // Validate the TblSalaryInfo in the database
        List<TblSalaryInfo> tblSalaryInfoList = tblSalaryInfoRepository.findAll();
        assertThat(tblSalaryInfoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTblSalaryInfos() throws Exception {
        // Initialize the database
        tblSalaryInfoRepository.saveAndFlush(tblSalaryInfo);

        // Get all the tblSalaryInfoList
        restTblSalaryInfoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tblSalaryInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].employeeId").value(hasItem(DEFAULT_EMPLOYEE_ID)))
            .andExpect(jsonPath("$.[*].baseSalary").value(hasItem(DEFAULT_BASE_SALARY)))
            .andExpect(jsonPath("$.[*].housingRights").value(hasItem(DEFAULT_HOUSING_RIGHTS)))
            .andExpect(jsonPath("$.[*].internetRights").value(hasItem(DEFAULT_INTERNET_RIGHTS)))
            .andExpect(jsonPath("$.[*].groceriesRights").value(hasItem(DEFAULT_GROCERIES_RIGHTS)));
    }

    @Test
    @Transactional
    void getTblSalaryInfo() throws Exception {
        // Initialize the database
        tblSalaryInfoRepository.saveAndFlush(tblSalaryInfo);

        // Get the tblSalaryInfo
        restTblSalaryInfoMockMvc
            .perform(get(ENTITY_API_URL_ID, tblSalaryInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tblSalaryInfo.getId().intValue()))
            .andExpect(jsonPath("$.employeeId").value(DEFAULT_EMPLOYEE_ID))
            .andExpect(jsonPath("$.baseSalary").value(DEFAULT_BASE_SALARY))
            .andExpect(jsonPath("$.housingRights").value(DEFAULT_HOUSING_RIGHTS))
            .andExpect(jsonPath("$.internetRights").value(DEFAULT_INTERNET_RIGHTS))
            .andExpect(jsonPath("$.groceriesRights").value(DEFAULT_GROCERIES_RIGHTS));
    }

    @Test
    @Transactional
    void getTblSalaryInfosByIdFiltering() throws Exception {
        // Initialize the database
        tblSalaryInfoRepository.saveAndFlush(tblSalaryInfo);

        Long id = tblSalaryInfo.getId();

        defaultTblSalaryInfoShouldBeFound("id.equals=" + id);
        defaultTblSalaryInfoShouldNotBeFound("id.notEquals=" + id);

        defaultTblSalaryInfoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTblSalaryInfoShouldNotBeFound("id.greaterThan=" + id);

        defaultTblSalaryInfoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTblSalaryInfoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTblSalaryInfosByEmployeeIdIsEqualToSomething() throws Exception {
        // Initialize the database
        tblSalaryInfoRepository.saveAndFlush(tblSalaryInfo);

        // Get all the tblSalaryInfoList where employeeId equals to DEFAULT_EMPLOYEE_ID
        defaultTblSalaryInfoShouldBeFound("employeeId.equals=" + DEFAULT_EMPLOYEE_ID);

        // Get all the tblSalaryInfoList where employeeId equals to UPDATED_EMPLOYEE_ID
        defaultTblSalaryInfoShouldNotBeFound("employeeId.equals=" + UPDATED_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllTblSalaryInfosByEmployeeIdIsInShouldWork() throws Exception {
        // Initialize the database
        tblSalaryInfoRepository.saveAndFlush(tblSalaryInfo);

        // Get all the tblSalaryInfoList where employeeId in DEFAULT_EMPLOYEE_ID or UPDATED_EMPLOYEE_ID
        defaultTblSalaryInfoShouldBeFound("employeeId.in=" + DEFAULT_EMPLOYEE_ID + "," + UPDATED_EMPLOYEE_ID);

        // Get all the tblSalaryInfoList where employeeId equals to UPDATED_EMPLOYEE_ID
        defaultTblSalaryInfoShouldNotBeFound("employeeId.in=" + UPDATED_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllTblSalaryInfosByEmployeeIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblSalaryInfoRepository.saveAndFlush(tblSalaryInfo);

        // Get all the tblSalaryInfoList where employeeId is not null
        defaultTblSalaryInfoShouldBeFound("employeeId.specified=true");

        // Get all the tblSalaryInfoList where employeeId is null
        defaultTblSalaryInfoShouldNotBeFound("employeeId.specified=false");
    }

    @Test
    @Transactional
    void getAllTblSalaryInfosByEmployeeIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tblSalaryInfoRepository.saveAndFlush(tblSalaryInfo);

        // Get all the tblSalaryInfoList where employeeId is greater than or equal to DEFAULT_EMPLOYEE_ID
        defaultTblSalaryInfoShouldBeFound("employeeId.greaterThanOrEqual=" + DEFAULT_EMPLOYEE_ID);

        // Get all the tblSalaryInfoList where employeeId is greater than or equal to UPDATED_EMPLOYEE_ID
        defaultTblSalaryInfoShouldNotBeFound("employeeId.greaterThanOrEqual=" + UPDATED_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllTblSalaryInfosByEmployeeIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tblSalaryInfoRepository.saveAndFlush(tblSalaryInfo);

        // Get all the tblSalaryInfoList where employeeId is less than or equal to DEFAULT_EMPLOYEE_ID
        defaultTblSalaryInfoShouldBeFound("employeeId.lessThanOrEqual=" + DEFAULT_EMPLOYEE_ID);

        // Get all the tblSalaryInfoList where employeeId is less than or equal to SMALLER_EMPLOYEE_ID
        defaultTblSalaryInfoShouldNotBeFound("employeeId.lessThanOrEqual=" + SMALLER_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllTblSalaryInfosByEmployeeIdIsLessThanSomething() throws Exception {
        // Initialize the database
        tblSalaryInfoRepository.saveAndFlush(tblSalaryInfo);

        // Get all the tblSalaryInfoList where employeeId is less than DEFAULT_EMPLOYEE_ID
        defaultTblSalaryInfoShouldNotBeFound("employeeId.lessThan=" + DEFAULT_EMPLOYEE_ID);

        // Get all the tblSalaryInfoList where employeeId is less than UPDATED_EMPLOYEE_ID
        defaultTblSalaryInfoShouldBeFound("employeeId.lessThan=" + UPDATED_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllTblSalaryInfosByEmployeeIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tblSalaryInfoRepository.saveAndFlush(tblSalaryInfo);

        // Get all the tblSalaryInfoList where employeeId is greater than DEFAULT_EMPLOYEE_ID
        defaultTblSalaryInfoShouldNotBeFound("employeeId.greaterThan=" + DEFAULT_EMPLOYEE_ID);

        // Get all the tblSalaryInfoList where employeeId is greater than SMALLER_EMPLOYEE_ID
        defaultTblSalaryInfoShouldBeFound("employeeId.greaterThan=" + SMALLER_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllTblSalaryInfosByBaseSalaryIsEqualToSomething() throws Exception {
        // Initialize the database
        tblSalaryInfoRepository.saveAndFlush(tblSalaryInfo);

        // Get all the tblSalaryInfoList where baseSalary equals to DEFAULT_BASE_SALARY
        defaultTblSalaryInfoShouldBeFound("baseSalary.equals=" + DEFAULT_BASE_SALARY);

        // Get all the tblSalaryInfoList where baseSalary equals to UPDATED_BASE_SALARY
        defaultTblSalaryInfoShouldNotBeFound("baseSalary.equals=" + UPDATED_BASE_SALARY);
    }

    @Test
    @Transactional
    void getAllTblSalaryInfosByBaseSalaryIsInShouldWork() throws Exception {
        // Initialize the database
        tblSalaryInfoRepository.saveAndFlush(tblSalaryInfo);

        // Get all the tblSalaryInfoList where baseSalary in DEFAULT_BASE_SALARY or UPDATED_BASE_SALARY
        defaultTblSalaryInfoShouldBeFound("baseSalary.in=" + DEFAULT_BASE_SALARY + "," + UPDATED_BASE_SALARY);

        // Get all the tblSalaryInfoList where baseSalary equals to UPDATED_BASE_SALARY
        defaultTblSalaryInfoShouldNotBeFound("baseSalary.in=" + UPDATED_BASE_SALARY);
    }

    @Test
    @Transactional
    void getAllTblSalaryInfosByBaseSalaryIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblSalaryInfoRepository.saveAndFlush(tblSalaryInfo);

        // Get all the tblSalaryInfoList where baseSalary is not null
        defaultTblSalaryInfoShouldBeFound("baseSalary.specified=true");

        // Get all the tblSalaryInfoList where baseSalary is null
        defaultTblSalaryInfoShouldNotBeFound("baseSalary.specified=false");
    }

    @Test
    @Transactional
    void getAllTblSalaryInfosByBaseSalaryIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tblSalaryInfoRepository.saveAndFlush(tblSalaryInfo);

        // Get all the tblSalaryInfoList where baseSalary is greater than or equal to DEFAULT_BASE_SALARY
        defaultTblSalaryInfoShouldBeFound("baseSalary.greaterThanOrEqual=" + DEFAULT_BASE_SALARY);

        // Get all the tblSalaryInfoList where baseSalary is greater than or equal to UPDATED_BASE_SALARY
        defaultTblSalaryInfoShouldNotBeFound("baseSalary.greaterThanOrEqual=" + UPDATED_BASE_SALARY);
    }

    @Test
    @Transactional
    void getAllTblSalaryInfosByBaseSalaryIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tblSalaryInfoRepository.saveAndFlush(tblSalaryInfo);

        // Get all the tblSalaryInfoList where baseSalary is less than or equal to DEFAULT_BASE_SALARY
        defaultTblSalaryInfoShouldBeFound("baseSalary.lessThanOrEqual=" + DEFAULT_BASE_SALARY);

        // Get all the tblSalaryInfoList where baseSalary is less than or equal to SMALLER_BASE_SALARY
        defaultTblSalaryInfoShouldNotBeFound("baseSalary.lessThanOrEqual=" + SMALLER_BASE_SALARY);
    }

    @Test
    @Transactional
    void getAllTblSalaryInfosByBaseSalaryIsLessThanSomething() throws Exception {
        // Initialize the database
        tblSalaryInfoRepository.saveAndFlush(tblSalaryInfo);

        // Get all the tblSalaryInfoList where baseSalary is less than DEFAULT_BASE_SALARY
        defaultTblSalaryInfoShouldNotBeFound("baseSalary.lessThan=" + DEFAULT_BASE_SALARY);

        // Get all the tblSalaryInfoList where baseSalary is less than UPDATED_BASE_SALARY
        defaultTblSalaryInfoShouldBeFound("baseSalary.lessThan=" + UPDATED_BASE_SALARY);
    }

    @Test
    @Transactional
    void getAllTblSalaryInfosByBaseSalaryIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tblSalaryInfoRepository.saveAndFlush(tblSalaryInfo);

        // Get all the tblSalaryInfoList where baseSalary is greater than DEFAULT_BASE_SALARY
        defaultTblSalaryInfoShouldNotBeFound("baseSalary.greaterThan=" + DEFAULT_BASE_SALARY);

        // Get all the tblSalaryInfoList where baseSalary is greater than SMALLER_BASE_SALARY
        defaultTblSalaryInfoShouldBeFound("baseSalary.greaterThan=" + SMALLER_BASE_SALARY);
    }

    @Test
    @Transactional
    void getAllTblSalaryInfosByHousingRightsIsEqualToSomething() throws Exception {
        // Initialize the database
        tblSalaryInfoRepository.saveAndFlush(tblSalaryInfo);

        // Get all the tblSalaryInfoList where housingRights equals to DEFAULT_HOUSING_RIGHTS
        defaultTblSalaryInfoShouldBeFound("housingRights.equals=" + DEFAULT_HOUSING_RIGHTS);

        // Get all the tblSalaryInfoList where housingRights equals to UPDATED_HOUSING_RIGHTS
        defaultTblSalaryInfoShouldNotBeFound("housingRights.equals=" + UPDATED_HOUSING_RIGHTS);
    }

    @Test
    @Transactional
    void getAllTblSalaryInfosByHousingRightsIsInShouldWork() throws Exception {
        // Initialize the database
        tblSalaryInfoRepository.saveAndFlush(tblSalaryInfo);

        // Get all the tblSalaryInfoList where housingRights in DEFAULT_HOUSING_RIGHTS or UPDATED_HOUSING_RIGHTS
        defaultTblSalaryInfoShouldBeFound("housingRights.in=" + DEFAULT_HOUSING_RIGHTS + "," + UPDATED_HOUSING_RIGHTS);

        // Get all the tblSalaryInfoList where housingRights equals to UPDATED_HOUSING_RIGHTS
        defaultTblSalaryInfoShouldNotBeFound("housingRights.in=" + UPDATED_HOUSING_RIGHTS);
    }

    @Test
    @Transactional
    void getAllTblSalaryInfosByHousingRightsIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblSalaryInfoRepository.saveAndFlush(tblSalaryInfo);

        // Get all the tblSalaryInfoList where housingRights is not null
        defaultTblSalaryInfoShouldBeFound("housingRights.specified=true");

        // Get all the tblSalaryInfoList where housingRights is null
        defaultTblSalaryInfoShouldNotBeFound("housingRights.specified=false");
    }

    @Test
    @Transactional
    void getAllTblSalaryInfosByHousingRightsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tblSalaryInfoRepository.saveAndFlush(tblSalaryInfo);

        // Get all the tblSalaryInfoList where housingRights is greater than or equal to DEFAULT_HOUSING_RIGHTS
        defaultTblSalaryInfoShouldBeFound("housingRights.greaterThanOrEqual=" + DEFAULT_HOUSING_RIGHTS);

        // Get all the tblSalaryInfoList where housingRights is greater than or equal to UPDATED_HOUSING_RIGHTS
        defaultTblSalaryInfoShouldNotBeFound("housingRights.greaterThanOrEqual=" + UPDATED_HOUSING_RIGHTS);
    }

    @Test
    @Transactional
    void getAllTblSalaryInfosByHousingRightsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tblSalaryInfoRepository.saveAndFlush(tblSalaryInfo);

        // Get all the tblSalaryInfoList where housingRights is less than or equal to DEFAULT_HOUSING_RIGHTS
        defaultTblSalaryInfoShouldBeFound("housingRights.lessThanOrEqual=" + DEFAULT_HOUSING_RIGHTS);

        // Get all the tblSalaryInfoList where housingRights is less than or equal to SMALLER_HOUSING_RIGHTS
        defaultTblSalaryInfoShouldNotBeFound("housingRights.lessThanOrEqual=" + SMALLER_HOUSING_RIGHTS);
    }

    @Test
    @Transactional
    void getAllTblSalaryInfosByHousingRightsIsLessThanSomething() throws Exception {
        // Initialize the database
        tblSalaryInfoRepository.saveAndFlush(tblSalaryInfo);

        // Get all the tblSalaryInfoList where housingRights is less than DEFAULT_HOUSING_RIGHTS
        defaultTblSalaryInfoShouldNotBeFound("housingRights.lessThan=" + DEFAULT_HOUSING_RIGHTS);

        // Get all the tblSalaryInfoList where housingRights is less than UPDATED_HOUSING_RIGHTS
        defaultTblSalaryInfoShouldBeFound("housingRights.lessThan=" + UPDATED_HOUSING_RIGHTS);
    }

    @Test
    @Transactional
    void getAllTblSalaryInfosByHousingRightsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tblSalaryInfoRepository.saveAndFlush(tblSalaryInfo);

        // Get all the tblSalaryInfoList where housingRights is greater than DEFAULT_HOUSING_RIGHTS
        defaultTblSalaryInfoShouldNotBeFound("housingRights.greaterThan=" + DEFAULT_HOUSING_RIGHTS);

        // Get all the tblSalaryInfoList where housingRights is greater than SMALLER_HOUSING_RIGHTS
        defaultTblSalaryInfoShouldBeFound("housingRights.greaterThan=" + SMALLER_HOUSING_RIGHTS);
    }

    @Test
    @Transactional
    void getAllTblSalaryInfosByInternetRightsIsEqualToSomething() throws Exception {
        // Initialize the database
        tblSalaryInfoRepository.saveAndFlush(tblSalaryInfo);

        // Get all the tblSalaryInfoList where internetRights equals to DEFAULT_INTERNET_RIGHTS
        defaultTblSalaryInfoShouldBeFound("internetRights.equals=" + DEFAULT_INTERNET_RIGHTS);

        // Get all the tblSalaryInfoList where internetRights equals to UPDATED_INTERNET_RIGHTS
        defaultTblSalaryInfoShouldNotBeFound("internetRights.equals=" + UPDATED_INTERNET_RIGHTS);
    }

    @Test
    @Transactional
    void getAllTblSalaryInfosByInternetRightsIsInShouldWork() throws Exception {
        // Initialize the database
        tblSalaryInfoRepository.saveAndFlush(tblSalaryInfo);

        // Get all the tblSalaryInfoList where internetRights in DEFAULT_INTERNET_RIGHTS or UPDATED_INTERNET_RIGHTS
        defaultTblSalaryInfoShouldBeFound("internetRights.in=" + DEFAULT_INTERNET_RIGHTS + "," + UPDATED_INTERNET_RIGHTS);

        // Get all the tblSalaryInfoList where internetRights equals to UPDATED_INTERNET_RIGHTS
        defaultTblSalaryInfoShouldNotBeFound("internetRights.in=" + UPDATED_INTERNET_RIGHTS);
    }

    @Test
    @Transactional
    void getAllTblSalaryInfosByInternetRightsIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblSalaryInfoRepository.saveAndFlush(tblSalaryInfo);

        // Get all the tblSalaryInfoList where internetRights is not null
        defaultTblSalaryInfoShouldBeFound("internetRights.specified=true");

        // Get all the tblSalaryInfoList where internetRights is null
        defaultTblSalaryInfoShouldNotBeFound("internetRights.specified=false");
    }

    @Test
    @Transactional
    void getAllTblSalaryInfosByInternetRightsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tblSalaryInfoRepository.saveAndFlush(tblSalaryInfo);

        // Get all the tblSalaryInfoList where internetRights is greater than or equal to DEFAULT_INTERNET_RIGHTS
        defaultTblSalaryInfoShouldBeFound("internetRights.greaterThanOrEqual=" + DEFAULT_INTERNET_RIGHTS);

        // Get all the tblSalaryInfoList where internetRights is greater than or equal to UPDATED_INTERNET_RIGHTS
        defaultTblSalaryInfoShouldNotBeFound("internetRights.greaterThanOrEqual=" + UPDATED_INTERNET_RIGHTS);
    }

    @Test
    @Transactional
    void getAllTblSalaryInfosByInternetRightsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tblSalaryInfoRepository.saveAndFlush(tblSalaryInfo);

        // Get all the tblSalaryInfoList where internetRights is less than or equal to DEFAULT_INTERNET_RIGHTS
        defaultTblSalaryInfoShouldBeFound("internetRights.lessThanOrEqual=" + DEFAULT_INTERNET_RIGHTS);

        // Get all the tblSalaryInfoList where internetRights is less than or equal to SMALLER_INTERNET_RIGHTS
        defaultTblSalaryInfoShouldNotBeFound("internetRights.lessThanOrEqual=" + SMALLER_INTERNET_RIGHTS);
    }

    @Test
    @Transactional
    void getAllTblSalaryInfosByInternetRightsIsLessThanSomething() throws Exception {
        // Initialize the database
        tblSalaryInfoRepository.saveAndFlush(tblSalaryInfo);

        // Get all the tblSalaryInfoList where internetRights is less than DEFAULT_INTERNET_RIGHTS
        defaultTblSalaryInfoShouldNotBeFound("internetRights.lessThan=" + DEFAULT_INTERNET_RIGHTS);

        // Get all the tblSalaryInfoList where internetRights is less than UPDATED_INTERNET_RIGHTS
        defaultTblSalaryInfoShouldBeFound("internetRights.lessThan=" + UPDATED_INTERNET_RIGHTS);
    }

    @Test
    @Transactional
    void getAllTblSalaryInfosByInternetRightsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tblSalaryInfoRepository.saveAndFlush(tblSalaryInfo);

        // Get all the tblSalaryInfoList where internetRights is greater than DEFAULT_INTERNET_RIGHTS
        defaultTblSalaryInfoShouldNotBeFound("internetRights.greaterThan=" + DEFAULT_INTERNET_RIGHTS);

        // Get all the tblSalaryInfoList where internetRights is greater than SMALLER_INTERNET_RIGHTS
        defaultTblSalaryInfoShouldBeFound("internetRights.greaterThan=" + SMALLER_INTERNET_RIGHTS);
    }

    @Test
    @Transactional
    void getAllTblSalaryInfosByGroceriesRightsIsEqualToSomething() throws Exception {
        // Initialize the database
        tblSalaryInfoRepository.saveAndFlush(tblSalaryInfo);

        // Get all the tblSalaryInfoList where groceriesRights equals to DEFAULT_GROCERIES_RIGHTS
        defaultTblSalaryInfoShouldBeFound("groceriesRights.equals=" + DEFAULT_GROCERIES_RIGHTS);

        // Get all the tblSalaryInfoList where groceriesRights equals to UPDATED_GROCERIES_RIGHTS
        defaultTblSalaryInfoShouldNotBeFound("groceriesRights.equals=" + UPDATED_GROCERIES_RIGHTS);
    }

    @Test
    @Transactional
    void getAllTblSalaryInfosByGroceriesRightsIsInShouldWork() throws Exception {
        // Initialize the database
        tblSalaryInfoRepository.saveAndFlush(tblSalaryInfo);

        // Get all the tblSalaryInfoList where groceriesRights in DEFAULT_GROCERIES_RIGHTS or UPDATED_GROCERIES_RIGHTS
        defaultTblSalaryInfoShouldBeFound("groceriesRights.in=" + DEFAULT_GROCERIES_RIGHTS + "," + UPDATED_GROCERIES_RIGHTS);

        // Get all the tblSalaryInfoList where groceriesRights equals to UPDATED_GROCERIES_RIGHTS
        defaultTblSalaryInfoShouldNotBeFound("groceriesRights.in=" + UPDATED_GROCERIES_RIGHTS);
    }

    @Test
    @Transactional
    void getAllTblSalaryInfosByGroceriesRightsIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblSalaryInfoRepository.saveAndFlush(tblSalaryInfo);

        // Get all the tblSalaryInfoList where groceriesRights is not null
        defaultTblSalaryInfoShouldBeFound("groceriesRights.specified=true");

        // Get all the tblSalaryInfoList where groceriesRights is null
        defaultTblSalaryInfoShouldNotBeFound("groceriesRights.specified=false");
    }

    @Test
    @Transactional
    void getAllTblSalaryInfosByGroceriesRightsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tblSalaryInfoRepository.saveAndFlush(tblSalaryInfo);

        // Get all the tblSalaryInfoList where groceriesRights is greater than or equal to DEFAULT_GROCERIES_RIGHTS
        defaultTblSalaryInfoShouldBeFound("groceriesRights.greaterThanOrEqual=" + DEFAULT_GROCERIES_RIGHTS);

        // Get all the tblSalaryInfoList where groceriesRights is greater than or equal to UPDATED_GROCERIES_RIGHTS
        defaultTblSalaryInfoShouldNotBeFound("groceriesRights.greaterThanOrEqual=" + UPDATED_GROCERIES_RIGHTS);
    }

    @Test
    @Transactional
    void getAllTblSalaryInfosByGroceriesRightsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tblSalaryInfoRepository.saveAndFlush(tblSalaryInfo);

        // Get all the tblSalaryInfoList where groceriesRights is less than or equal to DEFAULT_GROCERIES_RIGHTS
        defaultTblSalaryInfoShouldBeFound("groceriesRights.lessThanOrEqual=" + DEFAULT_GROCERIES_RIGHTS);

        // Get all the tblSalaryInfoList where groceriesRights is less than or equal to SMALLER_GROCERIES_RIGHTS
        defaultTblSalaryInfoShouldNotBeFound("groceriesRights.lessThanOrEqual=" + SMALLER_GROCERIES_RIGHTS);
    }

    @Test
    @Transactional
    void getAllTblSalaryInfosByGroceriesRightsIsLessThanSomething() throws Exception {
        // Initialize the database
        tblSalaryInfoRepository.saveAndFlush(tblSalaryInfo);

        // Get all the tblSalaryInfoList where groceriesRights is less than DEFAULT_GROCERIES_RIGHTS
        defaultTblSalaryInfoShouldNotBeFound("groceriesRights.lessThan=" + DEFAULT_GROCERIES_RIGHTS);

        // Get all the tblSalaryInfoList where groceriesRights is less than UPDATED_GROCERIES_RIGHTS
        defaultTblSalaryInfoShouldBeFound("groceriesRights.lessThan=" + UPDATED_GROCERIES_RIGHTS);
    }

    @Test
    @Transactional
    void getAllTblSalaryInfosByGroceriesRightsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tblSalaryInfoRepository.saveAndFlush(tblSalaryInfo);

        // Get all the tblSalaryInfoList where groceriesRights is greater than DEFAULT_GROCERIES_RIGHTS
        defaultTblSalaryInfoShouldNotBeFound("groceriesRights.greaterThan=" + DEFAULT_GROCERIES_RIGHTS);

        // Get all the tblSalaryInfoList where groceriesRights is greater than SMALLER_GROCERIES_RIGHTS
        defaultTblSalaryInfoShouldBeFound("groceriesRights.greaterThan=" + SMALLER_GROCERIES_RIGHTS);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTblSalaryInfoShouldBeFound(String filter) throws Exception {
        restTblSalaryInfoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tblSalaryInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].employeeId").value(hasItem(DEFAULT_EMPLOYEE_ID)))
            .andExpect(jsonPath("$.[*].baseSalary").value(hasItem(DEFAULT_BASE_SALARY)))
            .andExpect(jsonPath("$.[*].housingRights").value(hasItem(DEFAULT_HOUSING_RIGHTS)))
            .andExpect(jsonPath("$.[*].internetRights").value(hasItem(DEFAULT_INTERNET_RIGHTS)))
            .andExpect(jsonPath("$.[*].groceriesRights").value(hasItem(DEFAULT_GROCERIES_RIGHTS)));

        // Check, that the count call also returns 1
        restTblSalaryInfoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTblSalaryInfoShouldNotBeFound(String filter) throws Exception {
        restTblSalaryInfoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTblSalaryInfoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTblSalaryInfo() throws Exception {
        // Get the tblSalaryInfo
        restTblSalaryInfoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTblSalaryInfo() throws Exception {
        // Initialize the database
        tblSalaryInfoRepository.saveAndFlush(tblSalaryInfo);

        int databaseSizeBeforeUpdate = tblSalaryInfoRepository.findAll().size();

        // Update the tblSalaryInfo
        TblSalaryInfo updatedTblSalaryInfo = tblSalaryInfoRepository.findById(tblSalaryInfo.getId()).get();
        // Disconnect from session so that the updates on updatedTblSalaryInfo are not directly saved in db
        em.detach(updatedTblSalaryInfo);
        updatedTblSalaryInfo
            .employeeId(UPDATED_EMPLOYEE_ID)
            .baseSalary(UPDATED_BASE_SALARY)
            .housingRights(UPDATED_HOUSING_RIGHTS)
            .internetRights(UPDATED_INTERNET_RIGHTS)
            .groceriesRights(UPDATED_GROCERIES_RIGHTS);

        restTblSalaryInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTblSalaryInfo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTblSalaryInfo))
            )
            .andExpect(status().isOk());

        // Validate the TblSalaryInfo in the database
        List<TblSalaryInfo> tblSalaryInfoList = tblSalaryInfoRepository.findAll();
        assertThat(tblSalaryInfoList).hasSize(databaseSizeBeforeUpdate);
        TblSalaryInfo testTblSalaryInfo = tblSalaryInfoList.get(tblSalaryInfoList.size() - 1);
        assertThat(testTblSalaryInfo.getEmployeeId()).isEqualTo(UPDATED_EMPLOYEE_ID);
        assertThat(testTblSalaryInfo.getBaseSalary()).isEqualTo(UPDATED_BASE_SALARY);
        assertThat(testTblSalaryInfo.getHousingRights()).isEqualTo(UPDATED_HOUSING_RIGHTS);
        assertThat(testTblSalaryInfo.getInternetRights()).isEqualTo(UPDATED_INTERNET_RIGHTS);
        assertThat(testTblSalaryInfo.getGroceriesRights()).isEqualTo(UPDATED_GROCERIES_RIGHTS);
    }

    @Test
    @Transactional
    void putNonExistingTblSalaryInfo() throws Exception {
        int databaseSizeBeforeUpdate = tblSalaryInfoRepository.findAll().size();
        tblSalaryInfo.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTblSalaryInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tblSalaryInfo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tblSalaryInfo))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblSalaryInfo in the database
        List<TblSalaryInfo> tblSalaryInfoList = tblSalaryInfoRepository.findAll();
        assertThat(tblSalaryInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTblSalaryInfo() throws Exception {
        int databaseSizeBeforeUpdate = tblSalaryInfoRepository.findAll().size();
        tblSalaryInfo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblSalaryInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tblSalaryInfo))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblSalaryInfo in the database
        List<TblSalaryInfo> tblSalaryInfoList = tblSalaryInfoRepository.findAll();
        assertThat(tblSalaryInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTblSalaryInfo() throws Exception {
        int databaseSizeBeforeUpdate = tblSalaryInfoRepository.findAll().size();
        tblSalaryInfo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblSalaryInfoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tblSalaryInfo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TblSalaryInfo in the database
        List<TblSalaryInfo> tblSalaryInfoList = tblSalaryInfoRepository.findAll();
        assertThat(tblSalaryInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTblSalaryInfoWithPatch() throws Exception {
        // Initialize the database
        tblSalaryInfoRepository.saveAndFlush(tblSalaryInfo);

        int databaseSizeBeforeUpdate = tblSalaryInfoRepository.findAll().size();

        // Update the tblSalaryInfo using partial update
        TblSalaryInfo partialUpdatedTblSalaryInfo = new TblSalaryInfo();
        partialUpdatedTblSalaryInfo.setId(tblSalaryInfo.getId());

        partialUpdatedTblSalaryInfo
            .employeeId(UPDATED_EMPLOYEE_ID)
            .baseSalary(UPDATED_BASE_SALARY)
            .groceriesRights(UPDATED_GROCERIES_RIGHTS);

        restTblSalaryInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTblSalaryInfo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTblSalaryInfo))
            )
            .andExpect(status().isOk());

        // Validate the TblSalaryInfo in the database
        List<TblSalaryInfo> tblSalaryInfoList = tblSalaryInfoRepository.findAll();
        assertThat(tblSalaryInfoList).hasSize(databaseSizeBeforeUpdate);
        TblSalaryInfo testTblSalaryInfo = tblSalaryInfoList.get(tblSalaryInfoList.size() - 1);
        assertThat(testTblSalaryInfo.getEmployeeId()).isEqualTo(UPDATED_EMPLOYEE_ID);
        assertThat(testTblSalaryInfo.getBaseSalary()).isEqualTo(UPDATED_BASE_SALARY);
        assertThat(testTblSalaryInfo.getHousingRights()).isEqualTo(DEFAULT_HOUSING_RIGHTS);
        assertThat(testTblSalaryInfo.getInternetRights()).isEqualTo(DEFAULT_INTERNET_RIGHTS);
        assertThat(testTblSalaryInfo.getGroceriesRights()).isEqualTo(UPDATED_GROCERIES_RIGHTS);
    }

    @Test
    @Transactional
    void fullUpdateTblSalaryInfoWithPatch() throws Exception {
        // Initialize the database
        tblSalaryInfoRepository.saveAndFlush(tblSalaryInfo);

        int databaseSizeBeforeUpdate = tblSalaryInfoRepository.findAll().size();

        // Update the tblSalaryInfo using partial update
        TblSalaryInfo partialUpdatedTblSalaryInfo = new TblSalaryInfo();
        partialUpdatedTblSalaryInfo.setId(tblSalaryInfo.getId());

        partialUpdatedTblSalaryInfo
            .employeeId(UPDATED_EMPLOYEE_ID)
            .baseSalary(UPDATED_BASE_SALARY)
            .housingRights(UPDATED_HOUSING_RIGHTS)
            .internetRights(UPDATED_INTERNET_RIGHTS)
            .groceriesRights(UPDATED_GROCERIES_RIGHTS);

        restTblSalaryInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTblSalaryInfo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTblSalaryInfo))
            )
            .andExpect(status().isOk());

        // Validate the TblSalaryInfo in the database
        List<TblSalaryInfo> tblSalaryInfoList = tblSalaryInfoRepository.findAll();
        assertThat(tblSalaryInfoList).hasSize(databaseSizeBeforeUpdate);
        TblSalaryInfo testTblSalaryInfo = tblSalaryInfoList.get(tblSalaryInfoList.size() - 1);
        assertThat(testTblSalaryInfo.getEmployeeId()).isEqualTo(UPDATED_EMPLOYEE_ID);
        assertThat(testTblSalaryInfo.getBaseSalary()).isEqualTo(UPDATED_BASE_SALARY);
        assertThat(testTblSalaryInfo.getHousingRights()).isEqualTo(UPDATED_HOUSING_RIGHTS);
        assertThat(testTblSalaryInfo.getInternetRights()).isEqualTo(UPDATED_INTERNET_RIGHTS);
        assertThat(testTblSalaryInfo.getGroceriesRights()).isEqualTo(UPDATED_GROCERIES_RIGHTS);
    }

    @Test
    @Transactional
    void patchNonExistingTblSalaryInfo() throws Exception {
        int databaseSizeBeforeUpdate = tblSalaryInfoRepository.findAll().size();
        tblSalaryInfo.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTblSalaryInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tblSalaryInfo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tblSalaryInfo))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblSalaryInfo in the database
        List<TblSalaryInfo> tblSalaryInfoList = tblSalaryInfoRepository.findAll();
        assertThat(tblSalaryInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTblSalaryInfo() throws Exception {
        int databaseSizeBeforeUpdate = tblSalaryInfoRepository.findAll().size();
        tblSalaryInfo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblSalaryInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tblSalaryInfo))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblSalaryInfo in the database
        List<TblSalaryInfo> tblSalaryInfoList = tblSalaryInfoRepository.findAll();
        assertThat(tblSalaryInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTblSalaryInfo() throws Exception {
        int databaseSizeBeforeUpdate = tblSalaryInfoRepository.findAll().size();
        tblSalaryInfo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblSalaryInfoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tblSalaryInfo))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TblSalaryInfo in the database
        List<TblSalaryInfo> tblSalaryInfoList = tblSalaryInfoRepository.findAll();
        assertThat(tblSalaryInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTblSalaryInfo() throws Exception {
        // Initialize the database
        tblSalaryInfoRepository.saveAndFlush(tblSalaryInfo);

        int databaseSizeBeforeDelete = tblSalaryInfoRepository.findAll().size();

        // Delete the tblSalaryInfo
        restTblSalaryInfoMockMvc
            .perform(delete(ENTITY_API_URL_ID, tblSalaryInfo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TblSalaryInfo> tblSalaryInfoList = tblSalaryInfoRepository.findAll();
        assertThat(tblSalaryInfoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
