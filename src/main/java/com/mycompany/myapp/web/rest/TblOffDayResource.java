package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.TblOffDay;
import com.mycompany.myapp.repository.TblOffDayRepository;
import com.mycompany.myapp.service.TblOffDayQueryService;
import com.mycompany.myapp.service.TblOffDayService;
import com.mycompany.myapp.service.criteria.TblOffDayCriteria;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.TblOffDay}.
 */
@RestController
@RequestMapping("/api")
public class TblOffDayResource {

    private final Logger log = LoggerFactory.getLogger(TblOffDayResource.class);

    private static final String ENTITY_NAME = "tblOffDay";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TblOffDayService tblOffDayService;

    private final TblOffDayRepository tblOffDayRepository;

    private final TblOffDayQueryService tblOffDayQueryService;

    public TblOffDayResource(
        TblOffDayService tblOffDayService,
        TblOffDayRepository tblOffDayRepository,
        TblOffDayQueryService tblOffDayQueryService
    ) {
        this.tblOffDayService = tblOffDayService;
        this.tblOffDayRepository = tblOffDayRepository;
        this.tblOffDayQueryService = tblOffDayQueryService;
    }

    /**
     * {@code POST  /tbl-off-days} : Create a new tblOffDay.
     *
     * @param tblOffDay the tblOffDay to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tblOffDay, or with status {@code 400 (Bad Request)} if the tblOffDay has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tbl-off-days")
    public ResponseEntity<TblOffDay> createTblOffDay(@RequestBody TblOffDay tblOffDay) throws URISyntaxException {
        log.debug("REST request to save TblOffDay : {}", tblOffDay);
        if (tblOffDay.getId() != null) {
            throw new BadRequestAlertException("A new tblOffDay cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TblOffDay result = tblOffDayService.save(tblOffDay);
        return ResponseEntity
            .created(new URI("/api/tbl-off-days/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tbl-off-days/:id} : Updates an existing tblOffDay.
     *
     * @param id the id of the tblOffDay to save.
     * @param tblOffDay the tblOffDay to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tblOffDay,
     * or with status {@code 400 (Bad Request)} if the tblOffDay is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tblOffDay couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tbl-off-days/{id}")
    public ResponseEntity<TblOffDay> updateTblOffDay(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TblOffDay tblOffDay
    ) throws URISyntaxException {
        log.debug("REST request to update TblOffDay : {}, {}", id, tblOffDay);
        if (tblOffDay.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tblOffDay.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tblOffDayRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TblOffDay result = tblOffDayService.update(tblOffDay);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tblOffDay.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tbl-off-days/:id} : Partial updates given fields of an existing tblOffDay, field will ignore if it is null
     *
     * @param id the id of the tblOffDay to save.
     * @param tblOffDay the tblOffDay to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tblOffDay,
     * or with status {@code 400 (Bad Request)} if the tblOffDay is not valid,
     * or with status {@code 404 (Not Found)} if the tblOffDay is not found,
     * or with status {@code 500 (Internal Server Error)} if the tblOffDay couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tbl-off-days/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TblOffDay> partialUpdateTblOffDay(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TblOffDay tblOffDay
    ) throws URISyntaxException {
        log.debug("REST request to partial update TblOffDay partially : {}, {}", id, tblOffDay);
        if (tblOffDay.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tblOffDay.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tblOffDayRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TblOffDay> result = tblOffDayService.partialUpdate(tblOffDay);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tblOffDay.getId().toString())
        );
    }

    /**
     * {@code GET  /tbl-off-days} : get all the tblOffDays.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tblOffDays in body.
     */
    @GetMapping("/tbl-off-days")
    public ResponseEntity<List<TblOffDay>> getAllTblOffDays(
        TblOffDayCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get TblOffDays by criteria: {}", criteria);
        Page<TblOffDay> page = tblOffDayQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tbl-off-days/count} : count all the tblOffDays.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/tbl-off-days/count")
    public ResponseEntity<Long> countTblOffDays(TblOffDayCriteria criteria) {
        log.debug("REST request to count TblOffDays by criteria: {}", criteria);
        return ResponseEntity.ok().body(tblOffDayQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tbl-off-days/:id} : get the "id" tblOffDay.
     *
     * @param id the id of the tblOffDay to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tblOffDay, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tbl-off-days/{id}")
    public ResponseEntity<TblOffDay> getTblOffDay(@PathVariable Long id) {
        log.debug("REST request to get TblOffDay : {}", id);
        Optional<TblOffDay> tblOffDay = tblOffDayService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tblOffDay);
    }

    /**
     * {@code DELETE  /tbl-off-days/:id} : delete the "id" tblOffDay.
     *
     * @param id the id of the tblOffDay to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tbl-off-days/{id}")
    public ResponseEntity<Void> deleteTblOffDay(@PathVariable Long id) {
        log.debug("REST request to delete TblOffDay : {}", id);
        tblOffDayService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
