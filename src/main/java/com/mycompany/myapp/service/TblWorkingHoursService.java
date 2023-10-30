package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.TblWorkingHours;
import com.mycompany.myapp.repository.TblWorkingHoursRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TblWorkingHours}.
 */
@Service
@Transactional
public class TblWorkingHoursService {

    private final Logger log = LoggerFactory.getLogger(TblWorkingHoursService.class);

    private final TblWorkingHoursRepository tblWorkingHoursRepository;

    public TblWorkingHoursService(TblWorkingHoursRepository tblWorkingHoursRepository) {
        this.tblWorkingHoursRepository = tblWorkingHoursRepository;
    }

    /**
     * Save a tblWorkingHours.
     *
     * @param tblWorkingHours the entity to save.
     * @return the persisted entity.
     */
    public TblWorkingHours save(TblWorkingHours tblWorkingHours) {
        log.debug("Request to save TblWorkingHours : {}", tblWorkingHours);
        return tblWorkingHoursRepository.save(tblWorkingHours);
    }

    /**
     * Update a tblWorkingHours.
     *
     * @param tblWorkingHours the entity to save.
     * @return the persisted entity.
     */
    public TblWorkingHours update(TblWorkingHours tblWorkingHours) {
        log.debug("Request to update TblWorkingHours : {}", tblWorkingHours);
        return tblWorkingHoursRepository.save(tblWorkingHours);
    }

    /**
     * Partially update a tblWorkingHours.
     *
     * @param tblWorkingHours the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TblWorkingHours> partialUpdate(TblWorkingHours tblWorkingHours) {
        log.debug("Request to partially update TblWorkingHours : {}", tblWorkingHours);

        return tblWorkingHoursRepository
            .findById(tblWorkingHours.getId())
            .map(existingTblWorkingHours -> {
                if (tblWorkingHours.getDay() != null) {
                    existingTblWorkingHours.setDay(tblWorkingHours.getDay());
                }
                if (tblWorkingHours.getStartTimeHour() != null) {
                    existingTblWorkingHours.setStartTimeHour(tblWorkingHours.getStartTimeHour());
                }
                if (tblWorkingHours.getStartTimeMinute() != null) {
                    existingTblWorkingHours.setStartTimeMinute(tblWorkingHours.getStartTimeMinute());
                }
                if (tblWorkingHours.getEndTimeHour() != null) {
                    existingTblWorkingHours.setEndTimeHour(tblWorkingHours.getEndTimeHour());
                }
                if (tblWorkingHours.getEndTimeMinute() != null) {
                    existingTblWorkingHours.setEndTimeMinute(tblWorkingHours.getEndTimeMinute());
                }

                return existingTblWorkingHours;
            })
            .map(tblWorkingHoursRepository::save);
    }

    /**
     * Get all the tblWorkingHours.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TblWorkingHours> findAll(Pageable pageable) {
        log.debug("Request to get all TblWorkingHours");
        return tblWorkingHoursRepository.findAll(pageable);
    }

    /**
     * Get one tblWorkingHours by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TblWorkingHours> findOne(Long id) {
        log.debug("Request to get TblWorkingHours : {}", id);
        return tblWorkingHoursRepository.findById(id);
    }

    /**
     * Delete the tblWorkingHours by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TblWorkingHours : {}", id);
        tblWorkingHoursRepository.deleteById(id);
    }
}
