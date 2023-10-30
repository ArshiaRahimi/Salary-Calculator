package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.TblEmployeeSalary;
import com.mycompany.myapp.repository.TblEmployeeSalaryRepository;
import com.mycompany.myapp.service.TblEmployeeSalaryQueryService;
import com.mycompany.myapp.service.TblEmployeeSalaryService;
import com.mycompany.myapp.service.criteria.TblEmployeeSalaryCriteria;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.TblEmployeeSalary}.
 */
@RestController
@RequestMapping("/api")
public class TblEmployeeSalaryResource {

    private final Logger log = LoggerFactory.getLogger(TblEmployeeSalaryResource.class);

    private static final String ENTITY_NAME = "tblEmployeeSalary";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TblEmployeeSalaryService tblEmployeeSalaryService;

    private final TblEmployeeSalaryRepository tblEmployeeSalaryRepository;

    private final TblEmployeeSalaryQueryService tblEmployeeSalaryQueryService;

    public TblEmployeeSalaryResource(
        TblEmployeeSalaryService tblEmployeeSalaryService,
        TblEmployeeSalaryRepository tblEmployeeSalaryRepository,
        TblEmployeeSalaryQueryService tblEmployeeSalaryQueryService
    ) {
        this.tblEmployeeSalaryService = tblEmployeeSalaryService;
        this.tblEmployeeSalaryRepository = tblEmployeeSalaryRepository;
        this.tblEmployeeSalaryQueryService = tblEmployeeSalaryQueryService;
    }

    /**
     * {@code POST  /tbl-employee-salaries} : Create a new tblEmployeeSalary.
     *
     * @param tblEmployeeSalary the tblEmployeeSalary to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tblEmployeeSalary, or with status {@code 400 (Bad Request)} if the tblEmployeeSalary has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tbl-employee-salaries")
    public ResponseEntity<TblEmployeeSalary> createTblEmployeeSalary(@RequestBody TblEmployeeSalary tblEmployeeSalary)
        throws URISyntaxException {
        log.debug("REST request to save TblEmployeeSalary : {}", tblEmployeeSalary);
        if (tblEmployeeSalary.getId() != null) {
            throw new BadRequestAlertException("A new tblEmployeeSalary cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TblEmployeeSalary result = tblEmployeeSalaryService.save(tblEmployeeSalary);
        return ResponseEntity
            .created(new URI("/api/tbl-employee-salaries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tbl-employee-salaries/:id} : Updates an existing tblEmployeeSalary.
     *
     * @param id the id of the tblEmployeeSalary to save.
     * @param tblEmployeeSalary the tblEmployeeSalary to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tblEmployeeSalary,
     * or with status {@code 400 (Bad Request)} if the tblEmployeeSalary is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tblEmployeeSalary couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tbl-employee-salaries/{id}")
    public ResponseEntity<TblEmployeeSalary> updateTblEmployeeSalary(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TblEmployeeSalary tblEmployeeSalary
    ) throws URISyntaxException {
        log.debug("REST request to update TblEmployeeSalary : {}, {}", id, tblEmployeeSalary);
        if (tblEmployeeSalary.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tblEmployeeSalary.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tblEmployeeSalaryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TblEmployeeSalary result = tblEmployeeSalaryService.update(tblEmployeeSalary);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tblEmployeeSalary.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tbl-employee-salaries/:id} : Partial updates given fields of an existing tblEmployeeSalary, field will ignore if it is null
     *
     * @param id the id of the tblEmployeeSalary to save.
     * @param tblEmployeeSalary the tblEmployeeSalary to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tblEmployeeSalary,
     * or with status {@code 400 (Bad Request)} if the tblEmployeeSalary is not valid,
     * or with status {@code 404 (Not Found)} if the tblEmployeeSalary is not found,
     * or with status {@code 500 (Internal Server Error)} if the tblEmployeeSalary couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tbl-employee-salaries/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TblEmployeeSalary> partialUpdateTblEmployeeSalary(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TblEmployeeSalary tblEmployeeSalary
    ) throws URISyntaxException {
        log.debug("REST request to partial update TblEmployeeSalary partially : {}, {}", id, tblEmployeeSalary);
        if (tblEmployeeSalary.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tblEmployeeSalary.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tblEmployeeSalaryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TblEmployeeSalary> result = tblEmployeeSalaryService.partialUpdate(tblEmployeeSalary);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tblEmployeeSalary.getId().toString())
        );
    }

    /**
     * {@code GET  /tbl-employee-salaries} : get all the tblEmployeeSalaries.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tblEmployeeSalaries in body.
     */
    @GetMapping("/tbl-employee-salaries")
    public ResponseEntity<List<TblEmployeeSalary>> getAllTblEmployeeSalaries(
        TblEmployeeSalaryCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get TblEmployeeSalaries by criteria: {}", criteria);
        Page<TblEmployeeSalary> page = tblEmployeeSalaryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tbl-employee-salaries/count} : count all the tblEmployeeSalaries.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/tbl-employee-salaries/count")
    public ResponseEntity<Long> countTblEmployeeSalaries(TblEmployeeSalaryCriteria criteria) {
        log.debug("REST request to count TblEmployeeSalaries by criteria: {}", criteria);
        return ResponseEntity.ok().body(tblEmployeeSalaryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tbl-employee-salaries/:id} : get the "id" tblEmployeeSalary.
     *
     * @param id the id of the tblEmployeeSalary to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tblEmployeeSalary, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tbl-employee-salaries/{id}")
    public ResponseEntity<TblEmployeeSalary> getTblEmployeeSalary(@PathVariable Long id) {
        log.debug("REST request to get TblEmployeeSalary : {}", id);
        Optional<TblEmployeeSalary> tblEmployeeSalary = tblEmployeeSalaryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tblEmployeeSalary);
    }

    /**
     * {@code DELETE  /tbl-employee-salaries/:id} : delete the "id" tblEmployeeSalary.
     *
     * @param id the id of the tblEmployeeSalary to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tbl-employee-salaries/{id}")
    public ResponseEntity<Void> deleteTblEmployeeSalary(@PathVariable Long id) {
        log.debug("REST request to delete TblEmployeeSalary : {}", id);
        tblEmployeeSalaryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
