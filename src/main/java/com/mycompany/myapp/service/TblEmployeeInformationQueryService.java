package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.TblEmployeeInformation;
import com.mycompany.myapp.repository.TblEmployeeInformationRepository;
import com.mycompany.myapp.service.criteria.TblEmployeeInformationCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link TblEmployeeInformation} entities in the database.
 * The main input is a {@link TblEmployeeInformationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TblEmployeeInformation} or a {@link Page} of {@link TblEmployeeInformation} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TblEmployeeInformationQueryService extends QueryService<TblEmployeeInformation> {

    private final Logger log = LoggerFactory.getLogger(TblEmployeeInformationQueryService.class);

    private final TblEmployeeInformationRepository tblEmployeeInformationRepository;

    public TblEmployeeInformationQueryService(TblEmployeeInformationRepository tblEmployeeInformationRepository) {
        this.tblEmployeeInformationRepository = tblEmployeeInformationRepository;
    }

    /**
     * Return a {@link List} of {@link TblEmployeeInformation} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TblEmployeeInformation> findByCriteria(TblEmployeeInformationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TblEmployeeInformation> specification = createSpecification(criteria);
        return tblEmployeeInformationRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link TblEmployeeInformation} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TblEmployeeInformation> findByCriteria(TblEmployeeInformationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TblEmployeeInformation> specification = createSpecification(criteria);
        return tblEmployeeInformationRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TblEmployeeInformationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TblEmployeeInformation> specification = createSpecification(criteria);
        return tblEmployeeInformationRepository.count(specification);
    }

    /**
     * Function to convert {@link TblEmployeeInformationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TblEmployeeInformation> createSpecification(TblEmployeeInformationCriteria criteria) {
        Specification<TblEmployeeInformation> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TblEmployeeInformation_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), TblEmployeeInformation_.name));
            }
            if (criteria.getFamilyName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFamilyName(), TblEmployeeInformation_.familyName));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), TblEmployeeInformation_.phoneNumber));
            }
        }
        return specification;
    }
}
