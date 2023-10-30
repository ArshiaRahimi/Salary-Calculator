package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.TblUnauthorizedActivity;
import com.mycompany.myapp.repository.TblUnauthorizedActivityRepository;
import com.mycompany.myapp.service.TblUnauthorizedActivityQueryService;
import com.mycompany.myapp.service.TblUnauthorizedActivityService;
import com.mycompany.myapp.service.criteria.TblUnauthorizedActivityCriteria;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.TblUnauthorizedActivity}.
 */
@RestController
@RequestMapping("/api")
public class TblUnauthorizedActivityResource {

    private final Logger log = LoggerFactory.getLogger(TblUnauthorizedActivityResource.class);

    private static final String ENTITY_NAME = "tblUnauthorizedActivity";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TblUnauthorizedActivityService tblUnauthorizedActivityService;

    private final TblUnauthorizedActivityRepository tblUnauthorizedActivityRepository;

    private final TblUnauthorizedActivityQueryService tblUnauthorizedActivityQueryService;

    public TblUnauthorizedActivityResource(
        TblUnauthorizedActivityService tblUnauthorizedActivityService,
        TblUnauthorizedActivityRepository tblUnauthorizedActivityRepository,
        TblUnauthorizedActivityQueryService tblUnauthorizedActivityQueryService
    ) {
        this.tblUnauthorizedActivityService = tblUnauthorizedActivityService;
        this.tblUnauthorizedActivityRepository = tblUnauthorizedActivityRepository;
        this.tblUnauthorizedActivityQueryService = tblUnauthorizedActivityQueryService;
    }

    /**
     * {@code POST  /tbl-unauthorized-activities} : Create a new tblUnauthorizedActivity.
     *
     * @param tblUnauthorizedActivity the tblUnauthorizedActivity to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tblUnauthorizedActivity, or with status {@code 400 (Bad Request)} if the tblUnauthorizedActivity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tbl-unauthorized-activities")
    public ResponseEntity<TblUnauthorizedActivity> createTblUnauthorizedActivity(
        @RequestBody TblUnauthorizedActivity tblUnauthorizedActivity
    ) throws URISyntaxException {
        log.debug("REST request to save TblUnauthorizedActivity : {}", tblUnauthorizedActivity);
        if (tblUnauthorizedActivity.getId() != null) {
            throw new BadRequestAlertException("A new tblUnauthorizedActivity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TblUnauthorizedActivity result = tblUnauthorizedActivityService.save(tblUnauthorizedActivity);
        return ResponseEntity
            .created(new URI("/api/tbl-unauthorized-activities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tbl-unauthorized-activities/:id} : Updates an existing tblUnauthorizedActivity.
     *
     * @param id the id of the tblUnauthorizedActivity to save.
     * @param tblUnauthorizedActivity the tblUnauthorizedActivity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tblUnauthorizedActivity,
     * or with status {@code 400 (Bad Request)} if the tblUnauthorizedActivity is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tblUnauthorizedActivity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tbl-unauthorized-activities/{id}")
    public ResponseEntity<TblUnauthorizedActivity> updateTblUnauthorizedActivity(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TblUnauthorizedActivity tblUnauthorizedActivity
    ) throws URISyntaxException {
        log.debug("REST request to update TblUnauthorizedActivity : {}, {}", id, tblUnauthorizedActivity);
        if (tblUnauthorizedActivity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tblUnauthorizedActivity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tblUnauthorizedActivityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TblUnauthorizedActivity result = tblUnauthorizedActivityService.update(tblUnauthorizedActivity);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tblUnauthorizedActivity.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tbl-unauthorized-activities/:id} : Partial updates given fields of an existing tblUnauthorizedActivity, field will ignore if it is null
     *
     * @param id the id of the tblUnauthorizedActivity to save.
     * @param tblUnauthorizedActivity the tblUnauthorizedActivity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tblUnauthorizedActivity,
     * or with status {@code 400 (Bad Request)} if the tblUnauthorizedActivity is not valid,
     * or with status {@code 404 (Not Found)} if the tblUnauthorizedActivity is not found,
     * or with status {@code 500 (Internal Server Error)} if the tblUnauthorizedActivity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tbl-unauthorized-activities/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TblUnauthorizedActivity> partialUpdateTblUnauthorizedActivity(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TblUnauthorizedActivity tblUnauthorizedActivity
    ) throws URISyntaxException {
        log.debug("REST request to partial update TblUnauthorizedActivity partially : {}, {}", id, tblUnauthorizedActivity);
        if (tblUnauthorizedActivity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tblUnauthorizedActivity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tblUnauthorizedActivityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TblUnauthorizedActivity> result = tblUnauthorizedActivityService.partialUpdate(tblUnauthorizedActivity);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tblUnauthorizedActivity.getId().toString())
        );
    }

    /**
     * {@code GET  /tbl-unauthorized-activities} : get all the tblUnauthorizedActivities.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tblUnauthorizedActivities in body.
     */
    @GetMapping("/tbl-unauthorized-activities")
    public ResponseEntity<List<TblUnauthorizedActivity>> getAllTblUnauthorizedActivities(
        TblUnauthorizedActivityCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get TblUnauthorizedActivities by criteria: {}", criteria);
        Page<TblUnauthorizedActivity> page = tblUnauthorizedActivityQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tbl-unauthorized-activities/count} : count all the tblUnauthorizedActivities.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/tbl-unauthorized-activities/count")
    public ResponseEntity<Long> countTblUnauthorizedActivities(TblUnauthorizedActivityCriteria criteria) {
        log.debug("REST request to count TblUnauthorizedActivities by criteria: {}", criteria);
        return ResponseEntity.ok().body(tblUnauthorizedActivityQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tbl-unauthorized-activities/:id} : get the "id" tblUnauthorizedActivity.
     *
     * @param id the id of the tblUnauthorizedActivity to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tblUnauthorizedActivity, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tbl-unauthorized-activities/{id}")
    public ResponseEntity<TblUnauthorizedActivity> getTblUnauthorizedActivity(@PathVariable Long id) {
        log.debug("REST request to get TblUnauthorizedActivity : {}", id);
        Optional<TblUnauthorizedActivity> tblUnauthorizedActivity = tblUnauthorizedActivityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tblUnauthorizedActivity);
    }

    /**
     * {@code DELETE  /tbl-unauthorized-activities/:id} : delete the "id" tblUnauthorizedActivity.
     *
     * @param id the id of the tblUnauthorizedActivity to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tbl-unauthorized-activities/{id}")
    public ResponseEntity<Void> deleteTblUnauthorizedActivity(@PathVariable Long id) {
        log.debug("REST request to delete TblUnauthorizedActivity : {}", id);
        tblUnauthorizedActivityService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
