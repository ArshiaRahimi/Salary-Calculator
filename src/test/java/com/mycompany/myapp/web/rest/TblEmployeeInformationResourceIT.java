package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.TblEmployeeInformation;
import com.mycompany.myapp.repository.TblEmployeeInformationRepository;
import com.mycompany.myapp.service.criteria.TblEmployeeInformationCriteria;
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
 * Integration tests for the {@link TblEmployeeInformationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TblEmployeeInformationResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FAMILY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FAMILY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tbl-employee-informations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TblEmployeeInformationRepository tblEmployeeInformationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTblEmployeeInformationMockMvc;

    private TblEmployeeInformation tblEmployeeInformation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TblEmployeeInformation createEntity(EntityManager em) {
        TblEmployeeInformation tblEmployeeInformation = new TblEmployeeInformation()
            .name(DEFAULT_NAME)
            .familyName(DEFAULT_FAMILY_NAME)
            .phoneNumber(DEFAULT_PHONE_NUMBER);
        return tblEmployeeInformation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TblEmployeeInformation createUpdatedEntity(EntityManager em) {
        TblEmployeeInformation tblEmployeeInformation = new TblEmployeeInformation()
            .name(UPDATED_NAME)
            .familyName(UPDATED_FAMILY_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER);
        return tblEmployeeInformation;
    }

    @BeforeEach
    public void initTest() {
        tblEmployeeInformation = createEntity(em);
    }

    @Test
    @Transactional
    void createTblEmployeeInformation() throws Exception {
        int databaseSizeBeforeCreate = tblEmployeeInformationRepository.findAll().size();
        // Create the TblEmployeeInformation
        restTblEmployeeInformationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tblEmployeeInformation))
            )
            .andExpect(status().isCreated());

        // Validate the TblEmployeeInformation in the database
        List<TblEmployeeInformation> tblEmployeeInformationList = tblEmployeeInformationRepository.findAll();
        assertThat(tblEmployeeInformationList).hasSize(databaseSizeBeforeCreate + 1);
        TblEmployeeInformation testTblEmployeeInformation = tblEmployeeInformationList.get(tblEmployeeInformationList.size() - 1);
        assertThat(testTblEmployeeInformation.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTblEmployeeInformation.getFamilyName()).isEqualTo(DEFAULT_FAMILY_NAME);
        assertThat(testTblEmployeeInformation.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void createTblEmployeeInformationWithExistingId() throws Exception {
        // Create the TblEmployeeInformation with an existing ID
        tblEmployeeInformation.setId(1L);

        int databaseSizeBeforeCreate = tblEmployeeInformationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTblEmployeeInformationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tblEmployeeInformation))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblEmployeeInformation in the database
        List<TblEmployeeInformation> tblEmployeeInformationList = tblEmployeeInformationRepository.findAll();
        assertThat(tblEmployeeInformationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTblEmployeeInformations() throws Exception {
        // Initialize the database
        tblEmployeeInformationRepository.saveAndFlush(tblEmployeeInformation);

        // Get all the tblEmployeeInformationList
        restTblEmployeeInformationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tblEmployeeInformation.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].familyName").value(hasItem(DEFAULT_FAMILY_NAME)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));
    }

    @Test
    @Transactional
    void getTblEmployeeInformation() throws Exception {
        // Initialize the database
        tblEmployeeInformationRepository.saveAndFlush(tblEmployeeInformation);

        // Get the tblEmployeeInformation
        restTblEmployeeInformationMockMvc
            .perform(get(ENTITY_API_URL_ID, tblEmployeeInformation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tblEmployeeInformation.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.familyName").value(DEFAULT_FAMILY_NAME))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER));
    }

    @Test
    @Transactional
    void getTblEmployeeInformationsByIdFiltering() throws Exception {
        // Initialize the database
        tblEmployeeInformationRepository.saveAndFlush(tblEmployeeInformation);

        Long id = tblEmployeeInformation.getId();

        defaultTblEmployeeInformationShouldBeFound("id.equals=" + id);
        defaultTblEmployeeInformationShouldNotBeFound("id.notEquals=" + id);

        defaultTblEmployeeInformationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTblEmployeeInformationShouldNotBeFound("id.greaterThan=" + id);

        defaultTblEmployeeInformationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTblEmployeeInformationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTblEmployeeInformationsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        tblEmployeeInformationRepository.saveAndFlush(tblEmployeeInformation);

        // Get all the tblEmployeeInformationList where name equals to DEFAULT_NAME
        defaultTblEmployeeInformationShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the tblEmployeeInformationList where name equals to UPDATED_NAME
        defaultTblEmployeeInformationShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTblEmployeeInformationsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        tblEmployeeInformationRepository.saveAndFlush(tblEmployeeInformation);

        // Get all the tblEmployeeInformationList where name in DEFAULT_NAME or UPDATED_NAME
        defaultTblEmployeeInformationShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the tblEmployeeInformationList where name equals to UPDATED_NAME
        defaultTblEmployeeInformationShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTblEmployeeInformationsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblEmployeeInformationRepository.saveAndFlush(tblEmployeeInformation);

        // Get all the tblEmployeeInformationList where name is not null
        defaultTblEmployeeInformationShouldBeFound("name.specified=true");

        // Get all the tblEmployeeInformationList where name is null
        defaultTblEmployeeInformationShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllTblEmployeeInformationsByNameContainsSomething() throws Exception {
        // Initialize the database
        tblEmployeeInformationRepository.saveAndFlush(tblEmployeeInformation);

        // Get all the tblEmployeeInformationList where name contains DEFAULT_NAME
        defaultTblEmployeeInformationShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the tblEmployeeInformationList where name contains UPDATED_NAME
        defaultTblEmployeeInformationShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTblEmployeeInformationsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        tblEmployeeInformationRepository.saveAndFlush(tblEmployeeInformation);

        // Get all the tblEmployeeInformationList where name does not contain DEFAULT_NAME
        defaultTblEmployeeInformationShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the tblEmployeeInformationList where name does not contain UPDATED_NAME
        defaultTblEmployeeInformationShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTblEmployeeInformationsByFamilyNameIsEqualToSomething() throws Exception {
        // Initialize the database
        tblEmployeeInformationRepository.saveAndFlush(tblEmployeeInformation);

        // Get all the tblEmployeeInformationList where familyName equals to DEFAULT_FAMILY_NAME
        defaultTblEmployeeInformationShouldBeFound("familyName.equals=" + DEFAULT_FAMILY_NAME);

        // Get all the tblEmployeeInformationList where familyName equals to UPDATED_FAMILY_NAME
        defaultTblEmployeeInformationShouldNotBeFound("familyName.equals=" + UPDATED_FAMILY_NAME);
    }

    @Test
    @Transactional
    void getAllTblEmployeeInformationsByFamilyNameIsInShouldWork() throws Exception {
        // Initialize the database
        tblEmployeeInformationRepository.saveAndFlush(tblEmployeeInformation);

        // Get all the tblEmployeeInformationList where familyName in DEFAULT_FAMILY_NAME or UPDATED_FAMILY_NAME
        defaultTblEmployeeInformationShouldBeFound("familyName.in=" + DEFAULT_FAMILY_NAME + "," + UPDATED_FAMILY_NAME);

        // Get all the tblEmployeeInformationList where familyName equals to UPDATED_FAMILY_NAME
        defaultTblEmployeeInformationShouldNotBeFound("familyName.in=" + UPDATED_FAMILY_NAME);
    }

    @Test
    @Transactional
    void getAllTblEmployeeInformationsByFamilyNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblEmployeeInformationRepository.saveAndFlush(tblEmployeeInformation);

        // Get all the tblEmployeeInformationList where familyName is not null
        defaultTblEmployeeInformationShouldBeFound("familyName.specified=true");

        // Get all the tblEmployeeInformationList where familyName is null
        defaultTblEmployeeInformationShouldNotBeFound("familyName.specified=false");
    }

    @Test
    @Transactional
    void getAllTblEmployeeInformationsByFamilyNameContainsSomething() throws Exception {
        // Initialize the database
        tblEmployeeInformationRepository.saveAndFlush(tblEmployeeInformation);

        // Get all the tblEmployeeInformationList where familyName contains DEFAULT_FAMILY_NAME
        defaultTblEmployeeInformationShouldBeFound("familyName.contains=" + DEFAULT_FAMILY_NAME);

        // Get all the tblEmployeeInformationList where familyName contains UPDATED_FAMILY_NAME
        defaultTblEmployeeInformationShouldNotBeFound("familyName.contains=" + UPDATED_FAMILY_NAME);
    }

    @Test
    @Transactional
    void getAllTblEmployeeInformationsByFamilyNameNotContainsSomething() throws Exception {
        // Initialize the database
        tblEmployeeInformationRepository.saveAndFlush(tblEmployeeInformation);

        // Get all the tblEmployeeInformationList where familyName does not contain DEFAULT_FAMILY_NAME
        defaultTblEmployeeInformationShouldNotBeFound("familyName.doesNotContain=" + DEFAULT_FAMILY_NAME);

        // Get all the tblEmployeeInformationList where familyName does not contain UPDATED_FAMILY_NAME
        defaultTblEmployeeInformationShouldBeFound("familyName.doesNotContain=" + UPDATED_FAMILY_NAME);
    }

    @Test
    @Transactional
    void getAllTblEmployeeInformationsByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        tblEmployeeInformationRepository.saveAndFlush(tblEmployeeInformation);

        // Get all the tblEmployeeInformationList where phoneNumber equals to DEFAULT_PHONE_NUMBER
        defaultTblEmployeeInformationShouldBeFound("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER);

        // Get all the tblEmployeeInformationList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultTblEmployeeInformationShouldNotBeFound("phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllTblEmployeeInformationsByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        tblEmployeeInformationRepository.saveAndFlush(tblEmployeeInformation);

        // Get all the tblEmployeeInformationList where phoneNumber in DEFAULT_PHONE_NUMBER or UPDATED_PHONE_NUMBER
        defaultTblEmployeeInformationShouldBeFound("phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER);

        // Get all the tblEmployeeInformationList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultTblEmployeeInformationShouldNotBeFound("phoneNumber.in=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllTblEmployeeInformationsByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblEmployeeInformationRepository.saveAndFlush(tblEmployeeInformation);

        // Get all the tblEmployeeInformationList where phoneNumber is not null
        defaultTblEmployeeInformationShouldBeFound("phoneNumber.specified=true");

        // Get all the tblEmployeeInformationList where phoneNumber is null
        defaultTblEmployeeInformationShouldNotBeFound("phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllTblEmployeeInformationsByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        tblEmployeeInformationRepository.saveAndFlush(tblEmployeeInformation);

        // Get all the tblEmployeeInformationList where phoneNumber contains DEFAULT_PHONE_NUMBER
        defaultTblEmployeeInformationShouldBeFound("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER);

        // Get all the tblEmployeeInformationList where phoneNumber contains UPDATED_PHONE_NUMBER
        defaultTblEmployeeInformationShouldNotBeFound("phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllTblEmployeeInformationsByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        tblEmployeeInformationRepository.saveAndFlush(tblEmployeeInformation);

        // Get all the tblEmployeeInformationList where phoneNumber does not contain DEFAULT_PHONE_NUMBER
        defaultTblEmployeeInformationShouldNotBeFound("phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER);

        // Get all the tblEmployeeInformationList where phoneNumber does not contain UPDATED_PHONE_NUMBER
        defaultTblEmployeeInformationShouldBeFound("phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTblEmployeeInformationShouldBeFound(String filter) throws Exception {
        restTblEmployeeInformationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tblEmployeeInformation.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].familyName").value(hasItem(DEFAULT_FAMILY_NAME)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));

        // Check, that the count call also returns 1
        restTblEmployeeInformationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTblEmployeeInformationShouldNotBeFound(String filter) throws Exception {
        restTblEmployeeInformationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTblEmployeeInformationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTblEmployeeInformation() throws Exception {
        // Get the tblEmployeeInformation
        restTblEmployeeInformationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTblEmployeeInformation() throws Exception {
        // Initialize the database
        tblEmployeeInformationRepository.saveAndFlush(tblEmployeeInformation);

        int databaseSizeBeforeUpdate = tblEmployeeInformationRepository.findAll().size();

        // Update the tblEmployeeInformation
        TblEmployeeInformation updatedTblEmployeeInformation = tblEmployeeInformationRepository
            .findById(tblEmployeeInformation.getId())
            .get();
        // Disconnect from session so that the updates on updatedTblEmployeeInformation are not directly saved in db
        em.detach(updatedTblEmployeeInformation);
        updatedTblEmployeeInformation.name(UPDATED_NAME).familyName(UPDATED_FAMILY_NAME).phoneNumber(UPDATED_PHONE_NUMBER);

        restTblEmployeeInformationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTblEmployeeInformation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTblEmployeeInformation))
            )
            .andExpect(status().isOk());

        // Validate the TblEmployeeInformation in the database
        List<TblEmployeeInformation> tblEmployeeInformationList = tblEmployeeInformationRepository.findAll();
        assertThat(tblEmployeeInformationList).hasSize(databaseSizeBeforeUpdate);
        TblEmployeeInformation testTblEmployeeInformation = tblEmployeeInformationList.get(tblEmployeeInformationList.size() - 1);
        assertThat(testTblEmployeeInformation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTblEmployeeInformation.getFamilyName()).isEqualTo(UPDATED_FAMILY_NAME);
        assertThat(testTblEmployeeInformation.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void putNonExistingTblEmployeeInformation() throws Exception {
        int databaseSizeBeforeUpdate = tblEmployeeInformationRepository.findAll().size();
        tblEmployeeInformation.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTblEmployeeInformationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tblEmployeeInformation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tblEmployeeInformation))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblEmployeeInformation in the database
        List<TblEmployeeInformation> tblEmployeeInformationList = tblEmployeeInformationRepository.findAll();
        assertThat(tblEmployeeInformationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTblEmployeeInformation() throws Exception {
        int databaseSizeBeforeUpdate = tblEmployeeInformationRepository.findAll().size();
        tblEmployeeInformation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblEmployeeInformationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tblEmployeeInformation))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblEmployeeInformation in the database
        List<TblEmployeeInformation> tblEmployeeInformationList = tblEmployeeInformationRepository.findAll();
        assertThat(tblEmployeeInformationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTblEmployeeInformation() throws Exception {
        int databaseSizeBeforeUpdate = tblEmployeeInformationRepository.findAll().size();
        tblEmployeeInformation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblEmployeeInformationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tblEmployeeInformation))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TblEmployeeInformation in the database
        List<TblEmployeeInformation> tblEmployeeInformationList = tblEmployeeInformationRepository.findAll();
        assertThat(tblEmployeeInformationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTblEmployeeInformationWithPatch() throws Exception {
        // Initialize the database
        tblEmployeeInformationRepository.saveAndFlush(tblEmployeeInformation);

        int databaseSizeBeforeUpdate = tblEmployeeInformationRepository.findAll().size();

        // Update the tblEmployeeInformation using partial update
        TblEmployeeInformation partialUpdatedTblEmployeeInformation = new TblEmployeeInformation();
        partialUpdatedTblEmployeeInformation.setId(tblEmployeeInformation.getId());

        partialUpdatedTblEmployeeInformation.phoneNumber(UPDATED_PHONE_NUMBER);

        restTblEmployeeInformationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTblEmployeeInformation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTblEmployeeInformation))
            )
            .andExpect(status().isOk());

        // Validate the TblEmployeeInformation in the database
        List<TblEmployeeInformation> tblEmployeeInformationList = tblEmployeeInformationRepository.findAll();
        assertThat(tblEmployeeInformationList).hasSize(databaseSizeBeforeUpdate);
        TblEmployeeInformation testTblEmployeeInformation = tblEmployeeInformationList.get(tblEmployeeInformationList.size() - 1);
        assertThat(testTblEmployeeInformation.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTblEmployeeInformation.getFamilyName()).isEqualTo(DEFAULT_FAMILY_NAME);
        assertThat(testTblEmployeeInformation.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void fullUpdateTblEmployeeInformationWithPatch() throws Exception {
        // Initialize the database
        tblEmployeeInformationRepository.saveAndFlush(tblEmployeeInformation);

        int databaseSizeBeforeUpdate = tblEmployeeInformationRepository.findAll().size();

        // Update the tblEmployeeInformation using partial update
        TblEmployeeInformation partialUpdatedTblEmployeeInformation = new TblEmployeeInformation();
        partialUpdatedTblEmployeeInformation.setId(tblEmployeeInformation.getId());

        partialUpdatedTblEmployeeInformation.name(UPDATED_NAME).familyName(UPDATED_FAMILY_NAME).phoneNumber(UPDATED_PHONE_NUMBER);

        restTblEmployeeInformationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTblEmployeeInformation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTblEmployeeInformation))
            )
            .andExpect(status().isOk());

        // Validate the TblEmployeeInformation in the database
        List<TblEmployeeInformation> tblEmployeeInformationList = tblEmployeeInformationRepository.findAll();
        assertThat(tblEmployeeInformationList).hasSize(databaseSizeBeforeUpdate);
        TblEmployeeInformation testTblEmployeeInformation = tblEmployeeInformationList.get(tblEmployeeInformationList.size() - 1);
        assertThat(testTblEmployeeInformation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTblEmployeeInformation.getFamilyName()).isEqualTo(UPDATED_FAMILY_NAME);
        assertThat(testTblEmployeeInformation.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void patchNonExistingTblEmployeeInformation() throws Exception {
        int databaseSizeBeforeUpdate = tblEmployeeInformationRepository.findAll().size();
        tblEmployeeInformation.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTblEmployeeInformationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tblEmployeeInformation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tblEmployeeInformation))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblEmployeeInformation in the database
        List<TblEmployeeInformation> tblEmployeeInformationList = tblEmployeeInformationRepository.findAll();
        assertThat(tblEmployeeInformationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTblEmployeeInformation() throws Exception {
        int databaseSizeBeforeUpdate = tblEmployeeInformationRepository.findAll().size();
        tblEmployeeInformation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblEmployeeInformationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tblEmployeeInformation))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblEmployeeInformation in the database
        List<TblEmployeeInformation> tblEmployeeInformationList = tblEmployeeInformationRepository.findAll();
        assertThat(tblEmployeeInformationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTblEmployeeInformation() throws Exception {
        int databaseSizeBeforeUpdate = tblEmployeeInformationRepository.findAll().size();
        tblEmployeeInformation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblEmployeeInformationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tblEmployeeInformation))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TblEmployeeInformation in the database
        List<TblEmployeeInformation> tblEmployeeInformationList = tblEmployeeInformationRepository.findAll();
        assertThat(tblEmployeeInformationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTblEmployeeInformation() throws Exception {
        // Initialize the database
        tblEmployeeInformationRepository.saveAndFlush(tblEmployeeInformation);

        int databaseSizeBeforeDelete = tblEmployeeInformationRepository.findAll().size();

        // Delete the tblEmployeeInformation
        restTblEmployeeInformationMockMvc
            .perform(delete(ENTITY_API_URL_ID, tblEmployeeInformation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TblEmployeeInformation> tblEmployeeInformationList = tblEmployeeInformationRepository.findAll();
        assertThat(tblEmployeeInformationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
