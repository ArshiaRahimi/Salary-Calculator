package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.TblOffDay;
import com.mycompany.myapp.repository.TblOffDayRepository;
import com.mycompany.myapp.service.criteria.TblOffDayCriteria;
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
 * Service for executing complex queries for {@link TblOffDay} entities in the database.
 * The main input is a {@link TblOffDayCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TblOffDay} or a {@link Page} of {@link TblOffDay} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TblOffDayQueryService extends QueryService<TblOffDay> {

    private final Logger log = LoggerFactory.getLogger(TblOffDayQueryService.class);

    private final TblOffDayRepository tblOffDayRepository;

    public TblOffDayQueryService(TblOffDayRepository tblOffDayRepository) {
        this.tblOffDayRepository = tblOffDayRepository;
    }

    /**
     * Return a {@link List} of {@link TblOffDay} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TblOffDay> findByCriteria(TblOffDayCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TblOffDay> specification = createSpecification(criteria);
        return tblOffDayRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link TblOffDay} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TblOffDay> findByCriteria(TblOffDayCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TblOffDay> specification = createSpecification(criteria);
        return tblOffDayRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TblOffDayCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TblOffDay> specification = createSpecification(criteria);
        return tblOffDayRepository.count(specification);
    }

    /**
     * Function to convert {@link TblOffDayCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TblOffDay> createSpecification(TblOffDayCriteria criteria) {
        Specification<TblOffDay> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TblOffDay_.id));
            }
            if (criteria.getDay() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDay(), TblOffDay_.day));
            }
            if (criteria.getMonth() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMonth(), TblOffDay_.month));
            }
            if (criteria.getYear() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getYear(), TblOffDay_.year));
            }
        }
        return specification;
    }
}
