package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.TblEmployeeAuthentication;
import com.mycompany.myapp.repository.TblEmployeeAuthenticationRepository;
import com.mycompany.myapp.service.criteria.TblEmployeeAuthenticationCriteria;
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
 * Integration tests for the {@link TblEmployeeAuthenticationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TblEmployeeAuthenticationResourceIT {

    private static final Integer DEFAULT_EMPLOYEE_ID = 1;
    private static final Integer UPDATED_EMPLOYEE_ID = 2;
    private static final Integer SMALLER_EMPLOYEE_ID = 1 - 1;

    private static final String DEFAULT_RFID_ID = "AAAAAAAAAA";
    private static final String UPDATED_RFID_ID = "BBBBBBBBBB";

    private static final String DEFAULT_FINGERPRINT = "AAAAAAAAAA";
    private static final String UPDATED_FINGERPRINT = "BBBBBBBBBB";

    private static final Integer DEFAULT_IS_ACTIVE = 1;
    private static final Integer UPDATED_IS_ACTIVE = 2;
    private static final Integer SMALLER_IS_ACTIVE = 1 - 1;

    private static final String ENTITY_API_URL = "/api/tbl-employee-authentications";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TblEmployeeAuthenticationRepository tblEmployeeAuthenticationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTblEmployeeAuthenticationMockMvc;

    private TblEmployeeAuthentication tblEmployeeAuthentication;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TblEmployeeAuthentication createEntity(EntityManager em) {
        TblEmployeeAuthentication tblEmployeeAuthentication = new TblEmployeeAuthentication()
            .employeeId(DEFAULT_EMPLOYEE_ID)
            .rfidId(DEFAULT_RFID_ID)
            .fingerprint(DEFAULT_FINGERPRINT)
            .isActive(DEFAULT_IS_ACTIVE);
        return tblEmployeeAuthentication;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TblEmployeeAuthentication createUpdatedEntity(EntityManager em) {
        TblEmployeeAuthentication tblEmployeeAuthentication = new TblEmployeeAuthentication()
            .employeeId(UPDATED_EMPLOYEE_ID)
            .rfidId(UPDATED_RFID_ID)
            .fingerprint(UPDATED_FINGERPRINT)
            .isActive(UPDATED_IS_ACTIVE);
        return tblEmployeeAuthentication;
    }

    @BeforeEach
    public void initTest() {
        tblEmployeeAuthentication = createEntity(em);
    }

    @Test
    @Transactional
    void createTblEmployeeAuthentication() throws Exception {
        int databaseSizeBeforeCreate = tblEmployeeAuthenticationRepository.findAll().size();
        // Create the TblEmployeeAuthentication
        restTblEmployeeAuthenticationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tblEmployeeAuthentication))
            )
            .andExpect(status().isCreated());

        // Validate the TblEmployeeAuthentication in the database
        List<TblEmployeeAuthentication> tblEmployeeAuthenticationList = tblEmployeeAuthenticationRepository.findAll();
        assertThat(tblEmployeeAuthenticationList).hasSize(databaseSizeBeforeCreate + 1);
        TblEmployeeAuthentication testTblEmployeeAuthentication = tblEmployeeAuthenticationList.get(
            tblEmployeeAuthenticationList.size() - 1
        );
        assertThat(testTblEmployeeAuthentication.getEmployeeId()).isEqualTo(DEFAULT_EMPLOYEE_ID);
        assertThat(testTblEmployeeAuthentication.getRfidId()).isEqualTo(DEFAULT_RFID_ID);
        assertThat(testTblEmployeeAuthentication.getFingerprint()).isEqualTo(DEFAULT_FINGERPRINT);
        assertThat(testTblEmployeeAuthentication.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void createTblEmployeeAuthenticationWithExistingId() throws Exception {
        // Create the TblEmployeeAuthentication with an existing ID
        tblEmployeeAuthentication.setId(1L);

        int databaseSizeBeforeCreate = tblEmployeeAuthenticationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTblEmployeeAuthenticationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tblEmployeeAuthentication))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblEmployeeAuthentication in the database
        List<TblEmployeeAuthentication> tblEmployeeAuthenticationList = tblEmployeeAuthenticationRepository.findAll();
        assertThat(tblEmployeeAuthenticationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTblEmployeeAuthentications() throws Exception {
        // Initialize the database
        tblEmployeeAuthenticationRepository.saveAndFlush(tblEmployeeAuthentication);

        // Get all the tblEmployeeAuthenticationList
        restTblEmployeeAuthenticationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tblEmployeeAuthentication.getId().intValue())))
            .andExpect(jsonPath("$.[*].employeeId").value(hasItem(DEFAULT_EMPLOYEE_ID)))
            .andExpect(jsonPath("$.[*].rfidId").value(hasItem(DEFAULT_RFID_ID)))
            .andExpect(jsonPath("$.[*].fingerprint").value(hasItem(DEFAULT_FINGERPRINT.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)));
    }

    @Test
    @Transactional
    void getTblEmployeeAuthentication() throws Exception {
        // Initialize the database
        tblEmployeeAuthenticationRepository.saveAndFlush(tblEmployeeAuthentication);

        // Get the tblEmployeeAuthentication
        restTblEmployeeAuthenticationMockMvc
            .perform(get(ENTITY_API_URL_ID, tblEmployeeAuthentication.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tblEmployeeAuthentication.getId().intValue()))
            .andExpect(jsonPath("$.employeeId").value(DEFAULT_EMPLOYEE_ID))
            .andExpect(jsonPath("$.rfidId").value(DEFAULT_RFID_ID))
            .andExpect(jsonPath("$.fingerprint").value(DEFAULT_FINGERPRINT.toString()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE));
    }

    @Test
    @Transactional
    void getTblEmployeeAuthenticationsByIdFiltering() throws Exception {
        // Initialize the database
        tblEmployeeAuthenticationRepository.saveAndFlush(tblEmployeeAuthentication);

        Long id = tblEmployeeAuthentication.getId();

        defaultTblEmployeeAuthenticationShouldBeFound("id.equals=" + id);
        defaultTblEmployeeAuthenticationShouldNotBeFound("id.notEquals=" + id);

        defaultTblEmployeeAuthenticationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTblEmployeeAuthenticationShouldNotBeFound("id.greaterThan=" + id);

        defaultTblEmployeeAuthenticationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTblEmployeeAuthenticationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTblEmployeeAuthenticationsByEmployeeIdIsEqualToSomething() throws Exception {
        // Initialize the database
        tblEmployeeAuthenticationRepository.saveAndFlush(tblEmployeeAuthentication);

        // Get all the tblEmployeeAuthenticationList where employeeId equals to DEFAULT_EMPLOYEE_ID
        defaultTblEmployeeAuthenticationShouldBeFound("employeeId.equals=" + DEFAULT_EMPLOYEE_ID);

        // Get all the tblEmployeeAuthenticationList where employeeId equals to UPDATED_EMPLOYEE_ID
        defaultTblEmployeeAuthenticationShouldNotBeFound("employeeId.equals=" + UPDATED_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllTblEmployeeAuthenticationsByEmployeeIdIsInShouldWork() throws Exception {
        // Initialize the database
        tblEmployeeAuthenticationRepository.saveAndFlush(tblEmployeeAuthentication);

        // Get all the tblEmployeeAuthenticationList where employeeId in DEFAULT_EMPLOYEE_ID or UPDATED_EMPLOYEE_ID
        defaultTblEmployeeAuthenticationShouldBeFound("employeeId.in=" + DEFAULT_EMPLOYEE_ID + "," + UPDATED_EMPLOYEE_ID);

        // Get all the tblEmployeeAuthenticationList where employeeId equals to UPDATED_EMPLOYEE_ID
        defaultTblEmployeeAuthenticationShouldNotBeFound("employeeId.in=" + UPDATED_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllTblEmployeeAuthenticationsByEmployeeIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblEmployeeAuthenticationRepository.saveAndFlush(tblEmployeeAuthentication);

        // Get all the tblEmployeeAuthenticationList where employeeId is not null
        defaultTblEmployeeAuthenticationShouldBeFound("employeeId.specified=true");

        // Get all the tblEmployeeAuthenticationList where employeeId is null
        defaultTblEmployeeAuthenticationShouldNotBeFound("employeeId.specified=false");
    }

    @Test
    @Transactional
    void getAllTblEmployeeAuthenticationsByEmployeeIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tblEmployeeAuthenticationRepository.saveAndFlush(tblEmployeeAuthentication);

        // Get all the tblEmployeeAuthenticationList where employeeId is greater than or equal to DEFAULT_EMPLOYEE_ID
        defaultTblEmployeeAuthenticationShouldBeFound("employeeId.greaterThanOrEqual=" + DEFAULT_EMPLOYEE_ID);

        // Get all the tblEmployeeAuthenticationList where employeeId is greater than or equal to UPDATED_EMPLOYEE_ID
        defaultTblEmployeeAuthenticationShouldNotBeFound("employeeId.greaterThanOrEqual=" + UPDATED_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllTblEmployeeAuthenticationsByEmployeeIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tblEmployeeAuthenticationRepository.saveAndFlush(tblEmployeeAuthentication);

        // Get all the tblEmployeeAuthenticationList where employeeId is less than or equal to DEFAULT_EMPLOYEE_ID
        defaultTblEmployeeAuthenticationShouldBeFound("employeeId.lessThanOrEqual=" + DEFAULT_EMPLOYEE_ID);

        // Get all the tblEmployeeAuthenticationList where employeeId is less than or equal to SMALLER_EMPLOYEE_ID
        defaultTblEmployeeAuthenticationShouldNotBeFound("employeeId.lessThanOrEqual=" + SMALLER_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllTblEmployeeAuthenticationsByEmployeeIdIsLessThanSomething() throws Exception {
        // Initialize the database
        tblEmployeeAuthenticationRepository.saveAndFlush(tblEmployeeAuthentication);

        // Get all the tblEmployeeAuthenticationList where employeeId is less than DEFAULT_EMPLOYEE_ID
        defaultTblEmployeeAuthenticationShouldNotBeFound("employeeId.lessThan=" + DEFAULT_EMPLOYEE_ID);

        // Get all the tblEmployeeAuthenticationList where employeeId is less than UPDATED_EMPLOYEE_ID
        defaultTblEmployeeAuthenticationShouldBeFound("employeeId.lessThan=" + UPDATED_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllTblEmployeeAuthenticationsByEmployeeIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tblEmployeeAuthenticationRepository.saveAndFlush(tblEmployeeAuthentication);

        // Get all the tblEmployeeAuthenticationList where employeeId is greater than DEFAULT_EMPLOYEE_ID
        defaultTblEmployeeAuthenticationShouldNotBeFound("employeeId.greaterThan=" + DEFAULT_EMPLOYEE_ID);

        // Get all the tblEmployeeAuthenticationList where employeeId is greater than SMALLER_EMPLOYEE_ID
        defaultTblEmployeeAuthenticationShouldBeFound("employeeId.greaterThan=" + SMALLER_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllTblEmployeeAuthenticationsByRfidIdIsEqualToSomething() throws Exception {
        // Initialize the database
        tblEmployeeAuthenticationRepository.saveAndFlush(tblEmployeeAuthentication);

        // Get all the tblEmployeeAuthenticationList where rfidId equals to DEFAULT_RFID_ID
        defaultTblEmployeeAuthenticationShouldBeFound("rfidId.equals=" + DEFAULT_RFID_ID);

        // Get all the tblEmployeeAuthenticationList where rfidId equals to UPDATED_RFID_ID
        defaultTblEmployeeAuthenticationShouldNotBeFound("rfidId.equals=" + UPDATED_RFID_ID);
    }

    @Test
    @Transactional
    void getAllTblEmployeeAuthenticationsByRfidIdIsInShouldWork() throws Exception {
        // Initialize the database
        tblEmployeeAuthenticationRepository.saveAndFlush(tblEmployeeAuthentication);

        // Get all the tblEmployeeAuthenticationList where rfidId in DEFAULT_RFID_ID or UPDATED_RFID_ID
        defaultTblEmployeeAuthenticationShouldBeFound("rfidId.in=" + DEFAULT_RFID_ID + "," + UPDATED_RFID_ID);

        // Get all the tblEmployeeAuthenticationList where rfidId equals to UPDATED_RFID_ID
        defaultTblEmployeeAuthenticationShouldNotBeFound("rfidId.in=" + UPDATED_RFID_ID);
    }

    @Test
    @Transactional
    void getAllTblEmployeeAuthenticationsByRfidIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblEmployeeAuthenticationRepository.saveAndFlush(tblEmployeeAuthentication);

        // Get all the tblEmployeeAuthenticationList where rfidId is not null
        defaultTblEmployeeAuthenticationShouldBeFound("rfidId.specified=true");

        // Get all the tblEmployeeAuthenticationList where rfidId is null
        defaultTblEmployeeAuthenticationShouldNotBeFound("rfidId.specified=false");
    }

    @Test
    @Transactional
    void getAllTblEmployeeAuthenticationsByRfidIdContainsSomething() throws Exception {
        // Initialize the database
        tblEmployeeAuthenticationRepository.saveAndFlush(tblEmployeeAuthentication);

        // Get all the tblEmployeeAuthenticationList where rfidId contains DEFAULT_RFID_ID
        defaultTblEmployeeAuthenticationShouldBeFound("rfidId.contains=" + DEFAULT_RFID_ID);

        // Get all the tblEmployeeAuthenticationList where rfidId contains UPDATED_RFID_ID
        defaultTblEmployeeAuthenticationShouldNotBeFound("rfidId.contains=" + UPDATED_RFID_ID);
    }

    @Test
    @Transactional
    void getAllTblEmployeeAuthenticationsByRfidIdNotContainsSomething() throws Exception {
        // Initialize the database
        tblEmployeeAuthenticationRepository.saveAndFlush(tblEmployeeAuthentication);

        // Get all the tblEmployeeAuthenticationList where rfidId does not contain DEFAULT_RFID_ID
        defaultTblEmployeeAuthenticationShouldNotBeFound("rfidId.doesNotContain=" + DEFAULT_RFID_ID);

        // Get all the tblEmployeeAuthenticationList where rfidId does not contain UPDATED_RFID_ID
        defaultTblEmployeeAuthenticationShouldBeFound("rfidId.doesNotContain=" + UPDATED_RFID_ID);
    }

    @Test
    @Transactional
    void getAllTblEmployeeAuthenticationsByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        tblEmployeeAuthenticationRepository.saveAndFlush(tblEmployeeAuthentication);

        // Get all the tblEmployeeAuthenticationList where isActive equals to DEFAULT_IS_ACTIVE
        defaultTblEmployeeAuthenticationShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the tblEmployeeAuthenticationList where isActive equals to UPDATED_IS_ACTIVE
        defaultTblEmployeeAuthenticationShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllTblEmployeeAuthenticationsByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        tblEmployeeAuthenticationRepository.saveAndFlush(tblEmployeeAuthentication);

        // Get all the tblEmployeeAuthenticationList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultTblEmployeeAuthenticationShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the tblEmployeeAuthenticationList where isActive equals to UPDATED_IS_ACTIVE
        defaultTblEmployeeAuthenticationShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllTblEmployeeAuthenticationsByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblEmployeeAuthenticationRepository.saveAndFlush(tblEmployeeAuthentication);

        // Get all the tblEmployeeAuthenticationList where isActive is not null
        defaultTblEmployeeAuthenticationShouldBeFound("isActive.specified=true");

        // Get all the tblEmployeeAuthenticationList where isActive is null
        defaultTblEmployeeAuthenticationShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllTblEmployeeAuthenticationsByIsActiveIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tblEmployeeAuthenticationRepository.saveAndFlush(tblEmployeeAuthentication);

        // Get all the tblEmployeeAuthenticationList where isActive is greater than or equal to DEFAULT_IS_ACTIVE
        defaultTblEmployeeAuthenticationShouldBeFound("isActive.greaterThanOrEqual=" + DEFAULT_IS_ACTIVE);

        // Get all the tblEmployeeAuthenticationList where isActive is greater than or equal to UPDATED_IS_ACTIVE
        defaultTblEmployeeAuthenticationShouldNotBeFound("isActive.greaterThanOrEqual=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllTblEmployeeAuthenticationsByIsActiveIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tblEmployeeAuthenticationRepository.saveAndFlush(tblEmployeeAuthentication);

        // Get all the tblEmployeeAuthenticationList where isActive is less than or equal to DEFAULT_IS_ACTIVE
        defaultTblEmployeeAuthenticationShouldBeFound("isActive.lessThanOrEqual=" + DEFAULT_IS_ACTIVE);

        // Get all the tblEmployeeAuthenticationList where isActive is less than or equal to SMALLER_IS_ACTIVE
        defaultTblEmployeeAuthenticationShouldNotBeFound("isActive.lessThanOrEqual=" + SMALLER_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllTblEmployeeAuthenticationsByIsActiveIsLessThanSomething() throws Exception {
        // Initialize the database
        tblEmployeeAuthenticationRepository.saveAndFlush(tblEmployeeAuthentication);

        // Get all the tblEmployeeAuthenticationList where isActive is less than DEFAULT_IS_ACTIVE
        defaultTblEmployeeAuthenticationShouldNotBeFound("isActive.lessThan=" + DEFAULT_IS_ACTIVE);

        // Get all the tblEmployeeAuthenticationList where isActive is less than UPDATED_IS_ACTIVE
        defaultTblEmployeeAuthenticationShouldBeFound("isActive.lessThan=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllTblEmployeeAuthenticationsByIsActiveIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tblEmployeeAuthenticationRepository.saveAndFlush(tblEmployeeAuthentication);

        // Get all the tblEmployeeAuthenticationList where isActive is greater than DEFAULT_IS_ACTIVE
        defaultTblEmployeeAuthenticationShouldNotBeFound("isActive.greaterThan=" + DEFAULT_IS_ACTIVE);

        // Get all the tblEmployeeAuthenticationList where isActive is greater than SMALLER_IS_ACTIVE
        defaultTblEmployeeAuthenticationShouldBeFound("isActive.greaterThan=" + SMALLER_IS_ACTIVE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTblEmployeeAuthenticationShouldBeFound(String filter) throws Exception {
        restTblEmployeeAuthenticationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tblEmployeeAuthentication.getId().intValue())))
            .andExpect(jsonPath("$.[*].employeeId").value(hasItem(DEFAULT_EMPLOYEE_ID)))
            .andExpect(jsonPath("$.[*].rfidId").value(hasItem(DEFAULT_RFID_ID)))
            .andExpect(jsonPath("$.[*].fingerprint").value(hasItem(DEFAULT_FINGERPRINT.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)));

        // Check, that the count call also returns 1
        restTblEmployeeAuthenticationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTblEmployeeAuthenticationShouldNotBeFound(String filter) throws Exception {
        restTblEmployeeAuthenticationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTblEmployeeAuthenticationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTblEmployeeAuthentication() throws Exception {
        // Get the tblEmployeeAuthentication
        restTblEmployeeAuthenticationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTblEmployeeAuthentication() throws Exception {
        // Initialize the database
        tblEmployeeAuthenticationRepository.saveAndFlush(tblEmployeeAuthentication);

        int databaseSizeBeforeUpdate = tblEmployeeAuthenticationRepository.findAll().size();

        // Update the tblEmployeeAuthentication
        TblEmployeeAuthentication updatedTblEmployeeAuthentication = tblEmployeeAuthenticationRepository
            .findById(tblEmployeeAuthentication.getId())
            .get();
        // Disconnect from session so that the updates on updatedTblEmployeeAuthentication are not directly saved in db
        em.detach(updatedTblEmployeeAuthentication);
        updatedTblEmployeeAuthentication
            .employeeId(UPDATED_EMPLOYEE_ID)
            .rfidId(UPDATED_RFID_ID)
            .fingerprint(UPDATED_FINGERPRINT)
            .isActive(UPDATED_IS_ACTIVE);

        restTblEmployeeAuthenticationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTblEmployeeAuthentication.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTblEmployeeAuthentication))
            )
            .andExpect(status().isOk());

        // Validate the TblEmployeeAuthentication in the database
        List<TblEmployeeAuthentication> tblEmployeeAuthenticationList = tblEmployeeAuthenticationRepository.findAll();
        assertThat(tblEmployeeAuthenticationList).hasSize(databaseSizeBeforeUpdate);
        TblEmployeeAuthentication testTblEmployeeAuthentication = tblEmployeeAuthenticationList.get(
            tblEmployeeAuthenticationList.size() - 1
        );
        assertThat(testTblEmployeeAuthentication.getEmployeeId()).isEqualTo(UPDATED_EMPLOYEE_ID);
        assertThat(testTblEmployeeAuthentication.getRfidId()).isEqualTo(UPDATED_RFID_ID);
        assertThat(testTblEmployeeAuthentication.getFingerprint()).isEqualTo(UPDATED_FINGERPRINT);
        assertThat(testTblEmployeeAuthentication.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingTblEmployeeAuthentication() throws Exception {
        int databaseSizeBeforeUpdate = tblEmployeeAuthenticationRepository.findAll().size();
        tblEmployeeAuthentication.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTblEmployeeAuthenticationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tblEmployeeAuthentication.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tblEmployeeAuthentication))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblEmployeeAuthentication in the database
        List<TblEmployeeAuthentication> tblEmployeeAuthenticationList = tblEmployeeAuthenticationRepository.findAll();
        assertThat(tblEmployeeAuthenticationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTblEmployeeAuthentication() throws Exception {
        int databaseSizeBeforeUpdate = tblEmployeeAuthenticationRepository.findAll().size();
        tblEmployeeAuthentication.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblEmployeeAuthenticationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tblEmployeeAuthentication))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblEmployeeAuthentication in the database
        List<TblEmployeeAuthentication> tblEmployeeAuthenticationList = tblEmployeeAuthenticationRepository.findAll();
        assertThat(tblEmployeeAuthenticationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTblEmployeeAuthentication() throws Exception {
        int databaseSizeBeforeUpdate = tblEmployeeAuthenticationRepository.findAll().size();
        tblEmployeeAuthentication.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblEmployeeAuthenticationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tblEmployeeAuthentication))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TblEmployeeAuthentication in the database
        List<TblEmployeeAuthentication> tblEmployeeAuthenticationList = tblEmployeeAuthenticationRepository.findAll();
        assertThat(tblEmployeeAuthenticationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTblEmployeeAuthenticationWithPatch() throws Exception {
        // Initialize the database
        tblEmployeeAuthenticationRepository.saveAndFlush(tblEmployeeAuthentication);

        int databaseSizeBeforeUpdate = tblEmployeeAuthenticationRepository.findAll().size();

        // Update the tblEmployeeAuthentication using partial update
        TblEmployeeAuthentication partialUpdatedTblEmployeeAuthentication = new TblEmployeeAuthentication();
        partialUpdatedTblEmployeeAuthentication.setId(tblEmployeeAuthentication.getId());

        partialUpdatedTblEmployeeAuthentication.employeeId(UPDATED_EMPLOYEE_ID).isActive(UPDATED_IS_ACTIVE);

        restTblEmployeeAuthenticationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTblEmployeeAuthentication.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTblEmployeeAuthentication))
            )
            .andExpect(status().isOk());

        // Validate the TblEmployeeAuthentication in the database
        List<TblEmployeeAuthentication> tblEmployeeAuthenticationList = tblEmployeeAuthenticationRepository.findAll();
        assertThat(tblEmployeeAuthenticationList).hasSize(databaseSizeBeforeUpdate);
        TblEmployeeAuthentication testTblEmployeeAuthentication = tblEmployeeAuthenticationList.get(
            tblEmployeeAuthenticationList.size() - 1
        );
        assertThat(testTblEmployeeAuthentication.getEmployeeId()).isEqualTo(UPDATED_EMPLOYEE_ID);
        assertThat(testTblEmployeeAuthentication.getRfidId()).isEqualTo(DEFAULT_RFID_ID);
        assertThat(testTblEmployeeAuthentication.getFingerprint()).isEqualTo(DEFAULT_FINGERPRINT);
        assertThat(testTblEmployeeAuthentication.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateTblEmployeeAuthenticationWithPatch() throws Exception {
        // Initialize the database
        tblEmployeeAuthenticationRepository.saveAndFlush(tblEmployeeAuthentication);

        int databaseSizeBeforeUpdate = tblEmployeeAuthenticationRepository.findAll().size();

        // Update the tblEmployeeAuthentication using partial update
        TblEmployeeAuthentication partialUpdatedTblEmployeeAuthentication = new TblEmployeeAuthentication();
        partialUpdatedTblEmployeeAuthentication.setId(tblEmployeeAuthentication.getId());

        partialUpdatedTblEmployeeAuthentication
            .employeeId(UPDATED_EMPLOYEE_ID)
            .rfidId(UPDATED_RFID_ID)
            .fingerprint(UPDATED_FINGERPRINT)
            .isActive(UPDATED_IS_ACTIVE);

        restTblEmployeeAuthenticationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTblEmployeeAuthentication.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTblEmployeeAuthentication))
            )
            .andExpect(status().isOk());

        // Validate the TblEmployeeAuthentication in the database
        List<TblEmployeeAuthentication> tblEmployeeAuthenticationList = tblEmployeeAuthenticationRepository.findAll();
        assertThat(tblEmployeeAuthenticationList).hasSize(databaseSizeBeforeUpdate);
        TblEmployeeAuthentication testTblEmployeeAuthentication = tblEmployeeAuthenticationList.get(
            tblEmployeeAuthenticationList.size() - 1
        );
        assertThat(testTblEmployeeAuthentication.getEmployeeId()).isEqualTo(UPDATED_EMPLOYEE_ID);
        assertThat(testTblEmployeeAuthentication.getRfidId()).isEqualTo(UPDATED_RFID_ID);
        assertThat(testTblEmployeeAuthentication.getFingerprint()).isEqualTo(UPDATED_FINGERPRINT);
        assertThat(testTblEmployeeAuthentication.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingTblEmployeeAuthentication() throws Exception {
        int databaseSizeBeforeUpdate = tblEmployeeAuthenticationRepository.findAll().size();
        tblEmployeeAuthentication.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTblEmployeeAuthenticationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tblEmployeeAuthentication.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tblEmployeeAuthentication))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblEmployeeAuthentication in the database
        List<TblEmployeeAuthentication> tblEmployeeAuthenticationList = tblEmployeeAuthenticationRepository.findAll();
        assertThat(tblEmployeeAuthenticationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTblEmployeeAuthentication() throws Exception {
        int databaseSizeBeforeUpdate = tblEmployeeAuthenticationRepository.findAll().size();
        tblEmployeeAuthentication.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblEmployeeAuthenticationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tblEmployeeAuthentication))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblEmployeeAuthentication in the database
        List<TblEmployeeAuthentication> tblEmployeeAuthenticationList = tblEmployeeAuthenticationRepository.findAll();
        assertThat(tblEmployeeAuthenticationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTblEmployeeAuthentication() throws Exception {
        int databaseSizeBeforeUpdate = tblEmployeeAuthenticationRepository.findAll().size();
        tblEmployeeAuthentication.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblEmployeeAuthenticationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tblEmployeeAuthentication))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TblEmployeeAuthentication in the database
        List<TblEmployeeAuthentication> tblEmployeeAuthenticationList = tblEmployeeAuthenticationRepository.findAll();
        assertThat(tblEmployeeAuthenticationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTblEmployeeAuthentication() throws Exception {
        // Initialize the database
        tblEmployeeAuthenticationRepository.saveAndFlush(tblEmployeeAuthentication);

        int databaseSizeBeforeDelete = tblEmployeeAuthenticationRepository.findAll().size();

        // Delete the tblEmployeeAuthentication
        restTblEmployeeAuthenticationMockMvc
            .perform(delete(ENTITY_API_URL_ID, tblEmployeeAuthentication.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TblEmployeeAuthentication> tblEmployeeAuthenticationList = tblEmployeeAuthenticationRepository.findAll();
        assertThat(tblEmployeeAuthenticationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
