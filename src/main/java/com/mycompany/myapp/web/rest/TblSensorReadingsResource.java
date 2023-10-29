package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.TblSensorReadings;
import com.mycompany.myapp.repository.TblSensorReadingsRepository;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.TblSensorReadings}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TblSensorReadingsResource {

    private final Logger log = LoggerFactory.getLogger(TblSensorReadingsResource.class);

    private static final String ENTITY_NAME = "tblSensorReadings";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TblSensorReadingsRepository tblSensorReadingsRepository;

    public TblSensorReadingsResource(TblSensorReadingsRepository tblSensorReadingsRepository) {
        this.tblSensorReadingsRepository = tblSensorReadingsRepository;
    }

    /**
     * {@code POST  /tbl-sensor-readings} : Create a new tblSensorReadings.
     *
     * @param tblSensorReadings the tblSensorReadings to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tblSensorReadings, or with status {@code 400 (Bad Request)} if the tblSensorReadings has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tbl-sensor-readings")
    public ResponseEntity<TblSensorReadings> createTblSensorReadings(@RequestBody TblSensorReadings tblSensorReadings)
        throws URISyntaxException {
        log.debug("REST request to save TblSensorReadings : {}", tblSensorReadings);
        if (tblSensorReadings.getId() != null) {
            throw new BadRequestAlertException("A new tblSensorReadings cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TblSensorReadings result = tblSensorReadingsRepository.save(tblSensorReadings);
        return ResponseEntity
            .created(new URI("/api/tbl-sensor-readings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tbl-sensor-readings/:id} : Updates an existing tblSensorReadings.
     *
     * @param id the id of the tblSensorReadings to save.
     * @param tblSensorReadings the tblSensorReadings to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tblSensorReadings,
     * or with status {@code 400 (Bad Request)} if the tblSensorReadings is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tblSensorReadings couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tbl-sensor-readings/{id}")
    public ResponseEntity<TblSensorReadings> updateTblSensorReadings(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TblSensorReadings tblSensorReadings
    ) throws URISyntaxException {
        log.debug("REST request to update TblSensorReadings : {}, {}", id, tblSensorReadings);
        if (tblSensorReadings.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tblSensorReadings.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tblSensorReadingsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TblSensorReadings result = tblSensorReadingsRepository.save(tblSensorReadings);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tblSensorReadings.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tbl-sensor-readings/:id} : Partial updates given fields of an existing tblSensorReadings, field will ignore if it is null
     *
     * @param id the id of the tblSensorReadings to save.
     * @param tblSensorReadings the tblSensorReadings to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tblSensorReadings,
     * or with status {@code 400 (Bad Request)} if the tblSensorReadings is not valid,
     * or with status {@code 404 (Not Found)} if the tblSensorReadings is not found,
     * or with status {@code 500 (Internal Server Error)} if the tblSensorReadings couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tbl-sensor-readings/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TblSensorReadings> partialUpdateTblSensorReadings(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TblSensorReadings tblSensorReadings
    ) throws URISyntaxException {
        log.debug("REST request to partial update TblSensorReadings partially : {}, {}", id, tblSensorReadings);
        if (tblSensorReadings.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tblSensorReadings.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tblSensorReadingsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TblSensorReadings> result = tblSensorReadingsRepository
            .findById(tblSensorReadings.getId())
            .map(existingTblSensorReadings -> {
                if (tblSensorReadings.getEmployeeId() != null) {
                    existingTblSensorReadings.setEmployeeId(tblSensorReadings.getEmployeeId());
                }
                if (tblSensorReadings.getReadingTime() != null) {
                    existingTblSensorReadings.setReadingTime(tblSensorReadings.getReadingTime());
                }

                return existingTblSensorReadings;
            })
            .map(tblSensorReadingsRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tblSensorReadings.getId().toString())
        );
    }

    /**
     * {@code GET  /tbl-sensor-readings} : get all the tblSensorReadings.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tblSensorReadings in body.
     */
    @GetMapping("/tbl-sensor-readings")
    public ResponseEntity<List<TblSensorReadings>> getAllTblSensorReadings(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of TblSensorReadings");
        Page<TblSensorReadings> page = tblSensorReadingsRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tbl-sensor-readings/:id} : get the "id" tblSensorReadings.
     *
     * @param id the id of the tblSensorReadings to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tblSensorReadings, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tbl-sensor-readings/{id}")
    public ResponseEntity<TblSensorReadings> getTblSensorReadings(@PathVariable Long id) {
        log.debug("REST request to get TblSensorReadings : {}", id);
        Optional<TblSensorReadings> tblSensorReadings = tblSensorReadingsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(tblSensorReadings);
    }

    /**
     * {@code DELETE  /tbl-sensor-readings/:id} : delete the "id" tblSensorReadings.
     *
     * @param id the id of the tblSensorReadings to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tbl-sensor-readings/{id}")
    public ResponseEntity<Void> deleteTblSensorReadings(@PathVariable Long id) {
        log.debug("REST request to delete TblSensorReadings : {}", id);
        tblSensorReadingsRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
