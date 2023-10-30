package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.TblSalaryInfo;
import com.mycompany.myapp.repository.TblSalaryInfoRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TblSalaryInfo}.
 */
@Service
@Transactional
public class TblSalaryInfoService {

    private final Logger log = LoggerFactory.getLogger(TblSalaryInfoService.class);

    private final TblSalaryInfoRepository tblSalaryInfoRepository;

    public TblSalaryInfoService(TblSalaryInfoRepository tblSalaryInfoRepository) {
        this.tblSalaryInfoRepository = tblSalaryInfoRepository;
    }

    /**
     * Save a tblSalaryInfo.
     *
     * @param tblSalaryInfo the entity to save.
     * @return the persisted entity.
     */
    public TblSalaryInfo save(TblSalaryInfo tblSalaryInfo) {
        log.debug("Request to save TblSalaryInfo : {}", tblSalaryInfo);
        return tblSalaryInfoRepository.save(tblSalaryInfo);
    }

    /**
     * Update a tblSalaryInfo.
     *
     * @param tblSalaryInfo the entity to save.
     * @return the persisted entity.
     */
    public TblSalaryInfo update(TblSalaryInfo tblSalaryInfo) {
        log.debug("Request to update TblSalaryInfo : {}", tblSalaryInfo);
        return tblSalaryInfoRepository.save(tblSalaryInfo);
    }

    /**
     * Partially update a tblSalaryInfo.
     *
     * @param tblSalaryInfo the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TblSalaryInfo> partialUpdate(TblSalaryInfo tblSalaryInfo) {
        log.debug("Request to partially update TblSalaryInfo : {}", tblSalaryInfo);

        return tblSalaryInfoRepository
            .findById(tblSalaryInfo.getId())
            .map(existingTblSalaryInfo -> {
                if (tblSalaryInfo.getEmployeeId() != null) {
                    existingTblSalaryInfo.setEmployeeId(tblSalaryInfo.getEmployeeId());
                }
                if (tblSalaryInfo.getBaseSalary() != null) {
                    existingTblSalaryInfo.setBaseSalary(tblSalaryInfo.getBaseSalary());
                }
                if (tblSalaryInfo.getHousingRights() != null) {
                    existingTblSalaryInfo.setHousingRights(tblSalaryInfo.getHousingRights());
                }
                if (tblSalaryInfo.getInternetRights() != null) {
                    existingTblSalaryInfo.setInternetRights(tblSalaryInfo.getInternetRights());
                }
                if (tblSalaryInfo.getGroceriesRights() != null) {
                    existingTblSalaryInfo.setGroceriesRights(tblSalaryInfo.getGroceriesRights());
                }

                return existingTblSalaryInfo;
            })
            .map(tblSalaryInfoRepository::save);
    }

    /**
     * Get all the tblSalaryInfos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TblSalaryInfo> findAll(Pageable pageable) {
        log.debug("Request to get all TblSalaryInfos");
        return tblSalaryInfoRepository.findAll(pageable);
    }

    /**
     * Get one tblSalaryInfo by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TblSalaryInfo> findOne(Long id) {
        log.debug("Request to get TblSalaryInfo : {}", id);
        return tblSalaryInfoRepository.findById(id);
    }

    /**
     * Delete the tblSalaryInfo by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TblSalaryInfo : {}", id);
        tblSalaryInfoRepository.deleteById(id);
    }
}
