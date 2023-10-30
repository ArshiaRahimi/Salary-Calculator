package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.TblEmployeeInformation;
import com.mycompany.myapp.repository.TblEmployeeInformationRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TblEmployeeInformation}.
 */
@Service
@Transactional
public class TblEmployeeInformationService {

    private final Logger log = LoggerFactory.getLogger(TblEmployeeInformationService.class);

    private final TblEmployeeInformationRepository tblEmployeeInformationRepository;

    public TblEmployeeInformationService(TblEmployeeInformationRepository tblEmployeeInformationRepository) {
        this.tblEmployeeInformationRepository = tblEmployeeInformationRepository;
    }

    /**
     * Save a tblEmployeeInformation.
     *
     * @param tblEmployeeInformation the entity to save.
     * @return the persisted entity.
     */
    public TblEmployeeInformation save(TblEmployeeInformation tblEmployeeInformation) {
        log.debug("Request to save TblEmployeeInformation : {}", tblEmployeeInformation);
        return tblEmployeeInformationRepository.save(tblEmployeeInformation);
    }

    /**
     * Update a tblEmployeeInformation.
     *
     * @param tblEmployeeInformation the entity to save.
     * @return the persisted entity.
     */
    public TblEmployeeInformation update(TblEmployeeInformation tblEmployeeInformation) {
        log.debug("Request to update TblEmployeeInformation : {}", tblEmployeeInformation);
        return tblEmployeeInformationRepository.save(tblEmployeeInformation);
    }

    /**
     * Partially update a tblEmployeeInformation.
     *
     * @param tblEmployeeInformation the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TblEmployeeInformation> partialUpdate(TblEmployeeInformation tblEmployeeInformation) {
        log.debug("Request to partially update TblEmployeeInformation : {}", tblEmployeeInformation);

        return tblEmployeeInformationRepository
            .findById(tblEmployeeInformation.getId())
            .map(existingTblEmployeeInformation -> {
                if (tblEmployeeInformation.getName() != null) {
                    existingTblEmployeeInformation.setName(tblEmployeeInformation.getName());
                }
                if (tblEmployeeInformation.getFamilyName() != null) {
                    existingTblEmployeeInformation.setFamilyName(tblEmployeeInformation.getFamilyName());
                }
                if (tblEmployeeInformation.getPhoneNumber() != null) {
                    existingTblEmployeeInformation.setPhoneNumber(tblEmployeeInformation.getPhoneNumber());
                }

                return existingTblEmployeeInformation;
            })
            .map(tblEmployeeInformationRepository::save);
    }

    /**
     * Get all the tblEmployeeInformations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TblEmployeeInformation> findAll(Pageable pageable) {
        log.debug("Request to get all TblEmployeeInformations");
        return tblEmployeeInformationRepository.findAll(pageable);
    }

    /**
     * Get one tblEmployeeInformation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TblEmployeeInformation> findOne(Long id) {
        log.debug("Request to get TblEmployeeInformation : {}", id);
        return tblEmployeeInformationRepository.findById(id);
    }

    /**
     * Delete the tblEmployeeInformation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TblEmployeeInformation : {}", id);
        tblEmployeeInformationRepository.deleteById(id);
    }
}
