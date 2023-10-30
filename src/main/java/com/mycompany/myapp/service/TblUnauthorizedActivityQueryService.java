package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.TblUnauthorizedActivity;
import com.mycompany.myapp.repository.TblUnauthorizedActivityRepository;
import com.mycompany.myapp.service.criteria.TblUnauthorizedActivityCriteria;
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
 * Service for executing complex queries for {@link TblUnauthorizedActivity} entities in the database.
 * The main input is a {@link TblUnauthorizedActivityCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TblUnauthorizedActivity} or a {@link Page} of {@link TblUnauthorizedActivity} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TblUnauthorizedActivityQueryService extends QueryService<TblUnauthorizedActivity> {

    private final Logger log = LoggerFactory.getLogger(TblUnauthorizedActivityQueryService.class);

    private final TblUnauthorizedActivityRepository tblUnauthorizedActivityRepository;

    public TblUnauthorizedActivityQueryService(TblUnauthorizedActivityRepository tblUnauthorizedActivityRepository) {
        this.tblUnauthorizedActivityRepository = tblUnauthorizedActivityRepository;
    }

    /**
     * Return a {@link List} of {@link TblUnauthorizedActivity} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TblUnauthorizedActivity> findByCriteria(TblUnauthorizedActivityCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TblUnauthorizedActivity> specification = createSpecification(criteria);
        return tblUnauthorizedActivityRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link TblUnauthorizedActivity} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TblUnauthorizedActivity> findByCriteria(TblUnauthorizedActivityCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TblUnauthorizedActivity> specification = createSpecification(criteria);
        return tblUnauthorizedActivityRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TblUnauthorizedActivityCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TblUnauthorizedActivity> specification = createSpecification(criteria);
        return tblUnauthorizedActivityRepository.count(specification);
    }

    /**
     * Function to convert {@link TblUnauthorizedActivityCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TblUnauthorizedActivity> createSpecification(TblUnauthorizedActivityCriteria criteria) {
        Specification<TblUnauthorizedActivity> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TblUnauthorizedActivity_.id));
            }
            if (criteria.getRfidId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRfidId(), TblUnauthorizedActivity_.rfidId));
            }
            if (criteria.getEmployeeId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEmployeeId(), TblUnauthorizedActivity_.employeeId));
            }
            if (criteria.getReadingTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReadingTime(), TblUnauthorizedActivity_.readingTime));
            }
            if (criteria.getAttempt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAttempt(), TblUnauthorizedActivity_.attempt));
            }
        }
        return specification;
    }
}
