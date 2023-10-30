package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.TblEmployeeAuthentication;
import com.mycompany.myapp.repository.TblEmployeeAuthenticationRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TblEmployeeAuthentication}.
 */
@Service
@Transactional
public class TblEmployeeAuthenticationService {

    private final Logger log = LoggerFactory.getLogger(TblEmployeeAuthenticationService.class);

    private final TblEmployeeAuthenticationRepository tblEmployeeAuthenticationRepository;

    public TblEmployeeAuthenticationService(TblEmployeeAuthenticationRepository tblEmployeeAuthenticationRepository) {
        this.tblEmployeeAuthenticationRepository = tblEmployeeAuthenticationRepository;
    }

    /**
     * Save a tblEmployeeAuthentication.
     *
     * @param tblEmployeeAuthentication the entity to save.
     * @return the persisted entity.
     */
    public TblEmployeeAuthentication save(TblEmployeeAuthentication tblEmployeeAuthentication) {
        log.debug("Request to save TblEmployeeAuthentication : {}", tblEmployeeAuthentication);
        return tblEmployeeAuthenticationRepository.save(tblEmployeeAuthentication);
    }

    /**
     * Update a tblEmployeeAuthentication.
     *
     * @param tblEmployeeAuthentication the entity to save.
     * @return the persisted entity.
     */
    public TblEmployeeAuthentication update(TblEmployeeAuthentication tblEmployeeAuthentication) {
        log.debug("Request to update TblEmployeeAuthentication : {}", tblEmployeeAuthentication);
        return tblEmployeeAuthenticationRepository.save(tblEmployeeAuthentication);
    }

    /**
     * Partially update a tblEmployeeAuthentication.
     *
     * @param tblEmployeeAuthentication the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TblEmployeeAuthentication> partialUpdate(TblEmployeeAuthentication tblEmployeeAuthentication) {
        log.debug("Request to partially update TblEmployeeAuthentication : {}", tblEmployeeAuthentication);

        return tblEmployeeAuthenticationRepository
            .findById(tblEmployeeAuthentication.getId())
            .map(existingTblEmployeeAuthentication -> {
                if (tblEmployeeAuthentication.getEmployeeId() != null) {
                    existingTblEmployeeAuthentication.setEmployeeId(tblEmployeeAuthentication.getEmployeeId());
                }
                if (tblEmployeeAuthentication.getRfidId() != null) {
                    existingTblEmployeeAuthentication.setRfidId(tblEmployeeAuthentication.getRfidId());
                }
                if (tblEmployeeAuthentication.getFingerprint() != null) {
                    existingTblEmployeeAuthentication.setFingerprint(tblEmployeeAuthentication.getFingerprint());
                }
                if (tblEmployeeAuthentication.getIsActive() != null) {
                    existingTblEmployeeAuthentication.setIsActive(tblEmployeeAuthentication.getIsActive());
                }

                return existingTblEmployeeAuthentication;
            })
            .map(tblEmployeeAuthenticationRepository::save);
    }

    /**
     * Get all the tblEmployeeAuthentications.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TblEmployeeAuthentication> findAll(Pageable pageable) {
        log.debug("Request to get all TblEmployeeAuthentications");
        return tblEmployeeAuthenticationRepository.findAll(pageable);
    }

    /**
     * Get one tblEmployeeAuthentication by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TblEmployeeAuthentication> findOne(Long id) {
        log.debug("Request to get TblEmployeeAuthentication : {}", id);
        return tblEmployeeAuthenticationRepository.findById(id);
    }

    /**
     * Delete the tblEmployeeAuthentication by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TblEmployeeAuthentication : {}", id);
        tblEmployeeAuthenticationRepository.deleteById(id);
    }
}
