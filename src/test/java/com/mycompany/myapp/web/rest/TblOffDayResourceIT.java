package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.TblOffDay;
import com.mycompany.myapp.repository.TblOffDayRepository;
import com.mycompany.myapp.service.criteria.TblOffDayCriteria;
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
 * Integration tests for the {@link TblOffDayResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TblOffDayResourceIT {

    private static final Integer DEFAULT_DAY = 1;
    private static final Integer UPDATED_DAY = 2;
    private static final Integer SMALLER_DAY = 1 - 1;

    private static final Integer DEFAULT_MONTH = 1;
    private static final Integer UPDATED_MONTH = 2;
    private static final Integer SMALLER_MONTH = 1 - 1;

    private static final Integer DEFAULT_YEAR = 1;
    private static final Integer UPDATED_YEAR = 2;
    private static final Integer SMALLER_YEAR = 1 - 1;

    private static final String ENTITY_API_URL = "/api/tbl-off-days";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TblOffDayRepository tblOffDayRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTblOffDayMockMvc;

    private TblOffDay tblOffDay;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TblOffDay createEntity(EntityManager em) {
        TblOffDay tblOffDay = new TblOffDay().day(DEFAULT_DAY).month(DEFAULT_MONTH).year(DEFAULT_YEAR);
        return tblOffDay;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TblOffDay createUpdatedEntity(EntityManager em) {
        TblOffDay tblOffDay = new TblOffDay().day(UPDATED_DAY).month(UPDATED_MONTH).year(UPDATED_YEAR);
        return tblOffDay;
    }

    @BeforeEach
    public void initTest() {
        tblOffDay = createEntity(em);
    }

    @Test
    @Transactional
    void createTblOffDay() throws Exception {
        int databaseSizeBeforeCreate = tblOffDayRepository.findAll().size();
        // Create the TblOffDay
        restTblOffDayMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tblOffDay)))
            .andExpect(status().isCreated());

        // Validate the TblOffDay in the database
        List<TblOffDay> tblOffDayList = tblOffDayRepository.findAll();
        assertThat(tblOffDayList).hasSize(databaseSizeBeforeCreate + 1);
        TblOffDay testTblOffDay = tblOffDayList.get(tblOffDayList.size() - 1);
        assertThat(testTblOffDay.getDay()).isEqualTo(DEFAULT_DAY);
        assertThat(testTblOffDay.getMonth()).isEqualTo(DEFAULT_MONTH);
        assertThat(testTblOffDay.getYear()).isEqualTo(DEFAULT_YEAR);
    }

    @Test
    @Transactional
    void createTblOffDayWithExistingId() throws Exception {
        // Create the TblOffDay with an existing ID
        tblOffDay.setId(1L);

        int databaseSizeBeforeCreate = tblOffDayRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTblOffDayMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tblOffDay)))
            .andExpect(status().isBadRequest());

        // Validate the TblOffDay in the database
        List<TblOffDay> tblOffDayList = tblOffDayRepository.findAll();
        assertThat(tblOffDayList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTblOffDays() throws Exception {
        // Initialize the database
        tblOffDayRepository.saveAndFlush(tblOffDay);

        // Get all the tblOffDayList
        restTblOffDayMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tblOffDay.getId().intValue())))
            .andExpect(jsonPath("$.[*].day").value(hasItem(DEFAULT_DAY)))
            .andExpect(jsonPath("$.[*].month").value(hasItem(DEFAULT_MONTH)))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)));
    }

    @Test
    @Transactional
    void getTblOffDay() throws Exception {
        // Initialize the database
        tblOffDayRepository.saveAndFlush(tblOffDay);

        // Get the tblOffDay
        restTblOffDayMockMvc
            .perform(get(ENTITY_API_URL_ID, tblOffDay.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tblOffDay.getId().intValue()))
            .andExpect(jsonPath("$.day").value(DEFAULT_DAY))
            .andExpect(jsonPath("$.month").value(DEFAULT_MONTH))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR));
    }

    @Test
    @Transactional
    void getTblOffDaysByIdFiltering() throws Exception {
        // Initialize the database
        tblOffDayRepository.saveAndFlush(tblOffDay);

        Long id = tblOffDay.getId();

        defaultTblOffDayShouldBeFound("id.equals=" + id);
        defaultTblOffDayShouldNotBeFound("id.notEquals=" + id);

        defaultTblOffDayShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTblOffDayShouldNotBeFound("id.greaterThan=" + id);

        defaultTblOffDayShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTblOffDayShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTblOffDaysByDayIsEqualToSomething() throws Exception {
        // Initialize the database
        tblOffDayRepository.saveAndFlush(tblOffDay);

        // Get all the tblOffDayList where day equals to DEFAULT_DAY
        defaultTblOffDayShouldBeFound("day.equals=" + DEFAULT_DAY);

        // Get all the tblOffDayList where day equals to UPDATED_DAY
        defaultTblOffDayShouldNotBeFound("day.equals=" + UPDATED_DAY);
    }

    @Test
    @Transactional
    void getAllTblOffDaysByDayIsInShouldWork() throws Exception {
        // Initialize the database
        tblOffDayRepository.saveAndFlush(tblOffDay);

        // Get all the tblOffDayList where day in DEFAULT_DAY or UPDATED_DAY
        defaultTblOffDayShouldBeFound("day.in=" + DEFAULT_DAY + "," + UPDATED_DAY);

        // Get all the tblOffDayList where day equals to UPDATED_DAY
        defaultTblOffDayShouldNotBeFound("day.in=" + UPDATED_DAY);
    }

    @Test
    @Transactional
    void getAllTblOffDaysByDayIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblOffDayRepository.saveAndFlush(tblOffDay);

        // Get all the tblOffDayList where day is not null
        defaultTblOffDayShouldBeFound("day.specified=true");

        // Get all the tblOffDayList where day is null
        defaultTblOffDayShouldNotBeFound("day.specified=false");
    }

    @Test
    @Transactional
    void getAllTblOffDaysByDayIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tblOffDayRepository.saveAndFlush(tblOffDay);

        // Get all the tblOffDayList where day is greater than or equal to DEFAULT_DAY
        defaultTblOffDayShouldBeFound("day.greaterThanOrEqual=" + DEFAULT_DAY);

        // Get all the tblOffDayList where day is greater than or equal to UPDATED_DAY
        defaultTblOffDayShouldNotBeFound("day.greaterThanOrEqual=" + UPDATED_DAY);
    }

    @Test
    @Transactional
    void getAllTblOffDaysByDayIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tblOffDayRepository.saveAndFlush(tblOffDay);

        // Get all the tblOffDayList where day is less than or equal to DEFAULT_DAY
        defaultTblOffDayShouldBeFound("day.lessThanOrEqual=" + DEFAULT_DAY);

        // Get all the tblOffDayList where day is less than or equal to SMALLER_DAY
        defaultTblOffDayShouldNotBeFound("day.lessThanOrEqual=" + SMALLER_DAY);
    }

    @Test
    @Transactional
    void getAllTblOffDaysByDayIsLessThanSomething() throws Exception {
        // Initialize the database
        tblOffDayRepository.saveAndFlush(tblOffDay);

        // Get all the tblOffDayList where day is less than DEFAULT_DAY
        defaultTblOffDayShouldNotBeFound("day.lessThan=" + DEFAULT_DAY);

        // Get all the tblOffDayList where day is less than UPDATED_DAY
        defaultTblOffDayShouldBeFound("day.lessThan=" + UPDATED_DAY);
    }

    @Test
    @Transactional
    void getAllTblOffDaysByDayIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tblOffDayRepository.saveAndFlush(tblOffDay);

        // Get all the tblOffDayList where day is greater than DEFAULT_DAY
        defaultTblOffDayShouldNotBeFound("day.greaterThan=" + DEFAULT_DAY);

        // Get all the tblOffDayList where day is greater than SMALLER_DAY
        defaultTblOffDayShouldBeFound("day.greaterThan=" + SMALLER_DAY);
    }

    @Test
    @Transactional
    void getAllTblOffDaysByMonthIsEqualToSomething() throws Exception {
        // Initialize the database
        tblOffDayRepository.saveAndFlush(tblOffDay);

        // Get all the tblOffDayList where month equals to DEFAULT_MONTH
        defaultTblOffDayShouldBeFound("month.equals=" + DEFAULT_MONTH);

        // Get all the tblOffDayList where month equals to UPDATED_MONTH
        defaultTblOffDayShouldNotBeFound("month.equals=" + UPDATED_MONTH);
    }

    @Test
    @Transactional
    void getAllTblOffDaysByMonthIsInShouldWork() throws Exception {
        // Initialize the database
        tblOffDayRepository.saveAndFlush(tblOffDay);

        // Get all the tblOffDayList where month in DEFAULT_MONTH or UPDATED_MONTH
        defaultTblOffDayShouldBeFound("month.in=" + DEFAULT_MONTH + "," + UPDATED_MONTH);

        // Get all the tblOffDayList where month equals to UPDATED_MONTH
        defaultTblOffDayShouldNotBeFound("month.in=" + UPDATED_MONTH);
    }

    @Test
    @Transactional
    void getAllTblOffDaysByMonthIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblOffDayRepository.saveAndFlush(tblOffDay);

        // Get all the tblOffDayList where month is not null
        defaultTblOffDayShouldBeFound("month.specified=true");

        // Get all the tblOffDayList where month is null
        defaultTblOffDayShouldNotBeFound("month.specified=false");
    }

    @Test
    @Transactional
    void getAllTblOffDaysByMonthIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tblOffDayRepository.saveAndFlush(tblOffDay);

        // Get all the tblOffDayList where month is greater than or equal to DEFAULT_MONTH
        defaultTblOffDayShouldBeFound("month.greaterThanOrEqual=" + DEFAULT_MONTH);

        // Get all the tblOffDayList where month is greater than or equal to UPDATED_MONTH
        defaultTblOffDayShouldNotBeFound("month.greaterThanOrEqual=" + UPDATED_MONTH);
    }

    @Test
    @Transactional
    void getAllTblOffDaysByMonthIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tblOffDayRepository.saveAndFlush(tblOffDay);

        // Get all the tblOffDayList where month is less than or equal to DEFAULT_MONTH
        defaultTblOffDayShouldBeFound("month.lessThanOrEqual=" + DEFAULT_MONTH);

        // Get all the tblOffDayList where month is less than or equal to SMALLER_MONTH
        defaultTblOffDayShouldNotBeFound("month.lessThanOrEqual=" + SMALLER_MONTH);
    }

    @Test
    @Transactional
    void getAllTblOffDaysByMonthIsLessThanSomething() throws Exception {
        // Initialize the database
        tblOffDayRepository.saveAndFlush(tblOffDay);

        // Get all the tblOffDayList where month is less than DEFAULT_MONTH
        defaultTblOffDayShouldNotBeFound("month.lessThan=" + DEFAULT_MONTH);

        // Get all the tblOffDayList where month is less than UPDATED_MONTH
        defaultTblOffDayShouldBeFound("month.lessThan=" + UPDATED_MONTH);
    }

    @Test
    @Transactional
    void getAllTblOffDaysByMonthIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tblOffDayRepository.saveAndFlush(tblOffDay);

        // Get all the tblOffDayList where month is greater than DEFAULT_MONTH
        defaultTblOffDayShouldNotBeFound("month.greaterThan=" + DEFAULT_MONTH);

        // Get all the tblOffDayList where month is greater than SMALLER_MONTH
        defaultTblOffDayShouldBeFound("month.greaterThan=" + SMALLER_MONTH);
    }

    @Test
    @Transactional
    void getAllTblOffDaysByYearIsEqualToSomething() throws Exception {
        // Initialize the database
        tblOffDayRepository.saveAndFlush(tblOffDay);

        // Get all the tblOffDayList where year equals to DEFAULT_YEAR
        defaultTblOffDayShouldBeFound("year.equals=" + DEFAULT_YEAR);

        // Get all the tblOffDayList where year equals to UPDATED_YEAR
        defaultTblOffDayShouldNotBeFound("year.equals=" + UPDATED_YEAR);
    }

    @Test
    @Transactional
    void getAllTblOffDaysByYearIsInShouldWork() throws Exception {
        // Initialize the database
        tblOffDayRepository.saveAndFlush(tblOffDay);

        // Get all the tblOffDayList where year in DEFAULT_YEAR or UPDATED_YEAR
        defaultTblOffDayShouldBeFound("year.in=" + DEFAULT_YEAR + "," + UPDATED_YEAR);

        // Get all the tblOffDayList where year equals to UPDATED_YEAR
        defaultTblOffDayShouldNotBeFound("year.in=" + UPDATED_YEAR);
    }

    @Test
    @Transactional
    void getAllTblOffDaysByYearIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblOffDayRepository.saveAndFlush(tblOffDay);

        // Get all the tblOffDayList where year is not null
        defaultTblOffDayShouldBeFound("year.specified=true");

        // Get all the tblOffDayList where year is null
        defaultTblOffDayShouldNotBeFound("year.specified=false");
    }

    @Test
    @Transactional
    void getAllTblOffDaysByYearIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tblOffDayRepository.saveAndFlush(tblOffDay);

        // Get all the tblOffDayList where year is greater than or equal to DEFAULT_YEAR
        defaultTblOffDayShouldBeFound("year.greaterThanOrEqual=" + DEFAULT_YEAR);

        // Get all the tblOffDayList where year is greater than or equal to UPDATED_YEAR
        defaultTblOffDayShouldNotBeFound("year.greaterThanOrEqual=" + UPDATED_YEAR);
    }

    @Test
    @Transactional
    void getAllTblOffDaysByYearIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tblOffDayRepository.saveAndFlush(tblOffDay);

        // Get all the tblOffDayList where year is less than or equal to DEFAULT_YEAR
        defaultTblOffDayShouldBeFound("year.lessThanOrEqual=" + DEFAULT_YEAR);

        // Get all the tblOffDayList where year is less than or equal to SMALLER_YEAR
        defaultTblOffDayShouldNotBeFound("year.lessThanOrEqual=" + SMALLER_YEAR);
    }

    @Test
    @Transactional
    void getAllTblOffDaysByYearIsLessThanSomething() throws Exception {
        // Initialize the database
        tblOffDayRepository.saveAndFlush(tblOffDay);

        // Get all the tblOffDayList where year is less than DEFAULT_YEAR
        defaultTblOffDayShouldNotBeFound("year.lessThan=" + DEFAULT_YEAR);

        // Get all the tblOffDayList where year is less than UPDATED_YEAR
        defaultTblOffDayShouldBeFound("year.lessThan=" + UPDATED_YEAR);
    }

    @Test
    @Transactional
    void getAllTblOffDaysByYearIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tblOffDayRepository.saveAndFlush(tblOffDay);

        // Get all the tblOffDayList where year is greater than DEFAULT_YEAR
        defaultTblOffDayShouldNotBeFound("year.greaterThan=" + DEFAULT_YEAR);

        // Get all the tblOffDayList where year is greater than SMALLER_YEAR
        defaultTblOffDayShouldBeFound("year.greaterThan=" + SMALLER_YEAR);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTblOffDayShouldBeFound(String filter) throws Exception {
        restTblOffDayMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tblOffDay.getId().intValue())))
            .andExpect(jsonPath("$.[*].day").value(hasItem(DEFAULT_DAY)))
            .andExpect(jsonPath("$.[*].month").value(hasItem(DEFAULT_MONTH)))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)));

        // Check, that the count call also returns 1
        restTblOffDayMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTblOffDayShouldNotBeFound(String filter) throws Exception {
        restTblOffDayMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTblOffDayMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTblOffDay() throws Exception {
        // Get the tblOffDay
        restTblOffDayMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTblOffDay() throws Exception {
        // Initialize the database
        tblOffDayRepository.saveAndFlush(tblOffDay);

        int databaseSizeBeforeUpdate = tblOffDayRepository.findAll().size();

        // Update the tblOffDay
        TblOffDay updatedTblOffDay = tblOffDayRepository.findById(tblOffDay.getId()).get();
        // Disconnect from session so that the updates on updatedTblOffDay are not directly saved in db
        em.detach(updatedTblOffDay);
        updatedTblOffDay.day(UPDATED_DAY).month(UPDATED_MONTH).year(UPDATED_YEAR);

        restTblOffDayMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTblOffDay.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTblOffDay))
            )
            .andExpect(status().isOk());

        // Validate the TblOffDay in the database
        List<TblOffDay> tblOffDayList = tblOffDayRepository.findAll();
        assertThat(tblOffDayList).hasSize(databaseSizeBeforeUpdate);
        TblOffDay testTblOffDay = tblOffDayList.get(tblOffDayList.size() - 1);
        assertThat(testTblOffDay.getDay()).isEqualTo(UPDATED_DAY);
        assertThat(testTblOffDay.getMonth()).isEqualTo(UPDATED_MONTH);
        assertThat(testTblOffDay.getYear()).isEqualTo(UPDATED_YEAR);
    }

    @Test
    @Transactional
    void putNonExistingTblOffDay() throws Exception {
        int databaseSizeBeforeUpdate = tblOffDayRepository.findAll().size();
        tblOffDay.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTblOffDayMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tblOffDay.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tblOffDay))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblOffDay in the database
        List<TblOffDay> tblOffDayList = tblOffDayRepository.findAll();
        assertThat(tblOffDayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTblOffDay() throws Exception {
        int databaseSizeBeforeUpdate = tblOffDayRepository.findAll().size();
        tblOffDay.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblOffDayMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tblOffDay))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblOffDay in the database
        List<TblOffDay> tblOffDayList = tblOffDayRepository.findAll();
        assertThat(tblOffDayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTblOffDay() throws Exception {
        int databaseSizeBeforeUpdate = tblOffDayRepository.findAll().size();
        tblOffDay.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblOffDayMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tblOffDay)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TblOffDay in the database
        List<TblOffDay> tblOffDayList = tblOffDayRepository.findAll();
        assertThat(tblOffDayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTblOffDayWithPatch() throws Exception {
        // Initialize the database
        tblOffDayRepository.saveAndFlush(tblOffDay);

        int databaseSizeBeforeUpdate = tblOffDayRepository.findAll().size();

        // Update the tblOffDay using partial update
        TblOffDay partialUpdatedTblOffDay = new TblOffDay();
        partialUpdatedTblOffDay.setId(tblOffDay.getId());

        partialUpdatedTblOffDay.month(UPDATED_MONTH).year(UPDATED_YEAR);

        restTblOffDayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTblOffDay.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTblOffDay))
            )
            .andExpect(status().isOk());

        // Validate the TblOffDay in the database
        List<TblOffDay> tblOffDayList = tblOffDayRepository.findAll();
        assertThat(tblOffDayList).hasSize(databaseSizeBeforeUpdate);
        TblOffDay testTblOffDay = tblOffDayList.get(tblOffDayList.size() - 1);
        assertThat(testTblOffDay.getDay()).isEqualTo(DEFAULT_DAY);
        assertThat(testTblOffDay.getMonth()).isEqualTo(UPDATED_MONTH);
        assertThat(testTblOffDay.getYear()).isEqualTo(UPDATED_YEAR);
    }

    @Test
    @Transactional
    void fullUpdateTblOffDayWithPatch() throws Exception {
        // Initialize the database
        tblOffDayRepository.saveAndFlush(tblOffDay);

        int databaseSizeBeforeUpdate = tblOffDayRepository.findAll().size();

        // Update the tblOffDay using partial update
        TblOffDay partialUpdatedTblOffDay = new TblOffDay();
        partialUpdatedTblOffDay.setId(tblOffDay.getId());

        partialUpdatedTblOffDay.day(UPDATED_DAY).month(UPDATED_MONTH).year(UPDATED_YEAR);

        restTblOffDayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTblOffDay.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTblOffDay))
            )
            .andExpect(status().isOk());

        // Validate the TblOffDay in the database
        List<TblOffDay> tblOffDayList = tblOffDayRepository.findAll();
        assertThat(tblOffDayList).hasSize(databaseSizeBeforeUpdate);
        TblOffDay testTblOffDay = tblOffDayList.get(tblOffDayList.size() - 1);
        assertThat(testTblOffDay.getDay()).isEqualTo(UPDATED_DAY);
        assertThat(testTblOffDay.getMonth()).isEqualTo(UPDATED_MONTH);
        assertThat(testTblOffDay.getYear()).isEqualTo(UPDATED_YEAR);
    }

    @Test
    @Transactional
    void patchNonExistingTblOffDay() throws Exception {
        int databaseSizeBeforeUpdate = tblOffDayRepository.findAll().size();
        tblOffDay.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTblOffDayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tblOffDay.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tblOffDay))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblOffDay in the database
        List<TblOffDay> tblOffDayList = tblOffDayRepository.findAll();
        assertThat(tblOffDayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTblOffDay() throws Exception {
        int databaseSizeBeforeUpdate = tblOffDayRepository.findAll().size();
        tblOffDay.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblOffDayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tblOffDay))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblOffDay in the database
        List<TblOffDay> tblOffDayList = tblOffDayRepository.findAll();
        assertThat(tblOffDayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTblOffDay() throws Exception {
        int databaseSizeBeforeUpdate = tblOffDayRepository.findAll().size();
        tblOffDay.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblOffDayMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tblOffDay))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TblOffDay in the database
        List<TblOffDay> tblOffDayList = tblOffDayRepository.findAll();
        assertThat(tblOffDayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTblOffDay() throws Exception {
        // Initialize the database
        tblOffDayRepository.saveAndFlush(tblOffDay);

        int databaseSizeBeforeDelete = tblOffDayRepository.findAll().size();

        // Delete the tblOffDay
        restTblOffDayMockMvc
            .perform(delete(ENTITY_API_URL_ID, tblOffDay.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TblOffDay> tblOffDayList = tblOffDayRepository.findAll();
        assertThat(tblOffDayList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
