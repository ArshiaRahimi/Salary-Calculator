package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.TblWorkingHours;
import com.mycompany.myapp.repository.TblWorkingHoursRepository;
import com.mycompany.myapp.service.criteria.TblWorkingHoursCriteria;
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
 * Service for executing complex queries for {@link TblWorkingHours} entities in the database.
 * The main input is a {@link TblWorkingHoursCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TblWorkingHours} or a {@link Page} of {@link TblWorkingHours} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TblWorkingHoursQueryService extends QueryService<TblWorkingHours> {

    private final Logger log = LoggerFactory.getLogger(TblWorkingHoursQueryService.class);

    private final TblWorkingHoursRepository tblWorkingHoursRepository;

    public TblWorkingHoursQueryService(TblWorkingHoursRepository tblWorkingHoursRepository) {
        this.tblWorkingHoursRepository = tblWorkingHoursRepository;
    }

    /**
     * Return a {@link List} of {@link TblWorkingHours} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TblWorkingHours> findByCriteria(TblWorkingHoursCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TblWorkingHours> specification = createSpecification(criteria);
        return tblWorkingHoursRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link TblWorkingHours} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TblWorkingHours> findByCriteria(TblWorkingHoursCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TblWorkingHours> specification = createSpecification(criteria);
        return tblWorkingHoursRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TblWorkingHoursCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TblWorkingHours> specification = createSpecification(criteria);
        return tblWorkingHoursRepository.count(specification);
    }

    /**
     * Function to convert {@link TblWorkingHoursCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TblWorkingHours> createSpecification(TblWorkingHoursCriteria criteria) {
        Specification<TblWorkingHours> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TblWorkingHours_.id));
            }
            if (criteria.getDay() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDay(), TblWorkingHours_.day));
            }
            if (criteria.getStartTimeHour() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartTimeHour(), TblWorkingHours_.startTimeHour));
            }
            if (criteria.getStartTimeMinute() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartTimeMinute(), TblWorkingHours_.startTimeMinute));
            }
            if (criteria.getEndTimeHour() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndTimeHour(), TblWorkingHours_.endTimeHour));
            }
            if (criteria.getEndTimeMinute() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndTimeMinute(), TblWorkingHours_.endTimeMinute));
            }
        }
        return specification;
    }
}
