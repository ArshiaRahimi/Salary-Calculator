package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.TblOffDay;
import com.mycompany.myapp.repository.TblOffDayRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TblOffDay}.
 */
@Service
@Transactional
public class TblOffDayService {

    private final Logger log = LoggerFactory.getLogger(TblOffDayService.class);

    private final TblOffDayRepository tblOffDayRepository;

    public TblOffDayService(TblOffDayRepository tblOffDayRepository) {
        this.tblOffDayRepository = tblOffDayRepository;
    }

    /**
     * Save a tblOffDay.
     *
     * @param tblOffDay the entity to save.
     * @return the persisted entity.
     */
    public TblOffDay save(TblOffDay tblOffDay) {
        log.debug("Request to save TblOffDay : {}", tblOffDay);
        return tblOffDayRepository.save(tblOffDay);
    }

    /**
     * Update a tblOffDay.
     *
     * @param tblOffDay the entity to save.
     * @return the persisted entity.
     */
    public TblOffDay update(TblOffDay tblOffDay) {
        log.debug("Request to update TblOffDay : {}", tblOffDay);
        return tblOffDayRepository.save(tblOffDay);
    }

    /**
     * Partially update a tblOffDay.
     *
     * @param tblOffDay the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TblOffDay> partialUpdate(TblOffDay tblOffDay) {
        log.debug("Request to partially update TblOffDay : {}", tblOffDay);

        return tblOffDayRepository
            .findById(tblOffDay.getId())
            .map(existingTblOffDay -> {
                if (tblOffDay.getDay() != null) {
                    existingTblOffDay.setDay(tblOffDay.getDay());
                }
                if (tblOffDay.getMonth() != null) {
                    existingTblOffDay.setMonth(tblOffDay.getMonth());
                }
                if (tblOffDay.getYear() != null) {
                    existingTblOffDay.setYear(tblOffDay.getYear());
                }

                return existingTblOffDay;
            })
            .map(tblOffDayRepository::save);
    }

    /**
     * Get all the tblOffDays.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TblOffDay> findAll(Pageable pageable) {
        log.debug("Request to get all TblOffDays");
        return tblOffDayRepository.findAll(pageable);
    }

    /**
     * Get one tblOffDay by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TblOffDay> findOne(Long id) {
        log.debug("Request to get TblOffDay : {}", id);
        return tblOffDayRepository.findById(id);
    }

    /**
     * Delete the tblOffDay by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TblOffDay : {}", id);
        tblOffDayRepository.deleteById(id);
    }
}
