package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.TblUnauthorizedActivity;
import com.mycompany.myapp.repository.TblUnauthorizedActivityRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TblUnauthorizedActivity}.
 */
@Service
@Transactional
public class TblUnauthorizedActivityService {

    private final Logger log = LoggerFactory.getLogger(TblUnauthorizedActivityService.class);

    private final TblUnauthorizedActivityRepository tblUnauthorizedActivityRepository;

    public TblUnauthorizedActivityService(TblUnauthorizedActivityRepository tblUnauthorizedActivityRepository) {
        this.tblUnauthorizedActivityRepository = tblUnauthorizedActivityRepository;
    }

    /**
     * Save a tblUnauthorizedActivity.
     *
     * @param tblUnauthorizedActivity the entity to save.
     * @return the persisted entity.
     */
    public TblUnauthorizedActivity save(TblUnauthorizedActivity tblUnauthorizedActivity) {
        log.debug("Request to save TblUnauthorizedActivity : {}", tblUnauthorizedActivity);
        return tblUnauthorizedActivityRepository.save(tblUnauthorizedActivity);
    }

    /**
     * Update a tblUnauthorizedActivity.
     *
     * @param tblUnauthorizedActivity the entity to save.
     * @return the persisted entity.
     */
    public TblUnauthorizedActivity update(TblUnauthorizedActivity tblUnauthorizedActivity) {
        log.debug("Request to update TblUnauthorizedActivity : {}", tblUnauthorizedActivity);
        return tblUnauthorizedActivityRepository.save(tblUnauthorizedActivity);
    }

    /**
     * Partially update a tblUnauthorizedActivity.
     *
     * @param tblUnauthorizedActivity the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TblUnauthorizedActivity> partialUpdate(TblUnauthorizedActivity tblUnauthorizedActivity) {
        log.debug("Request to partially update TblUnauthorizedActivity : {}", tblUnauthorizedActivity);

        return tblUnauthorizedActivityRepository
            .findById(tblUnauthorizedActivity.getId())
            .map(existingTblUnauthorizedActivity -> {
                if (tblUnauthorizedActivity.getRfidId() != null) {
                    existingTblUnauthorizedActivity.setRfidId(tblUnauthorizedActivity.getRfidId());
                }
                if (tblUnauthorizedActivity.getEmployeeId() != null) {
                    existingTblUnauthorizedActivity.setEmployeeId(tblUnauthorizedActivity.getEmployeeId());
                }
                if (tblUnauthorizedActivity.getReadingTime() != null) {
                    existingTblUnauthorizedActivity.setReadingTime(tblUnauthorizedActivity.getReadingTime());
                }
                if (tblUnauthorizedActivity.getAttempt() != null) {
                    existingTblUnauthorizedActivity.setAttempt(tblUnauthorizedActivity.getAttempt());
                }
                if (tblUnauthorizedActivity.getFingerprint() != null) {
                    existingTblUnauthorizedActivity.setFingerprint(tblUnauthorizedActivity.getFingerprint());
                }

                return existingTblUnauthorizedActivity;
            })
            .map(tblUnauthorizedActivityRepository::save);
    }

    /**
     * Get all the tblUnauthorizedActivities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TblUnauthorizedActivity> findAll(Pageable pageable) {
        log.debug("Request to get all TblUnauthorizedActivities");
        return tblUnauthorizedActivityRepository.findAll(pageable);
    }

    /**
     * Get one tblUnauthorizedActivity by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TblUnauthorizedActivity> findOne(Long id) {
        log.debug("Request to get TblUnauthorizedActivity : {}", id);
        return tblUnauthorizedActivityRepository.findById(id);
    }

    /**
     * Delete the tblUnauthorizedActivity by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TblUnauthorizedActivity : {}", id);
        tblUnauthorizedActivityRepository.deleteById(id);
    }
}
