package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.TblWorkingHours;
import com.mycompany.myapp.repository.TblWorkingHoursRepository;
import com.mycompany.myapp.service.criteria.TblWorkingHoursCriteria;
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
 * Integration tests for the {@link TblWorkingHoursResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TblWorkingHoursResourceIT {

    private static final String DEFAULT_DAY = "AAAAAAAAAA";
    private static final String UPDATED_DAY = "BBBBBBBBBB";

    private static final Integer DEFAULT_START_TIME_HOUR = 1;
    private static final Integer UPDATED_START_TIME_HOUR = 2;
    private static final Integer SMALLER_START_TIME_HOUR = 1 - 1;

    private static final Integer DEFAULT_START_TIME_MINUTE = 1;
    private static final Integer UPDATED_START_TIME_MINUTE = 2;
    private static final Integer SMALLER_START_TIME_MINUTE = 1 - 1;

    private static final Integer DEFAULT_END_TIME_HOUR = 1;
    private static final Integer UPDATED_END_TIME_HOUR = 2;
    private static final Integer SMALLER_END_TIME_HOUR = 1 - 1;

    private static final Integer DEFAULT_END_TIME_MINUTE = 1;
    private static final Integer UPDATED_END_TIME_MINUTE = 2;
    private static final Integer SMALLER_END_TIME_MINUTE = 1 - 1;

    private static final String ENTITY_API_URL = "/api/tbl-working-hours";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TblWorkingHoursRepository tblWorkingHoursRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTblWorkingHoursMockMvc;

    private TblWorkingHours tblWorkingHours;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TblWorkingHours createEntity(EntityManager em) {
        TblWorkingHours tblWorkingHours = new TblWorkingHours()
            .day(DEFAULT_DAY)
            .startTimeHour(DEFAULT_START_TIME_HOUR)
            .startTimeMinute(DEFAULT_START_TIME_MINUTE)
            .endTimeHour(DEFAULT_END_TIME_HOUR)
            .endTimeMinute(DEFAULT_END_TIME_MINUTE);
        return tblWorkingHours;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TblWorkingHours createUpdatedEntity(EntityManager em) {
        TblWorkingHours tblWorkingHours = new TblWorkingHours()
            .day(UPDATED_DAY)
            .startTimeHour(UPDATED_START_TIME_HOUR)
            .startTimeMinute(UPDATED_START_TIME_MINUTE)
            .endTimeHour(UPDATED_END_TIME_HOUR)
            .endTimeMinute(UPDATED_END_TIME_MINUTE);
        return tblWorkingHours;
    }

    @BeforeEach
    public void initTest() {
        tblWorkingHours = createEntity(em);
    }

    @Test
    @Transactional
    void createTblWorkingHours() throws Exception {
        int databaseSizeBeforeCreate = tblWorkingHoursRepository.findAll().size();
        // Create the TblWorkingHours
        restTblWorkingHoursMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tblWorkingHours))
            )
            .andExpect(status().isCreated());

        // Validate the TblWorkingHours in the database
        List<TblWorkingHours> tblWorkingHoursList = tblWorkingHoursRepository.findAll();
        assertThat(tblWorkingHoursList).hasSize(databaseSizeBeforeCreate + 1);
        TblWorkingHours testTblWorkingHours = tblWorkingHoursList.get(tblWorkingHoursList.size() - 1);
        assertThat(testTblWorkingHours.getDay()).isEqualTo(DEFAULT_DAY);
        assertThat(testTblWorkingHours.getStartTimeHour()).isEqualTo(DEFAULT_START_TIME_HOUR);
        assertThat(testTblWorkingHours.getStartTimeMinute()).isEqualTo(DEFAULT_START_TIME_MINUTE);
        assertThat(testTblWorkingHours.getEndTimeHour()).isEqualTo(DEFAULT_END_TIME_HOUR);
        assertThat(testTblWorkingHours.getEndTimeMinute()).isEqualTo(DEFAULT_END_TIME_MINUTE);
    }

    @Test
    @Transactional
    void createTblWorkingHoursWithExistingId() throws Exception {
        // Create the TblWorkingHours with an existing ID
        tblWorkingHours.setId(1L);

        int databaseSizeBeforeCreate = tblWorkingHoursRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTblWorkingHoursMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tblWorkingHours))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblWorkingHours in the database
        List<TblWorkingHours> tblWorkingHoursList = tblWorkingHoursRepository.findAll();
        assertThat(tblWorkingHoursList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTblWorkingHours() throws Exception {
        // Initialize the database
        tblWorkingHoursRepository.saveAndFlush(tblWorkingHours);

        // Get all the tblWorkingHoursList
        restTblWorkingHoursMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tblWorkingHours.getId().intValue())))
            .andExpect(jsonPath("$.[*].day").value(hasItem(DEFAULT_DAY)))
            .andExpect(jsonPath("$.[*].startTimeHour").value(hasItem(DEFAULT_START_TIME_HOUR)))
            .andExpect(jsonPath("$.[*].startTimeMinute").value(hasItem(DEFAULT_START_TIME_MINUTE)))
            .andExpect(jsonPath("$.[*].endTimeHour").value(hasItem(DEFAULT_END_TIME_HOUR)))
            .andExpect(jsonPath("$.[*].endTimeMinute").value(hasItem(DEFAULT_END_TIME_MINUTE)));
    }

    @Test
    @Transactional
    void getTblWorkingHours() throws Exception {
        // Initialize the database
        tblWorkingHoursRepository.saveAndFlush(tblWorkingHours);

        // Get the tblWorkingHours
        restTblWorkingHoursMockMvc
            .perform(get(ENTITY_API_URL_ID, tblWorkingHours.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tblWorkingHours.getId().intValue()))
            .andExpect(jsonPath("$.day").value(DEFAULT_DAY))
            .andExpect(jsonPath("$.startTimeHour").value(DEFAULT_START_TIME_HOUR))
            .andExpect(jsonPath("$.startTimeMinute").value(DEFAULT_START_TIME_MINUTE))
            .andExpect(jsonPath("$.endTimeHour").value(DEFAULT_END_TIME_HOUR))
            .andExpect(jsonPath("$.endTimeMinute").value(DEFAULT_END_TIME_MINUTE));
    }

    @Test
    @Transactional
    void getTblWorkingHoursByIdFiltering() throws Exception {
        // Initialize the database
        tblWorkingHoursRepository.saveAndFlush(tblWorkingHours);

        Long id = tblWorkingHours.getId();

        defaultTblWorkingHoursShouldBeFound("id.equals=" + id);
        defaultTblWorkingHoursShouldNotBeFound("id.notEquals=" + id);

        defaultTblWorkingHoursShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTblWorkingHoursShouldNotBeFound("id.greaterThan=" + id);

        defaultTblWorkingHoursShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTblWorkingHoursShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTblWorkingHoursByDayIsEqualToSomething() throws Exception {
        // Initialize the database
        tblWorkingHoursRepository.saveAndFlush(tblWorkingHours);

        // Get all the tblWorkingHoursList where day equals to DEFAULT_DAY
        defaultTblWorkingHoursShouldBeFound("day.equals=" + DEFAULT_DAY);

        // Get all the tblWorkingHoursList where day equals to UPDATED_DAY
        defaultTblWorkingHoursShouldNotBeFound("day.equals=" + UPDATED_DAY);
    }

    @Test
    @Transactional
    void getAllTblWorkingHoursByDayIsInShouldWork() throws Exception {
        // Initialize the database
        tblWorkingHoursRepository.saveAndFlush(tblWorkingHours);

        // Get all the tblWorkingHoursList where day in DEFAULT_DAY or UPDATED_DAY
        defaultTblWorkingHoursShouldBeFound("day.in=" + DEFAULT_DAY + "," + UPDATED_DAY);

        // Get all the tblWorkingHoursList where day equals to UPDATED_DAY
        defaultTblWorkingHoursShouldNotBeFound("day.in=" + UPDATED_DAY);
    }

    @Test
    @Transactional
    void getAllTblWorkingHoursByDayIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblWorkingHoursRepository.saveAndFlush(tblWorkingHours);

        // Get all the tblWorkingHoursList where day is not null
        defaultTblWorkingHoursShouldBeFound("day.specified=true");

        // Get all the tblWorkingHoursList where day is null
        defaultTblWorkingHoursShouldNotBeFound("day.specified=false");
    }

    @Test
    @Transactional
    void getAllTblWorkingHoursByDayContainsSomething() throws Exception {
        // Initialize the database
        tblWorkingHoursRepository.saveAndFlush(tblWorkingHours);

        // Get all the tblWorkingHoursList where day contains DEFAULT_DAY
        defaultTblWorkingHoursShouldBeFound("day.contains=" + DEFAULT_DAY);

        // Get all the tblWorkingHoursList where day contains UPDATED_DAY
        defaultTblWorkingHoursShouldNotBeFound("day.contains=" + UPDATED_DAY);
    }

    @Test
    @Transactional
    void getAllTblWorkingHoursByDayNotContainsSomething() throws Exception {
        // Initialize the database
        tblWorkingHoursRepository.saveAndFlush(tblWorkingHours);

        // Get all the tblWorkingHoursList where day does not contain DEFAULT_DAY
        defaultTblWorkingHoursShouldNotBeFound("day.doesNotContain=" + DEFAULT_DAY);

        // Get all the tblWorkingHoursList where day does not contain UPDATED_DAY
        defaultTblWorkingHoursShouldBeFound("day.doesNotContain=" + UPDATED_DAY);
    }

    @Test
    @Transactional
    void getAllTblWorkingHoursByStartTimeHourIsEqualToSomething() throws Exception {
        // Initialize the database
        tblWorkingHoursRepository.saveAndFlush(tblWorkingHours);

        // Get all the tblWorkingHoursList where startTimeHour equals to DEFAULT_START_TIME_HOUR
        defaultTblWorkingHoursShouldBeFound("startTimeHour.equals=" + DEFAULT_START_TIME_HOUR);

        // Get all the tblWorkingHoursList where startTimeHour equals to UPDATED_START_TIME_HOUR
        defaultTblWorkingHoursShouldNotBeFound("startTimeHour.equals=" + UPDATED_START_TIME_HOUR);
    }

    @Test
    @Transactional
    void getAllTblWorkingHoursByStartTimeHourIsInShouldWork() throws Exception {
        // Initialize the database
        tblWorkingHoursRepository.saveAndFlush(tblWorkingHours);

        // Get all the tblWorkingHoursList where startTimeHour in DEFAULT_START_TIME_HOUR or UPDATED_START_TIME_HOUR
        defaultTblWorkingHoursShouldBeFound("startTimeHour.in=" + DEFAULT_START_TIME_HOUR + "," + UPDATED_START_TIME_HOUR);

        // Get all the tblWorkingHoursList where startTimeHour equals to UPDATED_START_TIME_HOUR
        defaultTblWorkingHoursShouldNotBeFound("startTimeHour.in=" + UPDATED_START_TIME_HOUR);
    }

    @Test
    @Transactional
    void getAllTblWorkingHoursByStartTimeHourIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblWorkingHoursRepository.saveAndFlush(tblWorkingHours);

        // Get all the tblWorkingHoursList where startTimeHour is not null
        defaultTblWorkingHoursShouldBeFound("startTimeHour.specified=true");

        // Get all the tblWorkingHoursList where startTimeHour is null
        defaultTblWorkingHoursShouldNotBeFound("startTimeHour.specified=false");
    }

    @Test
    @Transactional
    void getAllTblWorkingHoursByStartTimeHourIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tblWorkingHoursRepository.saveAndFlush(tblWorkingHours);

        // Get all the tblWorkingHoursList where startTimeHour is greater than or equal to DEFAULT_START_TIME_HOUR
        defaultTblWorkingHoursShouldBeFound("startTimeHour.greaterThanOrEqual=" + DEFAULT_START_TIME_HOUR);

        // Get all the tblWorkingHoursList where startTimeHour is greater than or equal to UPDATED_START_TIME_HOUR
        defaultTblWorkingHoursShouldNotBeFound("startTimeHour.greaterThanOrEqual=" + UPDATED_START_TIME_HOUR);
    }

    @Test
    @Transactional
    void getAllTblWorkingHoursByStartTimeHourIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tblWorkingHoursRepository.saveAndFlush(tblWorkingHours);

        // Get all the tblWorkingHoursList where startTimeHour is less than or equal to DEFAULT_START_TIME_HOUR
        defaultTblWorkingHoursShouldBeFound("startTimeHour.lessThanOrEqual=" + DEFAULT_START_TIME_HOUR);

        // Get all the tblWorkingHoursList where startTimeHour is less than or equal to SMALLER_START_TIME_HOUR
        defaultTblWorkingHoursShouldNotBeFound("startTimeHour.lessThanOrEqual=" + SMALLER_START_TIME_HOUR);
    }

    @Test
    @Transactional
    void getAllTblWorkingHoursByStartTimeHourIsLessThanSomething() throws Exception {
        // Initialize the database
        tblWorkingHoursRepository.saveAndFlush(tblWorkingHours);

        // Get all the tblWorkingHoursList where startTimeHour is less than DEFAULT_START_TIME_HOUR
        defaultTblWorkingHoursShouldNotBeFound("startTimeHour.lessThan=" + DEFAULT_START_TIME_HOUR);

        // Get all the tblWorkingHoursList where startTimeHour is less than UPDATED_START_TIME_HOUR
        defaultTblWorkingHoursShouldBeFound("startTimeHour.lessThan=" + UPDATED_START_TIME_HOUR);
    }

    @Test
    @Transactional
    void getAllTblWorkingHoursByStartTimeHourIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tblWorkingHoursRepository.saveAndFlush(tblWorkingHours);

        // Get all the tblWorkingHoursList where startTimeHour is greater than DEFAULT_START_TIME_HOUR
        defaultTblWorkingHoursShouldNotBeFound("startTimeHour.greaterThan=" + DEFAULT_START_TIME_HOUR);

        // Get all the tblWorkingHoursList where startTimeHour is greater than SMALLER_START_TIME_HOUR
        defaultTblWorkingHoursShouldBeFound("startTimeHour.greaterThan=" + SMALLER_START_TIME_HOUR);
    }

    @Test
    @Transactional
    void getAllTblWorkingHoursByStartTimeMinuteIsEqualToSomething() throws Exception {
        // Initialize the database
        tblWorkingHoursRepository.saveAndFlush(tblWorkingHours);

        // Get all the tblWorkingHoursList where startTimeMinute equals to DEFAULT_START_TIME_MINUTE
        defaultTblWorkingHoursShouldBeFound("startTimeMinute.equals=" + DEFAULT_START_TIME_MINUTE);

        // Get all the tblWorkingHoursList where startTimeMinute equals to UPDATED_START_TIME_MINUTE
        defaultTblWorkingHoursShouldNotBeFound("startTimeMinute.equals=" + UPDATED_START_TIME_MINUTE);
    }

    @Test
    @Transactional
    void getAllTblWorkingHoursByStartTimeMinuteIsInShouldWork() throws Exception {
        // Initialize the database
        tblWorkingHoursRepository.saveAndFlush(tblWorkingHours);

        // Get all the tblWorkingHoursList where startTimeMinute in DEFAULT_START_TIME_MINUTE or UPDATED_START_TIME_MINUTE
        defaultTblWorkingHoursShouldBeFound("startTimeMinute.in=" + DEFAULT_START_TIME_MINUTE + "," + UPDATED_START_TIME_MINUTE);

        // Get all the tblWorkingHoursList where startTimeMinute equals to UPDATED_START_TIME_MINUTE
        defaultTblWorkingHoursShouldNotBeFound("startTimeMinute.in=" + UPDATED_START_TIME_MINUTE);
    }

    @Test
    @Transactional
    void getAllTblWorkingHoursByStartTimeMinuteIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblWorkingHoursRepository.saveAndFlush(tblWorkingHours);

        // Get all the tblWorkingHoursList where startTimeMinute is not null
        defaultTblWorkingHoursShouldBeFound("startTimeMinute.specified=true");

        // Get all the tblWorkingHoursList where startTimeMinute is null
        defaultTblWorkingHoursShouldNotBeFound("startTimeMinute.specified=false");
    }

    @Test
    @Transactional
    void getAllTblWorkingHoursByStartTimeMinuteIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tblWorkingHoursRepository.saveAndFlush(tblWorkingHours);

        // Get all the tblWorkingHoursList where startTimeMinute is greater than or equal to DEFAULT_START_TIME_MINUTE
        defaultTblWorkingHoursShouldBeFound("startTimeMinute.greaterThanOrEqual=" + DEFAULT_START_TIME_MINUTE);

        // Get all the tblWorkingHoursList where startTimeMinute is greater than or equal to UPDATED_START_TIME_MINUTE
        defaultTblWorkingHoursShouldNotBeFound("startTimeMinute.greaterThanOrEqual=" + UPDATED_START_TIME_MINUTE);
    }

    @Test
    @Transactional
    void getAllTblWorkingHoursByStartTimeMinuteIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tblWorkingHoursRepository.saveAndFlush(tblWorkingHours);

        // Get all the tblWorkingHoursList where startTimeMinute is less than or equal to DEFAULT_START_TIME_MINUTE
        defaultTblWorkingHoursShouldBeFound("startTimeMinute.lessThanOrEqual=" + DEFAULT_START_TIME_MINUTE);

        // Get all the tblWorkingHoursList where startTimeMinute is less than or equal to SMALLER_START_TIME_MINUTE
        defaultTblWorkingHoursShouldNotBeFound("startTimeMinute.lessThanOrEqual=" + SMALLER_START_TIME_MINUTE);
    }

    @Test
    @Transactional
    void getAllTblWorkingHoursByStartTimeMinuteIsLessThanSomething() throws Exception {
        // Initialize the database
        tblWorkingHoursRepository.saveAndFlush(tblWorkingHours);

        // Get all the tblWorkingHoursList where startTimeMinute is less than DEFAULT_START_TIME_MINUTE
        defaultTblWorkingHoursShouldNotBeFound("startTimeMinute.lessThan=" + DEFAULT_START_TIME_MINUTE);

        // Get all the tblWorkingHoursList where startTimeMinute is less than UPDATED_START_TIME_MINUTE
        defaultTblWorkingHoursShouldBeFound("startTimeMinute.lessThan=" + UPDATED_START_TIME_MINUTE);
    }

    @Test
    @Transactional
    void getAllTblWorkingHoursByStartTimeMinuteIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tblWorkingHoursRepository.saveAndFlush(tblWorkingHours);

        // Get all the tblWorkingHoursList where startTimeMinute is greater than DEFAULT_START_TIME_MINUTE
        defaultTblWorkingHoursShouldNotBeFound("startTimeMinute.greaterThan=" + DEFAULT_START_TIME_MINUTE);

        // Get all the tblWorkingHoursList where startTimeMinute is greater than SMALLER_START_TIME_MINUTE
        defaultTblWorkingHoursShouldBeFound("startTimeMinute.greaterThan=" + SMALLER_START_TIME_MINUTE);
    }

    @Test
    @Transactional
    void getAllTblWorkingHoursByEndTimeHourIsEqualToSomething() throws Exception {
        // Initialize the database
        tblWorkingHoursRepository.saveAndFlush(tblWorkingHours);

        // Get all the tblWorkingHoursList where endTimeHour equals to DEFAULT_END_TIME_HOUR
        defaultTblWorkingHoursShouldBeFound("endTimeHour.equals=" + DEFAULT_END_TIME_HOUR);

        // Get all the tblWorkingHoursList where endTimeHour equals to UPDATED_END_TIME_HOUR
        defaultTblWorkingHoursShouldNotBeFound("endTimeHour.equals=" + UPDATED_END_TIME_HOUR);
    }

    @Test
    @Transactional
    void getAllTblWorkingHoursByEndTimeHourIsInShouldWork() throws Exception {
        // Initialize the database
        tblWorkingHoursRepository.saveAndFlush(tblWorkingHours);

        // Get all the tblWorkingHoursList where endTimeHour in DEFAULT_END_TIME_HOUR or UPDATED_END_TIME_HOUR
        defaultTblWorkingHoursShouldBeFound("endTimeHour.in=" + DEFAULT_END_TIME_HOUR + "," + UPDATED_END_TIME_HOUR);

        // Get all the tblWorkingHoursList where endTimeHour equals to UPDATED_END_TIME_HOUR
        defaultTblWorkingHoursShouldNotBeFound("endTimeHour.in=" + UPDATED_END_TIME_HOUR);
    }

    @Test
    @Transactional
    void getAllTblWorkingHoursByEndTimeHourIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblWorkingHoursRepository.saveAndFlush(tblWorkingHours);

        // Get all the tblWorkingHoursList where endTimeHour is not null
        defaultTblWorkingHoursShouldBeFound("endTimeHour.specified=true");

        // Get all the tblWorkingHoursList where endTimeHour is null
        defaultTblWorkingHoursShouldNotBeFound("endTimeHour.specified=false");
    }

    @Test
    @Transactional
    void getAllTblWorkingHoursByEndTimeHourIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tblWorkingHoursRepository.saveAndFlush(tblWorkingHours);

        // Get all the tblWorkingHoursList where endTimeHour is greater than or equal to DEFAULT_END_TIME_HOUR
        defaultTblWorkingHoursShouldBeFound("endTimeHour.greaterThanOrEqual=" + DEFAULT_END_TIME_HOUR);

        // Get all the tblWorkingHoursList where endTimeHour is greater than or equal to UPDATED_END_TIME_HOUR
        defaultTblWorkingHoursShouldNotBeFound("endTimeHour.greaterThanOrEqual=" + UPDATED_END_TIME_HOUR);
    }

    @Test
    @Transactional
    void getAllTblWorkingHoursByEndTimeHourIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tblWorkingHoursRepository.saveAndFlush(tblWorkingHours);

        // Get all the tblWorkingHoursList where endTimeHour is less than or equal to DEFAULT_END_TIME_HOUR
        defaultTblWorkingHoursShouldBeFound("endTimeHour.lessThanOrEqual=" + DEFAULT_END_TIME_HOUR);

        // Get all the tblWorkingHoursList where endTimeHour is less than or equal to SMALLER_END_TIME_HOUR
        defaultTblWorkingHoursShouldNotBeFound("endTimeHour.lessThanOrEqual=" + SMALLER_END_TIME_HOUR);
    }

    @Test
    @Transactional
    void getAllTblWorkingHoursByEndTimeHourIsLessThanSomething() throws Exception {
        // Initialize the database
        tblWorkingHoursRepository.saveAndFlush(tblWorkingHours);

        // Get all the tblWorkingHoursList where endTimeHour is less than DEFAULT_END_TIME_HOUR
        defaultTblWorkingHoursShouldNotBeFound("endTimeHour.lessThan=" + DEFAULT_END_TIME_HOUR);

        // Get all the tblWorkingHoursList where endTimeHour is less than UPDATED_END_TIME_HOUR
        defaultTblWorkingHoursShouldBeFound("endTimeHour.lessThan=" + UPDATED_END_TIME_HOUR);
    }

    @Test
    @Transactional
    void getAllTblWorkingHoursByEndTimeHourIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tblWorkingHoursRepository.saveAndFlush(tblWorkingHours);

        // Get all the tblWorkingHoursList where endTimeHour is greater than DEFAULT_END_TIME_HOUR
        defaultTblWorkingHoursShouldNotBeFound("endTimeHour.greaterThan=" + DEFAULT_END_TIME_HOUR);

        // Get all the tblWorkingHoursList where endTimeHour is greater than SMALLER_END_TIME_HOUR
        defaultTblWorkingHoursShouldBeFound("endTimeHour.greaterThan=" + SMALLER_END_TIME_HOUR);
    }

    @Test
    @Transactional
    void getAllTblWorkingHoursByEndTimeMinuteIsEqualToSomething() throws Exception {
        // Initialize the database
        tblWorkingHoursRepository.saveAndFlush(tblWorkingHours);

        // Get all the tblWorkingHoursList where endTimeMinute equals to DEFAULT_END_TIME_MINUTE
        defaultTblWorkingHoursShouldBeFound("endTimeMinute.equals=" + DEFAULT_END_TIME_MINUTE);

        // Get all the tblWorkingHoursList where endTimeMinute equals to UPDATED_END_TIME_MINUTE
        defaultTblWorkingHoursShouldNotBeFound("endTimeMinute.equals=" + UPDATED_END_TIME_MINUTE);
    }

    @Test
    @Transactional
    void getAllTblWorkingHoursByEndTimeMinuteIsInShouldWork() throws Exception {
        // Initialize the database
        tblWorkingHoursRepository.saveAndFlush(tblWorkingHours);

        // Get all the tblWorkingHoursList where endTimeMinute in DEFAULT_END_TIME_MINUTE or UPDATED_END_TIME_MINUTE
        defaultTblWorkingHoursShouldBeFound("endTimeMinute.in=" + DEFAULT_END_TIME_MINUTE + "," + UPDATED_END_TIME_MINUTE);

        // Get all the tblWorkingHoursList where endTimeMinute equals to UPDATED_END_TIME_MINUTE
        defaultTblWorkingHoursShouldNotBeFound("endTimeMinute.in=" + UPDATED_END_TIME_MINUTE);
    }

    @Test
    @Transactional
    void getAllTblWorkingHoursByEndTimeMinuteIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblWorkingHoursRepository.saveAndFlush(tblWorkingHours);

        // Get all the tblWorkingHoursList where endTimeMinute is not null
        defaultTblWorkingHoursShouldBeFound("endTimeMinute.specified=true");

        // Get all the tblWorkingHoursList where endTimeMinute is null
        defaultTblWorkingHoursShouldNotBeFound("endTimeMinute.specified=false");
    }

    @Test
    @Transactional
    void getAllTblWorkingHoursByEndTimeMinuteIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tblWorkingHoursRepository.saveAndFlush(tblWorkingHours);

        // Get all the tblWorkingHoursList where endTimeMinute is greater than or equal to DEFAULT_END_TIME_MINUTE
        defaultTblWorkingHoursShouldBeFound("endTimeMinute.greaterThanOrEqual=" + DEFAULT_END_TIME_MINUTE);

        // Get all the tblWorkingHoursList where endTimeMinute is greater than or equal to UPDATED_END_TIME_MINUTE
        defaultTblWorkingHoursShouldNotBeFound("endTimeMinute.greaterThanOrEqual=" + UPDATED_END_TIME_MINUTE);
    }

    @Test
    @Transactional
    void getAllTblWorkingHoursByEndTimeMinuteIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tblWorkingHoursRepository.saveAndFlush(tblWorkingHours);

        // Get all the tblWorkingHoursList where endTimeMinute is less than or equal to DEFAULT_END_TIME_MINUTE
        defaultTblWorkingHoursShouldBeFound("endTimeMinute.lessThanOrEqual=" + DEFAULT_END_TIME_MINUTE);

        // Get all the tblWorkingHoursList where endTimeMinute is less than or equal to SMALLER_END_TIME_MINUTE
        defaultTblWorkingHoursShouldNotBeFound("endTimeMinute.lessThanOrEqual=" + SMALLER_END_TIME_MINUTE);
    }

    @Test
    @Transactional
    void getAllTblWorkingHoursByEndTimeMinuteIsLessThanSomething() throws Exception {
        // Initialize the database
        tblWorkingHoursRepository.saveAndFlush(tblWorkingHours);

        // Get all the tblWorkingHoursList where endTimeMinute is less than DEFAULT_END_TIME_MINUTE
        defaultTblWorkingHoursShouldNotBeFound("endTimeMinute.lessThan=" + DEFAULT_END_TIME_MINUTE);

        // Get all the tblWorkingHoursList where endTimeMinute is less than UPDATED_END_TIME_MINUTE
        defaultTblWorkingHoursShouldBeFound("endTimeMinute.lessThan=" + UPDATED_END_TIME_MINUTE);
    }

    @Test
    @Transactional
    void getAllTblWorkingHoursByEndTimeMinuteIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tblWorkingHoursRepository.saveAndFlush(tblWorkingHours);

        // Get all the tblWorkingHoursList where endTimeMinute is greater than DEFAULT_END_TIME_MINUTE
        defaultTblWorkingHoursShouldNotBeFound("endTimeMinute.greaterThan=" + DEFAULT_END_TIME_MINUTE);

        // Get all the tblWorkingHoursList where endTimeMinute is greater than SMALLER_END_TIME_MINUTE
        defaultTblWorkingHoursShouldBeFound("endTimeMinute.greaterThan=" + SMALLER_END_TIME_MINUTE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTblWorkingHoursShouldBeFound(String filter) throws Exception {
        restTblWorkingHoursMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tblWorkingHours.getId().intValue())))
            .andExpect(jsonPath("$.[*].day").value(hasItem(DEFAULT_DAY)))
            .andExpect(jsonPath("$.[*].startTimeHour").value(hasItem(DEFAULT_START_TIME_HOUR)))
            .andExpect(jsonPath("$.[*].startTimeMinute").value(hasItem(DEFAULT_START_TIME_MINUTE)))
            .andExpect(jsonPath("$.[*].endTimeHour").value(hasItem(DEFAULT_END_TIME_HOUR)))
            .andExpect(jsonPath("$.[*].endTimeMinute").value(hasItem(DEFAULT_END_TIME_MINUTE)));

        // Check, that the count call also returns 1
        restTblWorkingHoursMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTblWorkingHoursShouldNotBeFound(String filter) throws Exception {
        restTblWorkingHoursMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTblWorkingHoursMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTblWorkingHours() throws Exception {
        // Get the tblWorkingHours
        restTblWorkingHoursMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTblWorkingHours() throws Exception {
        // Initialize the database
        tblWorkingHoursRepository.saveAndFlush(tblWorkingHours);

        int databaseSizeBeforeUpdate = tblWorkingHoursRepository.findAll().size();

        // Update the tblWorkingHours
        TblWorkingHours updatedTblWorkingHours = tblWorkingHoursRepository.findById(tblWorkingHours.getId()).get();
        // Disconnect from session so that the updates on updatedTblWorkingHours are not directly saved in db
        em.detach(updatedTblWorkingHours);
        updatedTblWorkingHours
            .day(UPDATED_DAY)
            .startTimeHour(UPDATED_START_TIME_HOUR)
            .startTimeMinute(UPDATED_START_TIME_MINUTE)
            .endTimeHour(UPDATED_END_TIME_HOUR)
            .endTimeMinute(UPDATED_END_TIME_MINUTE);

        restTblWorkingHoursMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTblWorkingHours.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTblWorkingHours))
            )
            .andExpect(status().isOk());

        // Validate the TblWorkingHours in the database
        List<TblWorkingHours> tblWorkingHoursList = tblWorkingHoursRepository.findAll();
        assertThat(tblWorkingHoursList).hasSize(databaseSizeBeforeUpdate);
        TblWorkingHours testTblWorkingHours = tblWorkingHoursList.get(tblWorkingHoursList.size() - 1);
        assertThat(testTblWorkingHours.getDay()).isEqualTo(UPDATED_DAY);
        assertThat(testTblWorkingHours.getStartTimeHour()).isEqualTo(UPDATED_START_TIME_HOUR);
        assertThat(testTblWorkingHours.getStartTimeMinute()).isEqualTo(UPDATED_START_TIME_MINUTE);
        assertThat(testTblWorkingHours.getEndTimeHour()).isEqualTo(UPDATED_END_TIME_HOUR);
        assertThat(testTblWorkingHours.getEndTimeMinute()).isEqualTo(UPDATED_END_TIME_MINUTE);
    }

    @Test
    @Transactional
    void putNonExistingTblWorkingHours() throws Exception {
        int databaseSizeBeforeUpdate = tblWorkingHoursRepository.findAll().size();
        tblWorkingHours.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTblWorkingHoursMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tblWorkingHours.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tblWorkingHours))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblWorkingHours in the database
        List<TblWorkingHours> tblWorkingHoursList = tblWorkingHoursRepository.findAll();
        assertThat(tblWorkingHoursList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTblWorkingHours() throws Exception {
        int databaseSizeBeforeUpdate = tblWorkingHoursRepository.findAll().size();
        tblWorkingHours.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblWorkingHoursMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tblWorkingHours))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblWorkingHours in the database
        List<TblWorkingHours> tblWorkingHoursList = tblWorkingHoursRepository.findAll();
        assertThat(tblWorkingHoursList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTblWorkingHours() throws Exception {
        int databaseSizeBeforeUpdate = tblWorkingHoursRepository.findAll().size();
        tblWorkingHours.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblWorkingHoursMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tblWorkingHours))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TblWorkingHours in the database
        List<TblWorkingHours> tblWorkingHoursList = tblWorkingHoursRepository.findAll();
        assertThat(tblWorkingHoursList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTblWorkingHoursWithPatch() throws Exception {
        // Initialize the database
        tblWorkingHoursRepository.saveAndFlush(tblWorkingHours);

        int databaseSizeBeforeUpdate = tblWorkingHoursRepository.findAll().size();

        // Update the tblWorkingHours using partial update
        TblWorkingHours partialUpdatedTblWorkingHours = new TblWorkingHours();
        partialUpdatedTblWorkingHours.setId(tblWorkingHours.getId());

        partialUpdatedTblWorkingHours.day(UPDATED_DAY);

        restTblWorkingHoursMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTblWorkingHours.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTblWorkingHours))
            )
            .andExpect(status().isOk());

        // Validate the TblWorkingHours in the database
        List<TblWorkingHours> tblWorkingHoursList = tblWorkingHoursRepository.findAll();
        assertThat(tblWorkingHoursList).hasSize(databaseSizeBeforeUpdate);
        TblWorkingHours testTblWorkingHours = tblWorkingHoursList.get(tblWorkingHoursList.size() - 1);
        assertThat(testTblWorkingHours.getDay()).isEqualTo(UPDATED_DAY);
        assertThat(testTblWorkingHours.getStartTimeHour()).isEqualTo(DEFAULT_START_TIME_HOUR);
        assertThat(testTblWorkingHours.getStartTimeMinute()).isEqualTo(DEFAULT_START_TIME_MINUTE);
        assertThat(testTblWorkingHours.getEndTimeHour()).isEqualTo(DEFAULT_END_TIME_HOUR);
        assertThat(testTblWorkingHours.getEndTimeMinute()).isEqualTo(DEFAULT_END_TIME_MINUTE);
    }

    @Test
    @Transactional
    void fullUpdateTblWorkingHoursWithPatch() throws Exception {
        // Initialize the database
        tblWorkingHoursRepository.saveAndFlush(tblWorkingHours);

        int databaseSizeBeforeUpdate = tblWorkingHoursRepository.findAll().size();

        // Update the tblWorkingHours using partial update
        TblWorkingHours partialUpdatedTblWorkingHours = new TblWorkingHours();
        partialUpdatedTblWorkingHours.setId(tblWorkingHours.getId());

        partialUpdatedTblWorkingHours
            .day(UPDATED_DAY)
            .startTimeHour(UPDATED_START_TIME_HOUR)
            .startTimeMinute(UPDATED_START_TIME_MINUTE)
            .endTimeHour(UPDATED_END_TIME_HOUR)
            .endTimeMinute(UPDATED_END_TIME_MINUTE);

        restTblWorkingHoursMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTblWorkingHours.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTblWorkingHours))
            )
            .andExpect(status().isOk());

        // Validate the TblWorkingHours in the database
        List<TblWorkingHours> tblWorkingHoursList = tblWorkingHoursRepository.findAll();
        assertThat(tblWorkingHoursList).hasSize(databaseSizeBeforeUpdate);
        TblWorkingHours testTblWorkingHours = tblWorkingHoursList.get(tblWorkingHoursList.size() - 1);
        assertThat(testTblWorkingHours.getDay()).isEqualTo(UPDATED_DAY);
        assertThat(testTblWorkingHours.getStartTimeHour()).isEqualTo(UPDATED_START_TIME_HOUR);
        assertThat(testTblWorkingHours.getStartTimeMinute()).isEqualTo(UPDATED_START_TIME_MINUTE);
        assertThat(testTblWorkingHours.getEndTimeHour()).isEqualTo(UPDATED_END_TIME_HOUR);
        assertThat(testTblWorkingHours.getEndTimeMinute()).isEqualTo(UPDATED_END_TIME_MINUTE);
    }

    @Test
    @Transactional
    void patchNonExistingTblWorkingHours() throws Exception {
        int databaseSizeBeforeUpdate = tblWorkingHoursRepository.findAll().size();
        tblWorkingHours.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTblWorkingHoursMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tblWorkingHours.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tblWorkingHours))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblWorkingHours in the database
        List<TblWorkingHours> tblWorkingHoursList = tblWorkingHoursRepository.findAll();
        assertThat(tblWorkingHoursList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTblWorkingHours() throws Exception {
        int databaseSizeBeforeUpdate = tblWorkingHoursRepository.findAll().size();
        tblWorkingHours.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblWorkingHoursMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tblWorkingHours))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblWorkingHours in the database
        List<TblWorkingHours> tblWorkingHoursList = tblWorkingHoursRepository.findAll();
        assertThat(tblWorkingHoursList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTblWorkingHours() throws Exception {
        int databaseSizeBeforeUpdate = tblWorkingHoursRepository.findAll().size();
        tblWorkingHours.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblWorkingHoursMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tblWorkingHours))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TblWorkingHours in the database
        List<TblWorkingHours> tblWorkingHoursList = tblWorkingHoursRepository.findAll();
        assertThat(tblWorkingHoursList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTblWorkingHours() throws Exception {
        // Initialize the database
        tblWorkingHoursRepository.saveAndFlush(tblWorkingHours);

        int databaseSizeBeforeDelete = tblWorkingHoursRepository.findAll().size();

        // Delete the tblWorkingHours
        restTblWorkingHoursMockMvc
            .perform(delete(ENTITY_API_URL_ID, tblWorkingHours.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TblWorkingHours> tblWorkingHoursList = tblWorkingHoursRepository.findAll();
        assertThat(tblWorkingHoursList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
