package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.TblSensorReadings;
import com.mycompany.myapp.repository.TblSensorReadingsRepository;
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
 * Integration tests for the {@link TblSensorReadingsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TblSensorReadingsResourceIT {

    private static final Long DEFAULT_EMPLOYEE_ID = 1L;
    private static final Long UPDATED_EMPLOYEE_ID = 2L;

    private static final ZonedDateTime DEFAULT_READING_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_READING_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/tbl-sensor-readings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TblSensorReadingsRepository tblSensorReadingsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTblSensorReadingsMockMvc;

    private TblSensorReadings tblSensorReadings;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TblSensorReadings createEntity(EntityManager em) {
        TblSensorReadings tblSensorReadings = new TblSensorReadings().employeeId(DEFAULT_EMPLOYEE_ID).readingTime(DEFAULT_READING_TIME);
        return tblSensorReadings;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TblSensorReadings createUpdatedEntity(EntityManager em) {
        TblSensorReadings tblSensorReadings = new TblSensorReadings().employeeId(UPDATED_EMPLOYEE_ID).readingTime(UPDATED_READING_TIME);
        return tblSensorReadings;
    }

    @BeforeEach
    public void initTest() {
        tblSensorReadings = createEntity(em);
    }

    @Test
    @Transactional
    void createTblSensorReadings() throws Exception {
        int databaseSizeBeforeCreate = tblSensorReadingsRepository.findAll().size();
        // Create the TblSensorReadings
        restTblSensorReadingsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tblSensorReadings))
            )
            .andExpect(status().isCreated());

        // Validate the TblSensorReadings in the database
        List<TblSensorReadings> tblSensorReadingsList = tblSensorReadingsRepository.findAll();
        assertThat(tblSensorReadingsList).hasSize(databaseSizeBeforeCreate + 1);
        TblSensorReadings testTblSensorReadings = tblSensorReadingsList.get(tblSensorReadingsList.size() - 1);
        assertThat(testTblSensorReadings.getEmployeeId()).isEqualTo(DEFAULT_EMPLOYEE_ID);
        assertThat(testTblSensorReadings.getReadingTime()).isEqualTo(DEFAULT_READING_TIME);
    }

    @Test
    @Transactional
    void createTblSensorReadingsWithExistingId() throws Exception {
        // Create the TblSensorReadings with an existing ID
        tblSensorReadings.setId(1L);

        int databaseSizeBeforeCreate = tblSensorReadingsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTblSensorReadingsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tblSensorReadings))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblSensorReadings in the database
        List<TblSensorReadings> tblSensorReadingsList = tblSensorReadingsRepository.findAll();
        assertThat(tblSensorReadingsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTblSensorReadings() throws Exception {
        // Initialize the database
        tblSensorReadingsRepository.saveAndFlush(tblSensorReadings);

        // Get all the tblSensorReadingsList
        restTblSensorReadingsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tblSensorReadings.getId().intValue())))
            .andExpect(jsonPath("$.[*].employeeId").value(hasItem(DEFAULT_EMPLOYEE_ID.intValue())))
            .andExpect(jsonPath("$.[*].readingTime").value(hasItem(sameInstant(DEFAULT_READING_TIME))));
    }

    @Test
    @Transactional
    void getTblSensorReadings() throws Exception {
        // Initialize the database
        tblSensorReadingsRepository.saveAndFlush(tblSensorReadings);

        // Get the tblSensorReadings
        restTblSensorReadingsMockMvc
            .perform(get(ENTITY_API_URL_ID, tblSensorReadings.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tblSensorReadings.getId().intValue()))
            .andExpect(jsonPath("$.employeeId").value(DEFAULT_EMPLOYEE_ID.intValue()))
            .andExpect(jsonPath("$.readingTime").value(sameInstant(DEFAULT_READING_TIME)));
    }

    @Test
    @Transactional
    void getNonExistingTblSensorReadings() throws Exception {
        // Get the tblSensorReadings
        restTblSensorReadingsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTblSensorReadings() throws Exception {
        // Initialize the database
        tblSensorReadingsRepository.saveAndFlush(tblSensorReadings);

        int databaseSizeBeforeUpdate = tblSensorReadingsRepository.findAll().size();

        // Update the tblSensorReadings
        TblSensorReadings updatedTblSensorReadings = tblSensorReadingsRepository.findById(tblSensorReadings.getId()).get();
        // Disconnect from session so that the updates on updatedTblSensorReadings are not directly saved in db
        em.detach(updatedTblSensorReadings);
        updatedTblSensorReadings.employeeId(UPDATED_EMPLOYEE_ID).readingTime(UPDATED_READING_TIME);

        restTblSensorReadingsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTblSensorReadings.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTblSensorReadings))
            )
            .andExpect(status().isOk());

        // Validate the TblSensorReadings in the database
        List<TblSensorReadings> tblSensorReadingsList = tblSensorReadingsRepository.findAll();
        assertThat(tblSensorReadingsList).hasSize(databaseSizeBeforeUpdate);
        TblSensorReadings testTblSensorReadings = tblSensorReadingsList.get(tblSensorReadingsList.size() - 1);
        assertThat(testTblSensorReadings.getEmployeeId()).isEqualTo(UPDATED_EMPLOYEE_ID);
        assertThat(testTblSensorReadings.getReadingTime()).isEqualTo(UPDATED_READING_TIME);
    }

    @Test
    @Transactional
    void putNonExistingTblSensorReadings() throws Exception {
        int databaseSizeBeforeUpdate = tblSensorReadingsRepository.findAll().size();
        tblSensorReadings.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTblSensorReadingsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tblSensorReadings.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tblSensorReadings))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblSensorReadings in the database
        List<TblSensorReadings> tblSensorReadingsList = tblSensorReadingsRepository.findAll();
        assertThat(tblSensorReadingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTblSensorReadings() throws Exception {
        int databaseSizeBeforeUpdate = tblSensorReadingsRepository.findAll().size();
        tblSensorReadings.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblSensorReadingsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tblSensorReadings))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblSensorReadings in the database
        List<TblSensorReadings> tblSensorReadingsList = tblSensorReadingsRepository.findAll();
        assertThat(tblSensorReadingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTblSensorReadings() throws Exception {
        int databaseSizeBeforeUpdate = tblSensorReadingsRepository.findAll().size();
        tblSensorReadings.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblSensorReadingsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tblSensorReadings))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TblSensorReadings in the database
        List<TblSensorReadings> tblSensorReadingsList = tblSensorReadingsRepository.findAll();
        assertThat(tblSensorReadingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTblSensorReadingsWithPatch() throws Exception {
        // Initialize the database
        tblSensorReadingsRepository.saveAndFlush(tblSensorReadings);

        int databaseSizeBeforeUpdate = tblSensorReadingsRepository.findAll().size();

        // Update the tblSensorReadings using partial update
        TblSensorReadings partialUpdatedTblSensorReadings = new TblSensorReadings();
        partialUpdatedTblSensorReadings.setId(tblSensorReadings.getId());

        partialUpdatedTblSensorReadings.readingTime(UPDATED_READING_TIME);

        restTblSensorReadingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTblSensorReadings.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTblSensorReadings))
            )
            .andExpect(status().isOk());

        // Validate the TblSensorReadings in the database
        List<TblSensorReadings> tblSensorReadingsList = tblSensorReadingsRepository.findAll();
        assertThat(tblSensorReadingsList).hasSize(databaseSizeBeforeUpdate);
        TblSensorReadings testTblSensorReadings = tblSensorReadingsList.get(tblSensorReadingsList.size() - 1);
        assertThat(testTblSensorReadings.getEmployeeId()).isEqualTo(DEFAULT_EMPLOYEE_ID);
        assertThat(testTblSensorReadings.getReadingTime()).isEqualTo(UPDATED_READING_TIME);
    }

    @Test
    @Transactional
    void fullUpdateTblSensorReadingsWithPatch() throws Exception {
        // Initialize the database
        tblSensorReadingsRepository.saveAndFlush(tblSensorReadings);

        int databaseSizeBeforeUpdate = tblSensorReadingsRepository.findAll().size();

        // Update the tblSensorReadings using partial update
        TblSensorReadings partialUpdatedTblSensorReadings = new TblSensorReadings();
        partialUpdatedTblSensorReadings.setId(tblSensorReadings.getId());

        partialUpdatedTblSensorReadings.employeeId(UPDATED_EMPLOYEE_ID).readingTime(UPDATED_READING_TIME);

        restTblSensorReadingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTblSensorReadings.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTblSensorReadings))
            )
            .andExpect(status().isOk());

        // Validate the TblSensorReadings in the database
        List<TblSensorReadings> tblSensorReadingsList = tblSensorReadingsRepository.findAll();
        assertThat(tblSensorReadingsList).hasSize(databaseSizeBeforeUpdate);
        TblSensorReadings testTblSensorReadings = tblSensorReadingsList.get(tblSensorReadingsList.size() - 1);
        assertThat(testTblSensorReadings.getEmployeeId()).isEqualTo(UPDATED_EMPLOYEE_ID);
        assertThat(testTblSensorReadings.getReadingTime()).isEqualTo(UPDATED_READING_TIME);
    }

    @Test
    @Transactional
    void patchNonExistingTblSensorReadings() throws Exception {
        int databaseSizeBeforeUpdate = tblSensorReadingsRepository.findAll().size();
        tblSensorReadings.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTblSensorReadingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tblSensorReadings.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tblSensorReadings))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblSensorReadings in the database
        List<TblSensorReadings> tblSensorReadingsList = tblSensorReadingsRepository.findAll();
        assertThat(tblSensorReadingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTblSensorReadings() throws Exception {
        int databaseSizeBeforeUpdate = tblSensorReadingsRepository.findAll().size();
        tblSensorReadings.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblSensorReadingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tblSensorReadings))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblSensorReadings in the database
        List<TblSensorReadings> tblSensorReadingsList = tblSensorReadingsRepository.findAll();
        assertThat(tblSensorReadingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTblSensorReadings() throws Exception {
        int databaseSizeBeforeUpdate = tblSensorReadingsRepository.findAll().size();
        tblSensorReadings.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblSensorReadingsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tblSensorReadings))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TblSensorReadings in the database
        List<TblSensorReadings> tblSensorReadingsList = tblSensorReadingsRepository.findAll();
        assertThat(tblSensorReadingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTblSensorReadings() throws Exception {
        // Initialize the database
        tblSensorReadingsRepository.saveAndFlush(tblSensorReadings);

        int databaseSizeBeforeDelete = tblSensorReadingsRepository.findAll().size();

        // Delete the tblSensorReadings
        restTblSensorReadingsMockMvc
            .perform(delete(ENTITY_API_URL_ID, tblSensorReadings.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TblSensorReadings> tblSensorReadingsList = tblSensorReadingsRepository.findAll();
        assertThat(tblSensorReadingsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
