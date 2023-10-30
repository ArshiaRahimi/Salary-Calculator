package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.TblEmployeeAuthentication;
import com.mycompany.myapp.repository.TblEmployeeAuthenticationRepository;
import com.mycompany.myapp.service.TblEmployeeAuthenticationQueryService;
import com.mycompany.myapp.service.TblEmployeeAuthenticationService;
import com.mycompany.myapp.service.criteria.TblEmployeeAuthenticationCriteria;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.TblEmployeeAuthentication}.
 */
@RestController
@RequestMapping("/api")
public class TblEmployeeAuthenticationResource {

    private final Logger log = LoggerFactory.getLogger(TblEmployeeAuthenticationResource.class);

    private static final String ENTITY_NAME = "tblEmployeeAuthentication";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TblEmployeeAuthenticationService tblEmployeeAuthenticationService;

    private final TblEmployeeAuthenticationRepository tblEmployeeAuthenticationRepository;

    private final TblEmployeeAuthenticationQueryService tblEmployeeAuthenticationQueryService;

    public TblEmployeeAuthenticationResource(
        TblEmployeeAuthenticationService tblEmployeeAuthenticationService,
        TblEmployeeAuthenticationRepository tblEmployeeAuthenticationRepository,
        TblEmployeeAuthenticationQueryService tblEmployeeAuthenticationQueryService
    ) {
        this.tblEmployeeAuthenticationService = tblEmployeeAuthenticationService;
        this.tblEmployeeAuthenticationRepository = tblEmployeeAuthenticationRepository;
        this.tblEmployeeAuthenticationQueryService = tblEmployeeAuthenticationQueryService;
    }

    /**
     * {@code POST  /tbl-employee-authentications} : Create a new tblEmployeeAuthentication.
     *
     * @param tblEmployeeAuthentication the tblEmployeeAuthentication to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tblEmployeeAuthentication, or with status {@code 400 (Bad Request)} if the tblEmployeeAuthentication has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tbl-employee-authentications")
    public ResponseEntity<TblEmployeeAuthentication> createTblEmployeeAuthentication(
        @RequestBody TblEmployeeAuthentication tblEmployeeAuthentication
    ) throws URISyntaxException {
        log.debug("REST request to save TblEmployeeAuthentication : {}", tblEmployeeAuthentication);
        if (tblEmployeeAuthentication.getId() != null) {
            throw new BadRequestAlertException("A new tblEmployeeAuthentication cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TblEmployeeAuthentication result = tblEmployeeAuthenticationService.save(tblEmployeeAuthentication);
        return ResponseEntity
            .created(new URI("/api/tbl-employee-authentications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tbl-employee-authentications/:id} : Updates an existing tblEmployeeAuthentication.
     *
     * @param id the id of the tblEmployeeAuthentication to save.
     * @param tblEmployeeAuthentication the tblEmployeeAuthentication to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tblEmployeeAuthentication,
     * or with status {@code 400 (Bad Request)} if the tblEmployeeAuthentication is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tblEmployeeAuthentication couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tbl-employee-authentications/{id}")
    public ResponseEntity<TblEmployeeAuthentication> updateTblEmployeeAuthentication(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TblEmployeeAuthentication tblEmployeeAuthentication
    ) throws URISyntaxException {
        log.debug("REST request to update TblEmployeeAuthentication : {}, {}", id, tblEmployeeAuthentication);
        if (tblEmployeeAuthentication.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tblEmployeeAuthentication.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tblEmployeeAuthenticationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TblEmployeeAuthentication result = tblEmployeeAuthenticationService.update(tblEmployeeAuthentication);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tblEmployeeAuthentication.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tbl-employee-authentications/:id} : Partial updates given fields of an existing tblEmployeeAuthentication, field will ignore if it is null
     *
     * @param id the id of the tblEmployeeAuthentication to save.
     * @param tblEmployeeAuthentication the tblEmployeeAuthentication to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tblEmployeeAuthentication,
     * or with status {@code 400 (Bad Request)} if the tblEmployeeAuthentication is not valid,
     * or with status {@code 404 (Not Found)} if the tblEmployeeAuthentication is not found,
     * or with status {@code 500 (Internal Server Error)} if the tblEmployeeAuthentication couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tbl-employee-authentications/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TblEmployeeAuthentication> partialUpdateTblEmployeeAuthentication(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TblEmployeeAuthentication tblEmployeeAuthentication
    ) throws URISyntaxException {
        log.debug("REST request to partial update TblEmployeeAuthentication partially : {}, {}", id, tblEmployeeAuthentication);
        if (tblEmployeeAuthentication.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tblEmployeeAuthentication.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tblEmployeeAuthenticationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TblEmployeeAuthentication> result = tblEmployeeAuthenticationService.partialUpdate(tblEmployeeAuthentication);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tblEmployeeAuthentication.getId().toString())
        );
    }

    /**
     * {@code GET  /tbl-employee-authentications} : get all the tblEmployeeAuthentications.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tblEmployeeAuthentications in body.
     */
    @GetMapping("/tbl-employee-authentications")
    public ResponseEntity<List<TblEmployeeAuthentication>> getAllTblEmployeeAuthentications(
        TblEmployeeAuthenticationCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get TblEmployeeAuthentications by criteria: {}", criteria);
        Page<TblEmployeeAuthentication> page = tblEmployeeAuthenticationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tbl-employee-authentications/count} : count all the tblEmployeeAuthentications.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/tbl-employee-authentications/count")
    public ResponseEntity<Long> countTblEmployeeAuthentications(TblEmployeeAuthenticationCriteria criteria) {
        log.debug("REST request to count TblEmployeeAuthentications by criteria: {}", criteria);
        return ResponseEntity.ok().body(tblEmployeeAuthenticationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tbl-employee-authentications/:id} : get the "id" tblEmployeeAuthentication.
     *
     * @param id the id of the tblEmployeeAuthentication to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tblEmployeeAuthentication, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tbl-employee-authentications/{id}")
    public ResponseEntity<TblEmployeeAuthentication> getTblEmployeeAuthentication(@PathVariable Long id) {
        log.debug("REST request to get TblEmployeeAuthentication : {}", id);
        Optional<TblEmployeeAuthentication> tblEmployeeAuthentication = tblEmployeeAuthenticationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tblEmployeeAuthentication);
    }

    /**
     * {@code DELETE  /tbl-employee-authentications/:id} : delete the "id" tblEmployeeAuthentication.
     *
     * @param id the id of the tblEmployeeAuthentication to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tbl-employee-authentications/{id}")
    public ResponseEntity<Void> deleteTblEmployeeAuthentication(@PathVariable Long id) {
        log.debug("REST request to delete TblEmployeeAuthentication : {}", id);
        tblEmployeeAuthenticationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
