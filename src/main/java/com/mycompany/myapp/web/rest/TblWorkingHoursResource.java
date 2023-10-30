package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.TblWorkingHours;
import com.mycompany.myapp.repository.TblWorkingHoursRepository;
import com.mycompany.myapp.service.TblWorkingHoursQueryService;
import com.mycompany.myapp.service.TblWorkingHoursService;
import com.mycompany.myapp.service.criteria.TblWorkingHoursCriteria;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.TblWorkingHours}.
 */
@RestController
@RequestMapping("/api")
public class TblWorkingHoursResource {

    private final Logger log = LoggerFactory.getLogger(TblWorkingHoursResource.class);

    private static final String ENTITY_NAME = "tblWorkingHours";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TblWorkingHoursService tblWorkingHoursService;

    private final TblWorkingHoursRepository tblWorkingHoursRepository;

    private final TblWorkingHoursQueryService tblWorkingHoursQueryService;

    public TblWorkingHoursResource(
        TblWorkingHoursService tblWorkingHoursService,
        TblWorkingHoursRepository tblWorkingHoursRepository,
        TblWorkingHoursQueryService tblWorkingHoursQueryService
    ) {
        this.tblWorkingHoursService = tblWorkingHoursService;
        this.tblWorkingHoursRepository = tblWorkingHoursRepository;
        this.tblWorkingHoursQueryService = tblWorkingHoursQueryService;
    }

    /**
     * {@code POST  /tbl-working-hours} : Create a new tblWorkingHours.
     *
     * @param tblWorkingHours the tblWorkingHours to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tblWorkingHours, or with status {@code 400 (Bad Request)} if the tblWorkingHours has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tbl-working-hours")
    public ResponseEntity<TblWorkingHours> createTblWorkingHours(@RequestBody TblWorkingHours tblWorkingHours) throws URISyntaxException {
        log.debug("REST request to save TblWorkingHours : {}", tblWorkingHours);
        if (tblWorkingHours.getId() != null) {
            throw new BadRequestAlertException("A new tblWorkingHours cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TblWorkingHours result = tblWorkingHoursService.save(tblWorkingHours);
        return ResponseEntity
            .created(new URI("/api/tbl-working-hours/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tbl-working-hours/:id} : Updates an existing tblWorkingHours.
     *
     * @param id the id of the tblWorkingHours to save.
     * @param tblWorkingHours the tblWorkingHours to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tblWorkingHours,
     * or with status {@code 400 (Bad Request)} if the tblWorkingHours is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tblWorkingHours couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tbl-working-hours/{id}")
    public ResponseEntity<TblWorkingHours> updateTblWorkingHours(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TblWorkingHours tblWorkingHours
    ) throws URISyntaxException {
        log.debug("REST request to update TblWorkingHours : {}, {}", id, tblWorkingHours);
        if (tblWorkingHours.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tblWorkingHours.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tblWorkingHoursRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TblWorkingHours result = tblWorkingHoursService.update(tblWorkingHours);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tblWorkingHours.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tbl-working-hours/:id} : Partial updates given fields of an existing tblWorkingHours, field will ignore if it is null
     *
     * @param id the id of the tblWorkingHours to save.
     * @param tblWorkingHours the tblWorkingHours to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tblWorkingHours,
     * or with status {@code 400 (Bad Request)} if the tblWorkingHours is not valid,
     * or with status {@code 404 (Not Found)} if the tblWorkingHours is not found,
     * or with status {@code 500 (Internal Server Error)} if the tblWorkingHours couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tbl-working-hours/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TblWorkingHours> partialUpdateTblWorkingHours(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TblWorkingHours tblWorkingHours
    ) throws URISyntaxException {
        log.debug("REST request to partial update TblWorkingHours partially : {}, {}", id, tblWorkingHours);
        if (tblWorkingHours.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tblWorkingHours.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tblWorkingHoursRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TblWorkingHours> result = tblWorkingHoursService.partialUpdate(tblWorkingHours);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tblWorkingHours.getId().toString())
        );
    }

    /**
     * {@code GET  /tbl-working-hours} : get all the tblWorkingHours.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tblWorkingHours in body.
     */
    @GetMapping("/tbl-working-hours")
    public ResponseEntity<List<TblWorkingHours>> getAllTblWorkingHours(
        TblWorkingHoursCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get TblWorkingHours by criteria: {}", criteria);
        Page<TblWorkingHours> page = tblWorkingHoursQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tbl-working-hours/count} : count all the tblWorkingHours.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/tbl-working-hours/count")
    public ResponseEntity<Long> countTblWorkingHours(TblWorkingHoursCriteria criteria) {
        log.debug("REST request to count TblWorkingHours by criteria: {}", criteria);
        return ResponseEntity.ok().body(tblWorkingHoursQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tbl-working-hours/:id} : get the "id" tblWorkingHours.
     *
     * @param id the id of the tblWorkingHours to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tblWorkingHours, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tbl-working-hours/{id}")
    public ResponseEntity<TblWorkingHours> getTblWorkingHours(@PathVariable Long id) {
        log.debug("REST request to get TblWorkingHours : {}", id);
        Optional<TblWorkingHours> tblWorkingHours = tblWorkingHoursService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tblWorkingHours);
    }

    /**
     * {@code DELETE  /tbl-working-hours/:id} : delete the "id" tblWorkingHours.
     *
     * @param id the id of the tblWorkingHours to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tbl-working-hours/{id}")
    public ResponseEntity<Void> deleteTblWorkingHours(@PathVariable Long id) {
        log.debug("REST request to delete TblWorkingHours : {}", id);
        tblWorkingHoursService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
