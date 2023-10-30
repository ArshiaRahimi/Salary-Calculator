package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.TblSalaryInfo;
import com.mycompany.myapp.repository.TblSalaryInfoRepository;
import com.mycompany.myapp.service.TblSalaryInfoQueryService;
import com.mycompany.myapp.service.TblSalaryInfoService;
import com.mycompany.myapp.service.criteria.TblSalaryInfoCriteria;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.TblSalaryInfo}.
 */
@RestController
@RequestMapping("/api")
public class TblSalaryInfoResource {

    private final Logger log = LoggerFactory.getLogger(TblSalaryInfoResource.class);

    private static final String ENTITY_NAME = "tblSalaryInfo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TblSalaryInfoService tblSalaryInfoService;

    private final TblSalaryInfoRepository tblSalaryInfoRepository;

    private final TblSalaryInfoQueryService tblSalaryInfoQueryService;

    public TblSalaryInfoResource(
        TblSalaryInfoService tblSalaryInfoService,
        TblSalaryInfoRepository tblSalaryInfoRepository,
        TblSalaryInfoQueryService tblSalaryInfoQueryService
    ) {
        this.tblSalaryInfoService = tblSalaryInfoService;
        this.tblSalaryInfoRepository = tblSalaryInfoRepository;
        this.tblSalaryInfoQueryService = tblSalaryInfoQueryService;
    }

    /**
     * {@code POST  /tbl-salary-infos} : Create a new tblSalaryInfo.
     *
     * @param tblSalaryInfo the tblSalaryInfo to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tblSalaryInfo, or with status {@code 400 (Bad Request)} if the tblSalaryInfo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tbl-salary-infos")
    public ResponseEntity<TblSalaryInfo> createTblSalaryInfo(@RequestBody TblSalaryInfo tblSalaryInfo) throws URISyntaxException {
        log.debug("REST request to save TblSalaryInfo : {}", tblSalaryInfo);
        if (tblSalaryInfo.getId() != null) {
            throw new BadRequestAlertException("A new tblSalaryInfo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TblSalaryInfo result = tblSalaryInfoService.save(tblSalaryInfo);
        return ResponseEntity
            .created(new URI("/api/tbl-salary-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tbl-salary-infos/:id} : Updates an existing tblSalaryInfo.
     *
     * @param id the id of the tblSalaryInfo to save.
     * @param tblSalaryInfo the tblSalaryInfo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tblSalaryInfo,
     * or with status {@code 400 (Bad Request)} if the tblSalaryInfo is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tblSalaryInfo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tbl-salary-infos/{id}")
    public ResponseEntity<TblSalaryInfo> updateTblSalaryInfo(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TblSalaryInfo tblSalaryInfo
    ) throws URISyntaxException {
        log.debug("REST request to update TblSalaryInfo : {}, {}", id, tblSalaryInfo);
        if (tblSalaryInfo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tblSalaryInfo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tblSalaryInfoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TblSalaryInfo result = tblSalaryInfoService.update(tblSalaryInfo);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tblSalaryInfo.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tbl-salary-infos/:id} : Partial updates given fields of an existing tblSalaryInfo, field will ignore if it is null
     *
     * @param id the id of the tblSalaryInfo to save.
     * @param tblSalaryInfo the tblSalaryInfo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tblSalaryInfo,
     * or with status {@code 400 (Bad Request)} if the tblSalaryInfo is not valid,
     * or with status {@code 404 (Not Found)} if the tblSalaryInfo is not found,
     * or with status {@code 500 (Internal Server Error)} if the tblSalaryInfo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tbl-salary-infos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TblSalaryInfo> partialUpdateTblSalaryInfo(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TblSalaryInfo tblSalaryInfo
    ) throws URISyntaxException {
        log.debug("REST request to partial update TblSalaryInfo partially : {}, {}", id, tblSalaryInfo);
        if (tblSalaryInfo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tblSalaryInfo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tblSalaryInfoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TblSalaryInfo> result = tblSalaryInfoService.partialUpdate(tblSalaryInfo);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tblSalaryInfo.getId().toString())
        );
    }

    /**
     * {@code GET  /tbl-salary-infos} : get all the tblSalaryInfos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tblSalaryInfos in body.
     */
    @GetMapping("/tbl-salary-infos")
    public ResponseEntity<List<TblSalaryInfo>> getAllTblSalaryInfos(
        TblSalaryInfoCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get TblSalaryInfos by criteria: {}", criteria);
        Page<TblSalaryInfo> page = tblSalaryInfoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tbl-salary-infos/count} : count all the tblSalaryInfos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/tbl-salary-infos/count")
    public ResponseEntity<Long> countTblSalaryInfos(TblSalaryInfoCriteria criteria) {
        log.debug("REST request to count TblSalaryInfos by criteria: {}", criteria);
        return ResponseEntity.ok().body(tblSalaryInfoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tbl-salary-infos/:id} : get the "id" tblSalaryInfo.
     *
     * @param id the id of the tblSalaryInfo to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tblSalaryInfo, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tbl-salary-infos/{id}")
    public ResponseEntity<TblSalaryInfo> getTblSalaryInfo(@PathVariable Long id) {
        log.debug("REST request to get TblSalaryInfo : {}", id);
        Optional<TblSalaryInfo> tblSalaryInfo = tblSalaryInfoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tblSalaryInfo);
    }

    /**
     * {@code DELETE  /tbl-salary-infos/:id} : delete the "id" tblSalaryInfo.
     *
     * @param id the id of the tblSalaryInfo to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tbl-salary-infos/{id}")
    public ResponseEntity<Void> deleteTblSalaryInfo(@PathVariable Long id) {
        log.debug("REST request to delete TblSalaryInfo : {}", id);
        tblSalaryInfoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
