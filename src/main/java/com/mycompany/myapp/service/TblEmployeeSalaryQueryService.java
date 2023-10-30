package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.TblEmployeeSalary;
import com.mycompany.myapp.repository.TblEmployeeSalaryRepository;
import com.mycompany.myapp.service.criteria.TblEmployeeSalaryCriteria;
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
 * Service for executing complex queries for {@link TblEmployeeSalary} entities in the database.
 * The main input is a {@link TblEmployeeSalaryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TblEmployeeSalary} or a {@link Page} of {@link TblEmployeeSalary} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TblEmployeeSalaryQueryService extends QueryService<TblEmployeeSalary> {

    private final Logger log = LoggerFactory.getLogger(TblEmployeeSalaryQueryService.class);

    private final TblEmployeeSalaryRepository tblEmployeeSalaryRepository;

    public TblEmployeeSalaryQueryService(TblEmployeeSalaryRepository tblEmployeeSalaryRepository) {
        this.tblEmployeeSalaryRepository = tblEmployeeSalaryRepository;
    }

    /**
     * Return a {@link List} of {@link TblEmployeeSalary} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TblEmployeeSalary> findByCriteria(TblEmployeeSalaryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TblEmployeeSalary> specification = createSpecification(criteria);
        return tblEmployeeSalaryRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link TblEmployeeSalary} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TblEmployeeSalary> findByCriteria(TblEmployeeSalaryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TblEmployeeSalary> specification = createSpecification(criteria);
        return tblEmployeeSalaryRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TblEmployeeSalaryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TblEmployeeSalary> specification = createSpecification(criteria);
        return tblEmployeeSalaryRepository.count(specification);
    }

    /**
     * Function to convert {@link TblEmployeeSalaryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TblEmployeeSalary> createSpecification(TblEmployeeSalaryCriteria criteria) {
        Specification<TblEmployeeSalary> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TblEmployeeSalary_.id));
            }
            if (criteria.getDateCalculated() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateCalculated(), TblEmployeeSalary_.dateCalculated));
            }
            if (criteria.getEmployeeId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEmployeeId(), TblEmployeeSalary_.employeeId));
            }
            if (criteria.getUndertime() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUndertime(), TblEmployeeSalary_.undertime));
            }
            if (criteria.getOvertime() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOvertime(), TblEmployeeSalary_.overtime));
            }
            if (criteria.getOvertimeOffday() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getOvertimeOffday(), TblEmployeeSalary_.overtimeOffday));
            }
            if (criteria.getTotalSalary() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTotalSalary(), TblEmployeeSalary_.totalSalary));
            }
        }
        return specification;
    }
}
