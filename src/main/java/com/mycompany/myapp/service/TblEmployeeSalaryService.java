package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.TblEmployeeSalary;
import com.mycompany.myapp.repository.TblEmployeeSalaryRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TblEmployeeSalary}.
 */
@Service
@Transactional
public class TblEmployeeSalaryService {

    private final Logger log = LoggerFactory.getLogger(TblEmployeeSalaryService.class);

    private final TblEmployeeSalaryRepository tblEmployeeSalaryRepository;

    public TblEmployeeSalaryService(TblEmployeeSalaryRepository tblEmployeeSalaryRepository) {
        this.tblEmployeeSalaryRepository = tblEmployeeSalaryRepository;
    }

    /**
     * Save a tblEmployeeSalary.
     *
     * @param tblEmployeeSalary the entity to save.
     * @return the persisted entity.
     */
    public TblEmployeeSalary save(TblEmployeeSalary tblEmployeeSalary) {
        log.debug("Request to save TblEmployeeSalary : {}", tblEmployeeSalary);
        return tblEmployeeSalaryRepository.save(tblEmployeeSalary);
    }

    /**
     * Update a tblEmployeeSalary.
     *
     * @param tblEmployeeSalary the entity to save.
     * @return the persisted entity.
     */
    public TblEmployeeSalary update(TblEmployeeSalary tblEmployeeSalary) {
        log.debug("Request to update TblEmployeeSalary : {}", tblEmployeeSalary);
        return tblEmployeeSalaryRepository.save(tblEmployeeSalary);
    }

    /**
     * Partially update a tblEmployeeSalary.
     *
     * @param tblEmployeeSalary the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TblEmployeeSalary> partialUpdate(TblEmployeeSalary tblEmployeeSalary) {
        log.debug("Request to partially update TblEmployeeSalary : {}", tblEmployeeSalary);

        return tblEmployeeSalaryRepository
            .findById(tblEmployeeSalary.getId())
            .map(existingTblEmployeeSalary -> {
                if (tblEmployeeSalary.getDateCalculated() != null) {
                    existingTblEmployeeSalary.setDateCalculated(tblEmployeeSalary.getDateCalculated());
                }
                if (tblEmployeeSalary.getEmployeeId() != null) {
                    existingTblEmployeeSalary.setEmployeeId(tblEmployeeSalary.getEmployeeId());
                }
                if (tblEmployeeSalary.getUndertime() != null) {
                    existingTblEmployeeSalary.setUndertime(tblEmployeeSalary.getUndertime());
                }
                if (tblEmployeeSalary.getOvertime() != null) {
                    existingTblEmployeeSalary.setOvertime(tblEmployeeSalary.getOvertime());
                }
                if (tblEmployeeSalary.getOvertimeOffday() != null) {
                    existingTblEmployeeSalary.setOvertimeOffday(tblEmployeeSalary.getOvertimeOffday());
                }
                if (tblEmployeeSalary.getTotalSalary() != null) {
                    existingTblEmployeeSalary.setTotalSalary(tblEmployeeSalary.getTotalSalary());
                }

                return existingTblEmployeeSalary;
            })
            .map(tblEmployeeSalaryRepository::save);
    }

    /**
     * Get all the tblEmployeeSalaries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TblEmployeeSalary> findAll(Pageable pageable) {
        log.debug("Request to get all TblEmployeeSalaries");
        return tblEmployeeSalaryRepository.findAll(pageable);
    }

    /**
     * Get one tblEmployeeSalary by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TblEmployeeSalary> findOne(Long id) {
        log.debug("Request to get TblEmployeeSalary : {}", id);
        return tblEmployeeSalaryRepository.findById(id);
    }

    /**
     * Delete the tblEmployeeSalary by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TblEmployeeSalary : {}", id);
        tblEmployeeSalaryRepository.deleteById(id);
    }
}
