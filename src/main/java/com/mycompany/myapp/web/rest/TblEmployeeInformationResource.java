package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.TblEmployeeInformation;
import com.mycompany.myapp.repository.TblEmployeeInformationRepository;
import com.mycompany.myapp.service.TblEmployeeInformationQueryService;
import com.mycompany.myapp.service.TblEmployeeInformationService;
import com.mycompany.myapp.service.criteria.TblEmployeeInformationCriteria;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.TblEmployeeInformation}.
 */
@RestController
@RequestMapping("/api")
public class TblEmployeeInformationResource {

    private final Logger log = LoggerFactory.getLogger(TblEmployeeInformationResource.class);

    private static final String ENTITY_NAME = "tblEmployeeInformation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TblEmployeeInformationService tblEmployeeInformationService;

    private final TblEmployeeInformationRepository tblEmployeeInformationRepository;

    private final TblEmployeeInformationQueryService tblEmployeeInformationQueryService;

    public TblEmployeeInformationResource(
        TblEmployeeInformationService tblEmployeeInformationService,
        TblEmployeeInformationRepository tblEmployeeInformationRepository,
        TblEmployeeInformationQueryService tblEmployeeInformationQueryService
    ) {
        this.tblEmployeeInformationService = tblEmployeeInformationService;
        this.tblEmployeeInformationRepository = tblEmployeeInformationRepository;
        this.tblEmployeeInformationQueryService = tblEmployeeInformationQueryService;
    }

    /**
     * {@code POST  /tbl-employee-informations} : Create a new tblEmployeeInformation.
     *
     * @param tblEmployeeInformation the tblEmployeeInformation to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tblEmployeeInformation, or with status {@code 400 (Bad Request)} if the tblEmployeeInformation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tbl-employee-informations")
    public ResponseEntity<TblEmployeeInformation> createTblEmployeeInformation(@RequestBody TblEmployeeInformation tblEmployeeInformation)
        throws URISyntaxException {
        log.debug("REST request to save TblEmployeeInformation : {}", tblEmployeeInformation);
        if (tblEmployeeInformation.getId() != null) {
            throw new BadRequestAlertException("A new tblEmployeeInformation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TblEmployeeInformation result = tblEmployeeInformationService.save(tblEmployeeInformation);
        return ResponseEntity
            .created(new URI("/api/tbl-employee-informations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tbl-employee-informations/:id} : Updates an existing tblEmployeeInformation.
     *
     * @param id the id of the tblEmployeeInformation to save.
     * @param tblEmployeeInformation the tblEmployeeInformation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tblEmployeeInformation,
     * or with status {@code 400 (Bad Request)} if the tblEmployeeInformation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tblEmployeeInformation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tbl-employee-informations/{id}")
    public ResponseEntity<TblEmployeeInformation> updateTblEmployeeInformation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TblEmployeeInformation tblEmployeeInformation
    ) throws URISyntaxException {
        log.debug("REST request to update TblEmployeeInformation : {}, {}", id, tblEmployeeInformation);
        if (tblEmployeeInformation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tblEmployeeInformation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tblEmployeeInformationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TblEmployeeInformation result = tblEmployeeInformationService.update(tblEmployeeInformation);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tblEmployeeInformation.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tbl-employee-informations/:id} : Partial updates given fields of an existing tblEmployeeInformation, field will ignore if it is null
     *
     * @param id the id of the tblEmployeeInformation to save.
     * @param tblEmployeeInformation the tblEmployeeInformation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tblEmployeeInformation,
     * or with status {@code 400 (Bad Request)} if the tblEmployeeInformation is not valid,
     * or with status {@code 404 (Not Found)} if the tblEmployeeInformation is not found,
     * or with status {@code 500 (Internal Server Error)} if the tblEmployeeInformation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tbl-employee-informations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TblEmployeeInformation> partialUpdateTblEmployeeInformation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TblEmployeeInformation tblEmployeeInformation
    ) throws URISyntaxException {
        log.debug("REST request to partial update TblEmployeeInformation partially : {}, {}", id, tblEmployeeInformation);
        if (tblEmployeeInformation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tblEmployeeInformation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tblEmployeeInformationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TblEmployeeInformation> result = tblEmployeeInformationService.partialUpdate(tblEmployeeInformation);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tblEmployeeInformation.getId().toString())
        );
    }

    /**
     * {@code GET  /tbl-employee-informations} : get all the tblEmployeeInformations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tblEmployeeInformations in body.
     */
    @GetMapping("/tbl-employee-informations")
    public ResponseEntity<List<TblEmployeeInformation>> getAllTblEmployeeInformations(
        TblEmployeeInformationCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get TblEmployeeInformations by criteria: {}", criteria);
        Page<TblEmployeeInformation> page = tblEmployeeInformationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tbl-employee-informations/count} : count all the tblEmployeeInformations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/tbl-employee-informations/count")
    public ResponseEntity<Long> countTblEmployeeInformations(TblEmployeeInformationCriteria criteria) {
        log.debug("REST request to count TblEmployeeInformations by criteria: {}", criteria);
        return ResponseEntity.ok().body(tblEmployeeInformationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tbl-employee-informations/:id} : get the "id" tblEmployeeInformation.
     *
     * @param id the id of the tblEmployeeInformation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tblEmployeeInformation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tbl-employee-informations/{id}")
    public ResponseEntity<TblEmployeeInformation> getTblEmployeeInformation(@PathVariable Long id) {
        log.debug("REST request to get TblEmployeeInformation : {}", id);
        Optional<TblEmployeeInformation> tblEmployeeInformation = tblEmployeeInformationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tblEmployeeInformation);
    }

    /**
     * {@code DELETE  /tbl-employee-informations/:id} : delete the "id" tblEmployeeInformation.
     *
     * @param id the id of the tblEmployeeInformation to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tbl-employee-informations/{id}")
    public ResponseEntity<Void> deleteTblEmployeeInformation(@PathVariable Long id) {
        log.debug("REST request to delete TblEmployeeInformation : {}", id);
        tblEmployeeInformationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
