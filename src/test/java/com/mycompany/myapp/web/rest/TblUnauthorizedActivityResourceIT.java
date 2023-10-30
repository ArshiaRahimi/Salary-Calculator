package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.TblUnauthorizedActivity;
import com.mycompany.myapp.repository.TblUnauthorizedActivityRepository;
import com.mycompany.myapp.service.criteria.TblUnauthorizedActivityCriteria;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link TblUnauthorizedActivityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TblUnauthorizedActivityResourceIT {

    private static final String DEFAULT_RFID_ID = "AAAAAAAAAA";
    private static final String UPDATED_RFID_ID = "BBBBBBBBBB";

    private static final Integer DEFAULT_EMPLOYEE_ID = 1;
    private static final Integer UPDATED_EMPLOYEE_ID = 2;
    private static final Integer SMALLER_EMPLOYEE_ID = 1 - 1;

    private static final ZonedDateTime DEFAULT_READING_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_READING_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_READING_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Integer DEFAULT_ATTEMPT = 1;
    private static final Integer UPDATED_ATTEMPT = 2;
    private static final Integer SMALLER_ATTEMPT = 1 - 1;

    private static final String DEFAULT_FINGERPRINT = "AAAAAAAAAA";
    private static final String UPDATED_FINGERPRINT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tbl-unauthorized-activities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TblUnauthorizedActivityRepository tblUnauthorizedActivityRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTblUnauthorizedActivityMockMvc;

    private TblUnauthorizedActivity tblUnauthorizedActivity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TblUnauthorizedActivity createEntity(EntityManager em) {
        TblUnauthorizedActivity tblUnauthorizedActivity = new TblUnauthorizedActivity()
            .rfidId(DEFAULT_RFID_ID)
            .employeeId(DEFAULT_EMPLOYEE_ID)
            .readingTime(DEFAULT_READING_TIME)
            .attempt(DEFAULT_ATTEMPT)
            .fingerprint(DEFAULT_FINGERPRINT);
        return tblUnauthorizedActivity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TblUnauthorizedActivity createUpdatedEntity(EntityManager em) {
        TblUnauthorizedActivity tblUnauthorizedActivity = new TblUnauthorizedActivity()
            .rfidId(UPDATED_RFID_ID)
            .employeeId(UPDATED_EMPLOYEE_ID)
            .readingTime(UPDATED_READING_TIME)
            .attempt(UPDATED_ATTEMPT)
            .fingerprint(UPDATED_FINGERPRINT);
        return tblUnauthorizedActivity;
    }

    @BeforeEach
    public void initTest() {
        tblUnauthorizedActivity = createEntity(em);
    }

    @Test
    @Transactional
    void createTblUnauthorizedActivity() throws Exception {
        int databaseSizeBeforeCreate = tblUnauthorizedActivityRepository.findAll().size();
        // Create the TblUnauthorizedActivity
        restTblUnauthorizedActivityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tblUnauthorizedActivity))
            )
            .andExpect(status().isCreated());

        // Validate the TblUnauthorizedActivity in the database
        List<TblUnauthorizedActivity> tblUnauthorizedActivityList = tblUnauthorizedActivityRepository.findAll();
        assertThat(tblUnauthorizedActivityList).hasSize(databaseSizeBeforeCreate + 1);
        TblUnauthorizedActivity testTblUnauthorizedActivity = tblUnauthorizedActivityList.get(tblUnauthorizedActivityList.size() - 1);
        assertThat(testTblUnauthorizedActivity.getRfidId()).isEqualTo(DEFAULT_RFID_ID);
        assertThat(testTblUnauthorizedActivity.getEmployeeId()).isEqualTo(DEFAULT_EMPLOYEE_ID);
        assertThat(testTblUnauthorizedActivity.getReadingTime()).isEqualTo(DEFAULT_READING_TIME);
        assertThat(testTblUnauthorizedActivity.getAttempt()).isEqualTo(DEFAULT_ATTEMPT);
        assertThat(testTblUnauthorizedActivity.getFingerprint()).isEqualTo(DEFAULT_FINGERPRINT);
    }

    @Test
    @Transactional
    void createTblUnauthorizedActivityWithExistingId() throws Exception {
        // Create the TblUnauthorizedActivity with an existing ID
        tblUnauthorizedActivity.setId(1L);

        int databaseSizeBeforeCreate = tblUnauthorizedActivityRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTblUnauthorizedActivityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tblUnauthorizedActivity))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblUnauthorizedActivity in the database
        List<TblUnauthorizedActivity> tblUnauthorizedActivityList = tblUnauthorizedActivityRepository.findAll();
        assertThat(tblUnauthorizedActivityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTblUnauthorizedActivities() throws Exception {
        // Initialize the database
        tblUnauthorizedActivityRepository.saveAndFlush(tblUnauthorizedActivity);

        // Get all the tblUnauthorizedActivityList
        restTblUnauthorizedActivityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tblUnauthorizedActivity.getId().intValue())))
            .andExpect(jsonPath("$.[*].rfidId").value(hasItem(DEFAULT_RFID_ID)))
            .andExpect(jsonPath("$.[*].employeeId").value(hasItem(DEFAULT_EMPLOYEE_ID)))
            .andExpect(jsonPath("$.[*].readingTime").value(hasItem(sameInstant(DEFAULT_READING_TIME))))
            .andExpect(jsonPath("$.[*].attempt").value(hasItem(DEFAULT_ATTEMPT)))
            .andExpect(jsonPath("$.[*].fingerprint").value(hasItem(DEFAULT_FINGERPRINT.toString())));
    }

    @Test
    @Transactional
    void getTblUnauthorizedActivity() throws Exception {
        // Initialize the database
        tblUnauthorizedActivityRepository.saveAndFlush(tblUnauthorizedActivity);

        // Get the tblUnauthorizedActivity
        restTblUnauthorizedActivityMockMvc
            .perform(get(ENTITY_API_URL_ID, tblUnauthorizedActivity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tblUnauthorizedActivity.getId().intValue()))
            .andExpect(jsonPath("$.rfidId").value(DEFAULT_RFID_ID))
            .andExpect(jsonPath("$.employeeId").value(DEFAULT_EMPLOYEE_ID))
            .andExpect(jsonPath("$.readingTime").value(sameInstant(DEFAULT_READING_TIME)))
            .andExpect(jsonPath("$.attempt").value(DEFAULT_ATTEMPT))
            .andExpect(jsonPath("$.fingerprint").value(DEFAULT_FINGERPRINT.toString()));
    }

    @Test
    @Transactional
    void getTblUnauthorizedActivitiesByIdFiltering() throws Exception {
        // Initialize the database
        tblUnauthorizedActivityRepository.saveAndFlush(tblUnauthorizedActivity);

        Long id = tblUnauthorizedActivity.getId();

        defaultTblUnauthorizedActivityShouldBeFound("id.equals=" + id);
        defaultTblUnauthorizedActivityShouldNotBeFound("id.notEquals=" + id);

        defaultTblUnauthorizedActivityShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTblUnauthorizedActivityShouldNotBeFound("id.greaterThan=" + id);

        defaultTblUnauthorizedActivityShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTblUnauthorizedActivityShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTblUnauthorizedActivitiesByRfidIdIsEqualToSomething() throws Exception {
        // Initialize the database
        tblUnauthorizedActivityRepository.saveAndFlush(tblUnauthorizedActivity);

        // Get all the tblUnauthorizedActivityList where rfidId equals to DEFAULT_RFID_ID
        defaultTblUnauthorizedActivityShouldBeFound("rfidId.equals=" + DEFAULT_RFID_ID);

        // Get all the tblUnauthorizedActivityList where rfidId equals to UPDATED_RFID_ID
        defaultTblUnauthorizedActivityShouldNotBeFound("rfidId.equals=" + UPDATED_RFID_ID);
    }

    @Test
    @Transactional
    void getAllTblUnauthorizedActivitiesByRfidIdIsInShouldWork() throws Exception {
        // Initialize the database
        tblUnauthorizedActivityRepository.saveAndFlush(tblUnauthorizedActivity);

        // Get all the tblUnauthorizedActivityList where rfidId in DEFAULT_RFID_ID or UPDATED_RFID_ID
        defaultTblUnauthorizedActivityShouldBeFound("rfidId.in=" + DEFAULT_RFID_ID + "," + UPDATED_RFID_ID);

        // Get all the tblUnauthorizedActivityList where rfidId equals to UPDATED_RFID_ID
        defaultTblUnauthorizedActivityShouldNotBeFound("rfidId.in=" + UPDATED_RFID_ID);
    }

    @Test
    @Transactional
    void getAllTblUnauthorizedActivitiesByRfidIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblUnauthorizedActivityRepository.saveAndFlush(tblUnauthorizedActivity);

        // Get all the tblUnauthorizedActivityList where rfidId is not null
        defaultTblUnauthorizedActivityShouldBeFound("rfidId.specified=true");

        // Get all the tblUnauthorizedActivityList where rfidId is null
        defaultTblUnauthorizedActivityShouldNotBeFound("rfidId.specified=false");
    }

    @Test
    @Transactional
    void getAllTblUnauthorizedActivitiesByRfidIdContainsSomething() throws Exception {
        // Initialize the database
        tblUnauthorizedActivityRepository.saveAndFlush(tblUnauthorizedActivity);

        // Get all the tblUnauthorizedActivityList where rfidId contains DEFAULT_RFID_ID
        defaultTblUnauthorizedActivityShouldBeFound("rfidId.contains=" + DEFAULT_RFID_ID);

        // Get all the tblUnauthorizedActivityList where rfidId contains UPDATED_RFID_ID
        defaultTblUnauthorizedActivityShouldNotBeFound("rfidId.contains=" + UPDATED_RFID_ID);
    }

    @Test
    @Transactional
    void getAllTblUnauthorizedActivitiesByRfidIdNotContainsSomething() throws Exception {
        // Initialize the database
        tblUnauthorizedActivityRepository.saveAndFlush(tblUnauthorizedActivity);

        // Get all the tblUnauthorizedActivityList where rfidId does not contain DEFAULT_RFID_ID
        defaultTblUnauthorizedActivityShouldNotBeFound("rfidId.doesNotContain=" + DEFAULT_RFID_ID);

        // Get all the tblUnauthorizedActivityList where rfidId does not contain UPDATED_RFID_ID
        defaultTblUnauthorizedActivityShouldBeFound("rfidId.doesNotContain=" + UPDATED_RFID_ID);
    }

    @Test
    @Transactional
    void getAllTblUnauthorizedActivitiesByEmployeeIdIsEqualToSomething() throws Exception {
        // Initialize the database
        tblUnauthorizedActivityRepository.saveAndFlush(tblUnauthorizedActivity);

        // Get all the tblUnauthorizedActivityList where employeeId equals to DEFAULT_EMPLOYEE_ID
        defaultTblUnauthorizedActivityShouldBeFound("employeeId.equals=" + DEFAULT_EMPLOYEE_ID);

        // Get all the tblUnauthorizedActivityList where employeeId equals to UPDATED_EMPLOYEE_ID
        defaultTblUnauthorizedActivityShouldNotBeFound("employeeId.equals=" + UPDATED_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllTblUnauthorizedActivitiesByEmployeeIdIsInShouldWork() throws Exception {
        // Initialize the database
        tblUnauthorizedActivityRepository.saveAndFlush(tblUnauthorizedActivity);

        // Get all the tblUnauthorizedActivityList where employeeId in DEFAULT_EMPLOYEE_ID or UPDATED_EMPLOYEE_ID
        defaultTblUnauthorizedActivityShouldBeFound("employeeId.in=" + DEFAULT_EMPLOYEE_ID + "," + UPDATED_EMPLOYEE_ID);

        // Get all the tblUnauthorizedActivityList where employeeId equals to UPDATED_EMPLOYEE_ID
        defaultTblUnauthorizedActivityShouldNotBeFound("employeeId.in=" + UPDATED_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllTblUnauthorizedActivitiesByEmployeeIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblUnauthorizedActivityRepository.saveAndFlush(tblUnauthorizedActivity);

        // Get all the tblUnauthorizedActivityList where employeeId is not null
        defaultTblUnauthorizedActivityShouldBeFound("employeeId.specified=true");

        // Get all the tblUnauthorizedActivityList where employeeId is null
        defaultTblUnauthorizedActivityShouldNotBeFound("employeeId.specified=false");
    }

    @Test
    @Transactional
    void getAllTblUnauthorizedActivitiesByEmployeeIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tblUnauthorizedActivityRepository.saveAndFlush(tblUnauthorizedActivity);

        // Get all the tblUnauthorizedActivityList where employeeId is greater than or equal to DEFAULT_EMPLOYEE_ID
        defaultTblUnauthorizedActivityShouldBeFound("employeeId.greaterThanOrEqual=" + DEFAULT_EMPLOYEE_ID);

        // Get all the tblUnauthorizedActivityList where employeeId is greater than or equal to UPDATED_EMPLOYEE_ID
        defaultTblUnauthorizedActivityShouldNotBeFound("employeeId.greaterThanOrEqual=" + UPDATED_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllTblUnauthorizedActivitiesByEmployeeIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tblUnauthorizedActivityRepository.saveAndFlush(tblUnauthorizedActivity);

        // Get all the tblUnauthorizedActivityList where employeeId is less than or equal to DEFAULT_EMPLOYEE_ID
        defaultTblUnauthorizedActivityShouldBeFound("employeeId.lessThanOrEqual=" + DEFAULT_EMPLOYEE_ID);

        // Get all the tblUnauthorizedActivityList where employeeId is less than or equal to SMALLER_EMPLOYEE_ID
        defaultTblUnauthorizedActivityShouldNotBeFound("employeeId.lessThanOrEqual=" + SMALLER_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllTblUnauthorizedActivitiesByEmployeeIdIsLessThanSomething() throws Exception {
        // Initialize the database
        tblUnauthorizedActivityRepository.saveAndFlush(tblUnauthorizedActivity);

        // Get all the tblUnauthorizedActivityList where employeeId is less than DEFAULT_EMPLOYEE_ID
        defaultTblUnauthorizedActivityShouldNotBeFound("employeeId.lessThan=" + DEFAULT_EMPLOYEE_ID);

        // Get all the tblUnauthorizedActivityList where employeeId is less than UPDATED_EMPLOYEE_ID
        defaultTblUnauthorizedActivityShouldBeFound("employeeId.lessThan=" + UPDATED_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllTblUnauthorizedActivitiesByEmployeeIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tblUnauthorizedActivityRepository.saveAndFlush(tblUnauthorizedActivity);

        // Get all the tblUnauthorizedActivityList where employeeId is greater than DEFAULT_EMPLOYEE_ID
        defaultTblUnauthorizedActivityShouldNotBeFound("employeeId.greaterThan=" + DEFAULT_EMPLOYEE_ID);

        // Get all the tblUnauthorizedActivityList where employeeId is greater than SMALLER_EMPLOYEE_ID
        defaultTblUnauthorizedActivityShouldBeFound("employeeId.greaterThan=" + SMALLER_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllTblUnauthorizedActivitiesByReadingTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        tblUnauthorizedActivityRepository.saveAndFlush(tblUnauthorizedActivity);

        // Get all the tblUnauthorizedActivityList where readingTime equals to DEFAULT_READING_TIME
        defaultTblUnauthorizedActivityShouldBeFound("readingTime.equals=" + DEFAULT_READING_TIME);

        // Get all the tblUnauthorizedActivityList where readingTime equals to UPDATED_READING_TIME
        defaultTblUnauthorizedActivityShouldNotBeFound("readingTime.equals=" + UPDATED_READING_TIME);
    }

    @Test
    @Transactional
    void getAllTblUnauthorizedActivitiesByReadingTimeIsInShouldWork() throws Exception {
        // Initialize the database
        tblUnauthorizedActivityRepository.saveAndFlush(tblUnauthorizedActivity);

        // Get all the tblUnauthorizedActivityList where readingTime in DEFAULT_READING_TIME or UPDATED_READING_TIME
        defaultTblUnauthorizedActivityShouldBeFound("readingTime.in=" + DEFAULT_READING_TIME + "," + UPDATED_READING_TIME);

        // Get all the tblUnauthorizedActivityList where readingTime equals to UPDATED_READING_TIME
        defaultTblUnauthorizedActivityShouldNotBeFound("readingTime.in=" + UPDATED_READING_TIME);
    }

    @Test
    @Transactional
    void getAllTblUnauthorizedActivitiesByReadingTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblUnauthorizedActivityRepository.saveAndFlush(tblUnauthorizedActivity);

        // Get all the tblUnauthorizedActivityList where readingTime is not null
        defaultTblUnauthorizedActivityShouldBeFound("readingTime.specified=true");

        // Get all the tblUnauthorizedActivityList where readingTime is null
        defaultTblUnauthorizedActivityShouldNotBeFound("readingTime.specified=false");
    }

    @Test
    @Transactional
    void getAllTblUnauthorizedActivitiesByReadingTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tblUnauthorizedActivityRepository.saveAndFlush(tblUnauthorizedActivity);

        // Get all the tblUnauthorizedActivityList where readingTime is greater than or equal to DEFAULT_READING_TIME
        defaultTblUnauthorizedActivityShouldBeFound("readingTime.greaterThanOrEqual=" + DEFAULT_READING_TIME);

        // Get all the tblUnauthorizedActivityList where readingTime is greater than or equal to UPDATED_READING_TIME
        defaultTblUnauthorizedActivityShouldNotBeFound("readingTime.greaterThanOrEqual=" + UPDATED_READING_TIME);
    }

    @Test
    @Transactional
    void getAllTblUnauthorizedActivitiesByReadingTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tblUnauthorizedActivityRepository.saveAndFlush(tblUnauthorizedActivity);

        // Get all the tblUnauthorizedActivityList where readingTime is less than or equal to DEFAULT_READING_TIME
        defaultTblUnauthorizedActivityShouldBeFound("readingTime.lessThanOrEqual=" + DEFAULT_READING_TIME);

        // Get all the tblUnauthorizedActivityList where readingTime is less than or equal to SMALLER_READING_TIME
        defaultTblUnauthorizedActivityShouldNotBeFound("readingTime.lessThanOrEqual=" + SMALLER_READING_TIME);
    }

    @Test
    @Transactional
    void getAllTblUnauthorizedActivitiesByReadingTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        tblUnauthorizedActivityRepository.saveAndFlush(tblUnauthorizedActivity);

        // Get all the tblUnauthorizedActivityList where readingTime is less than DEFAULT_READING_TIME
        defaultTblUnauthorizedActivityShouldNotBeFound("readingTime.lessThan=" + DEFAULT_READING_TIME);

        // Get all the tblUnauthorizedActivityList where readingTime is less than UPDATED_READING_TIME
        defaultTblUnauthorizedActivityShouldBeFound("readingTime.lessThan=" + UPDATED_READING_TIME);
    }

    @Test
    @Transactional
    void getAllTblUnauthorizedActivitiesByReadingTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tblUnauthorizedActivityRepository.saveAndFlush(tblUnauthorizedActivity);

        // Get all the tblUnauthorizedActivityList where readingTime is greater than DEFAULT_READING_TIME
        defaultTblUnauthorizedActivityShouldNotBeFound("readingTime.greaterThan=" + DEFAULT_READING_TIME);

        // Get all the tblUnauthorizedActivityList where readingTime is greater than SMALLER_READING_TIME
        defaultTblUnauthorizedActivityShouldBeFound("readingTime.greaterThan=" + SMALLER_READING_TIME);
    }

    @Test
    @Transactional
    void getAllTblUnauthorizedActivitiesByAttemptIsEqualToSomething() throws Exception {
        // Initialize the database
        tblUnauthorizedActivityRepository.saveAndFlush(tblUnauthorizedActivity);

        // Get all the tblUnauthorizedActivityList where attempt equals to DEFAULT_ATTEMPT
        defaultTblUnauthorizedActivityShouldBeFound("attempt.equals=" + DEFAULT_ATTEMPT);

        // Get all the tblUnauthorizedActivityList where attempt equals to UPDATED_ATTEMPT
        defaultTblUnauthorizedActivityShouldNotBeFound("attempt.equals=" + UPDATED_ATTEMPT);
    }

    @Test
    @Transactional
    void getAllTblUnauthorizedActivitiesByAttemptIsInShouldWork() throws Exception {
        // Initialize the database
        tblUnauthorizedActivityRepository.saveAndFlush(tblUnauthorizedActivity);

        // Get all the tblUnauthorizedActivityList where attempt in DEFAULT_ATTEMPT or UPDATED_ATTEMPT
        defaultTblUnauthorizedActivityShouldBeFound("attempt.in=" + DEFAULT_ATTEMPT + "," + UPDATED_ATTEMPT);

        // Get all the tblUnauthorizedActivityList where attempt equals to UPDATED_ATTEMPT
        defaultTblUnauthorizedActivityShouldNotBeFound("attempt.in=" + UPDATED_ATTEMPT);
    }

    @Test
    @Transactional
    void getAllTblUnauthorizedActivitiesByAttemptIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblUnauthorizedActivityRepository.saveAndFlush(tblUnauthorizedActivity);

        // Get all the tblUnauthorizedActivityList where attempt is not null
        defaultTblUnauthorizedActivityShouldBeFound("attempt.specified=true");

        // Get all the tblUnauthorizedActivityList where attempt is null
        defaultTblUnauthorizedActivityShouldNotBeFound("attempt.specified=false");
    }

    @Test
    @Transactional
    void getAllTblUnauthorizedActivitiesByAttemptIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tblUnauthorizedActivityRepository.saveAndFlush(tblUnauthorizedActivity);

        // Get all the tblUnauthorizedActivityList where attempt is greater than or equal to DEFAULT_ATTEMPT
        defaultTblUnauthorizedActivityShouldBeFound("attempt.greaterThanOrEqual=" + DEFAULT_ATTEMPT);

        // Get all the tblUnauthorizedActivityList where attempt is greater than or equal to UPDATED_ATTEMPT
        defaultTblUnauthorizedActivityShouldNotBeFound("attempt.greaterThanOrEqual=" + UPDATED_ATTEMPT);
    }

    @Test
    @Transactional
    void getAllTblUnauthorizedActivitiesByAttemptIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tblUnauthorizedActivityRepository.saveAndFlush(tblUnauthorizedActivity);

        // Get all the tblUnauthorizedActivityList where attempt is less than or equal to DEFAULT_ATTEMPT
        defaultTblUnauthorizedActivityShouldBeFound("attempt.lessThanOrEqual=" + DEFAULT_ATTEMPT);

        // Get all the tblUnauthorizedActivityList where attempt is less than or equal to SMALLER_ATTEMPT
        defaultTblUnauthorizedActivityShouldNotBeFound("attempt.lessThanOrEqual=" + SMALLER_ATTEMPT);
    }

    @Test
    @Transactional
    void getAllTblUnauthorizedActivitiesByAttemptIsLessThanSomething() throws Exception {
        // Initialize the database
        tblUnauthorizedActivityRepository.saveAndFlush(tblUnauthorizedActivity);

        // Get all the tblUnauthorizedActivityList where attempt is less than DEFAULT_ATTEMPT
        defaultTblUnauthorizedActivityShouldNotBeFound("attempt.lessThan=" + DEFAULT_ATTEMPT);

        // Get all the tblUnauthorizedActivityList where attempt is less than UPDATED_ATTEMPT
        defaultTblUnauthorizedActivityShouldBeFound("attempt.lessThan=" + UPDATED_ATTEMPT);
    }

    @Test
    @Transactional
    void getAllTblUnauthorizedActivitiesByAttemptIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tblUnauthorizedActivityRepository.saveAndFlush(tblUnauthorizedActivity);

        // Get all the tblUnauthorizedActivityList where attempt is greater than DEFAULT_ATTEMPT
        defaultTblUnauthorizedActivityShouldNotBeFound("attempt.greaterThan=" + DEFAULT_ATTEMPT);

        // Get all the tblUnauthorizedActivityList where attempt is greater than SMALLER_ATTEMPT
        defaultTblUnauthorizedActivityShouldBeFound("attempt.greaterThan=" + SMALLER_ATTEMPT);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTblUnauthorizedActivityShouldBeFound(String filter) throws Exception {
        restTblUnauthorizedActivityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tblUnauthorizedActivity.getId().intValue())))
            .andExpect(jsonPath("$.[*].rfidId").value(hasItem(DEFAULT_RFID_ID)))
            .andExpect(jsonPath("$.[*].employeeId").value(hasItem(DEFAULT_EMPLOYEE_ID)))
            .andExpect(jsonPath("$.[*].readingTime").value(hasItem(sameInstant(DEFAULT_READING_TIME))))
            .andExpect(jsonPath("$.[*].attempt").value(hasItem(DEFAULT_ATTEMPT)))
            .andExpect(jsonPath("$.[*].fingerprint").value(hasItem(DEFAULT_FINGERPRINT.toString())));

        // Check, that the count call also returns 1
        restTblUnauthorizedActivityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTblUnauthorizedActivityShouldNotBeFound(String filter) throws Exception {
        restTblUnauthorizedActivityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTblUnauthorizedActivityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTblUnauthorizedActivity() throws Exception {
        // Get the tblUnauthorizedActivity
        restTblUnauthorizedActivityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTblUnauthorizedActivity() throws Exception {
        // Initialize the database
        tblUnauthorizedActivityRepository.saveAndFlush(tblUnauthorizedActivity);

        int databaseSizeBeforeUpdate = tblUnauthorizedActivityRepository.findAll().size();

        // Update the tblUnauthorizedActivity
        TblUnauthorizedActivity updatedTblUnauthorizedActivity = tblUnauthorizedActivityRepository
            .findById(tblUnauthorizedActivity.getId())
            .get();
        // Disconnect from session so that the updates on updatedTblUnauthorizedActivity are not directly saved in db
        em.detach(updatedTblUnauthorizedActivity);
        updatedTblUnauthorizedActivity
            .rfidId(UPDATED_RFID_ID)
            .employeeId(UPDATED_EMPLOYEE_ID)
            .readingTime(UPDATED_READING_TIME)
            .attempt(UPDATED_ATTEMPT)
            .fingerprint(UPDATED_FINGERPRINT);

        restTblUnauthorizedActivityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTblUnauthorizedActivity.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTblUnauthorizedActivity))
            )
            .andExpect(status().isOk());

        // Validate the TblUnauthorizedActivity in the database
        List<TblUnauthorizedActivity> tblUnauthorizedActivityList = tblUnauthorizedActivityRepository.findAll();
        assertThat(tblUnauthorizedActivityList).hasSize(databaseSizeBeforeUpdate);
        TblUnauthorizedActivity testTblUnauthorizedActivity = tblUnauthorizedActivityList.get(tblUnauthorizedActivityList.size() - 1);
        assertThat(testTblUnauthorizedActivity.getRfidId()).isEqualTo(UPDATED_RFID_ID);
        assertThat(testTblUnauthorizedActivity.getEmployeeId()).isEqualTo(UPDATED_EMPLOYEE_ID);
        assertThat(testTblUnauthorizedActivity.getReadingTime()).isEqualTo(UPDATED_READING_TIME);
        assertThat(testTblUnauthorizedActivity.getAttempt()).isEqualTo(UPDATED_ATTEMPT);
        assertThat(testTblUnauthorizedActivity.getFingerprint()).isEqualTo(UPDATED_FINGERPRINT);
    }

    @Test
    @Transactional
    void putNonExistingTblUnauthorizedActivity() throws Exception {
        int databaseSizeBeforeUpdate = tblUnauthorizedActivityRepository.findAll().size();
        tblUnauthorizedActivity.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTblUnauthorizedActivityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tblUnauthorizedActivity.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tblUnauthorizedActivity))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblUnauthorizedActivity in the database
        List<TblUnauthorizedActivity> tblUnauthorizedActivityList = tblUnauthorizedActivityRepository.findAll();
        assertThat(tblUnauthorizedActivityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTblUnauthorizedActivity() throws Exception {
        int databaseSizeBeforeUpdate = tblUnauthorizedActivityRepository.findAll().size();
        tblUnauthorizedActivity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblUnauthorizedActivityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tblUnauthorizedActivity))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblUnauthorizedActivity in the database
        List<TblUnauthorizedActivity> tblUnauthorizedActivityList = tblUnauthorizedActivityRepository.findAll();
        assertThat(tblUnauthorizedActivityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTblUnauthorizedActivity() throws Exception {
        int databaseSizeBeforeUpdate = tblUnauthorizedActivityRepository.findAll().size();
        tblUnauthorizedActivity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblUnauthorizedActivityMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tblUnauthorizedActivity))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TblUnauthorizedActivity in the database
        List<TblUnauthorizedActivity> tblUnauthorizedActivityList = tblUnauthorizedActivityRepository.findAll();
        assertThat(tblUnauthorizedActivityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTblUnauthorizedActivityWithPatch() throws Exception {
        // Initialize the database
        tblUnauthorizedActivityRepository.saveAndFlush(tblUnauthorizedActivity);

        int databaseSizeBeforeUpdate = tblUnauthorizedActivityRepository.findAll().size();

        // Update the tblUnauthorizedActivity using partial update
        TblUnauthorizedActivity partialUpdatedTblUnauthorizedActivity = new TblUnauthorizedActivity();
        partialUpdatedTblUnauthorizedActivity.setId(tblUnauthorizedActivity.getId());

        partialUpdatedTblUnauthorizedActivity
            .rfidId(UPDATED_RFID_ID)
            .employeeId(UPDATED_EMPLOYEE_ID)
            .readingTime(UPDATED_READING_TIME)
            .fingerprint(UPDATED_FINGERPRINT);

        restTblUnauthorizedActivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTblUnauthorizedActivity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTblUnauthorizedActivity))
            )
            .andExpect(status().isOk());

        // Validate the TblUnauthorizedActivity in the database
        List<TblUnauthorizedActivity> tblUnauthorizedActivityList = tblUnauthorizedActivityRepository.findAll();
        assertThat(tblUnauthorizedActivityList).hasSize(databaseSizeBeforeUpdate);
        TblUnauthorizedActivity testTblUnauthorizedActivity = tblUnauthorizedActivityList.get(tblUnauthorizedActivityList.size() - 1);
        assertThat(testTblUnauthorizedActivity.getRfidId()).isEqualTo(UPDATED_RFID_ID);
        assertThat(testTblUnauthorizedActivity.getEmployeeId()).isEqualTo(UPDATED_EMPLOYEE_ID);
        assertThat(testTblUnauthorizedActivity.getReadingTime()).isEqualTo(UPDATED_READING_TIME);
        assertThat(testTblUnauthorizedActivity.getAttempt()).isEqualTo(DEFAULT_ATTEMPT);
        assertThat(testTblUnauthorizedActivity.getFingerprint()).isEqualTo(UPDATED_FINGERPRINT);
    }

    @Test
    @Transactional
    void fullUpdateTblUnauthorizedActivityWithPatch() throws Exception {
        // Initialize the database
        tblUnauthorizedActivityRepository.saveAndFlush(tblUnauthorizedActivity);

        int databaseSizeBeforeUpdate = tblUnauthorizedActivityRepository.findAll().size();

        // Update the tblUnauthorizedActivity using partial update
        TblUnauthorizedActivity partialUpdatedTblUnauthorizedActivity = new TblUnauthorizedActivity();
        partialUpdatedTblUnauthorizedActivity.setId(tblUnauthorizedActivity.getId());

        partialUpdatedTblUnauthorizedActivity
            .rfidId(UPDATED_RFID_ID)
            .employeeId(UPDATED_EMPLOYEE_ID)
            .readingTime(UPDATED_READING_TIME)
            .attempt(UPDATED_ATTEMPT)
            .fingerprint(UPDATED_FINGERPRINT);

        restTblUnauthorizedActivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTblUnauthorizedActivity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTblUnauthorizedActivity))
            )
            .andExpect(status().isOk());

        // Validate the TblUnauthorizedActivity in the database
        List<TblUnauthorizedActivity> tblUnauthorizedActivityList = tblUnauthorizedActivityRepository.findAll();
        assertThat(tblUnauthorizedActivityList).hasSize(databaseSizeBeforeUpdate);
        TblUnauthorizedActivity testTblUnauthorizedActivity = tblUnauthorizedActivityList.get(tblUnauthorizedActivityList.size() - 1);
        assertThat(testTblUnauthorizedActivity.getRfidId()).isEqualTo(UPDATED_RFID_ID);
        assertThat(testTblUnauthorizedActivity.getEmployeeId()).isEqualTo(UPDATED_EMPLOYEE_ID);
        assertThat(testTblUnauthorizedActivity.getReadingTime()).isEqualTo(UPDATED_READING_TIME);
        assertThat(testTblUnauthorizedActivity.getAttempt()).isEqualTo(UPDATED_ATTEMPT);
        assertThat(testTblUnauthorizedActivity.getFingerprint()).isEqualTo(UPDATED_FINGERPRINT);
    }

    @Test
    @Transactional
    void patchNonExistingTblUnauthorizedActivity() throws Exception {
        int databaseSizeBeforeUpdate = tblUnauthorizedActivityRepository.findAll().size();
        tblUnauthorizedActivity.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTblUnauthorizedActivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tblUnauthorizedActivity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tblUnauthorizedActivity))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblUnauthorizedActivity in the database
        List<TblUnauthorizedActivity> tblUnauthorizedActivityList = tblUnauthorizedActivityRepository.findAll();
        assertThat(tblUnauthorizedActivityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTblUnauthorizedActivity() throws Exception {
        int databaseSizeBeforeUpdate = tblUnauthorizedActivityRepository.findAll().size();
        tblUnauthorizedActivity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblUnauthorizedActivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tblUnauthorizedActivity))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblUnauthorizedActivity in the database
        List<TblUnauthorizedActivity> tblUnauthorizedActivityList = tblUnauthorizedActivityRepository.findAll();
        assertThat(tblUnauthorizedActivityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTblUnauthorizedActivity() throws Exception {
        int databaseSizeBeforeUpdate = tblUnauthorizedActivityRepository.findAll().size();
        tblUnauthorizedActivity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblUnauthorizedActivityMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tblUnauthorizedActivity))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TblUnauthorizedActivity in the database
        List<TblUnauthorizedActivity> tblUnauthorizedActivityList = tblUnauthorizedActivityRepository.findAll();
        assertThat(tblUnauthorizedActivityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTblUnauthorizedActivity() throws Exception {
        // Initialize the database
        tblUnauthorizedActivityRepository.saveAndFlush(tblUnauthorizedActivity);

        int databaseSizeBeforeDelete = tblUnauthorizedActivityRepository.findAll().size();

        // Delete the tblUnauthorizedActivity
        restTblUnauthorizedActivityMockMvc
            .perform(delete(ENTITY_API_URL_ID, tblUnauthorizedActivity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TblUnauthorizedActivity> tblUnauthorizedActivityList = tblUnauthorizedActivityRepository.findAll();
        assertThat(tblUnauthorizedActivityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
