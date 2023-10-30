package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.TblSalaryInfo;
import com.mycompany.myapp.repository.TblSalaryInfoRepository;
import com.mycompany.myapp.service.criteria.TblSalaryInfoCriteria;
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
 * Service for executing complex queries for {@link TblSalaryInfo} entities in the database.
 * The main input is a {@link TblSalaryInfoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TblSalaryInfo} or a {@link Page} of {@link TblSalaryInfo} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TblSalaryInfoQueryService extends QueryService<TblSalaryInfo> {

    private final Logger log = LoggerFactory.getLogger(TblSalaryInfoQueryService.class);

    private final TblSalaryInfoRepository tblSalaryInfoRepository;

    public TblSalaryInfoQueryService(TblSalaryInfoRepository tblSalaryInfoRepository) {
        this.tblSalaryInfoRepository = tblSalaryInfoRepository;
    }

    /**
     * Return a {@link List} of {@link TblSalaryInfo} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TblSalaryInfo> findByCriteria(TblSalaryInfoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TblSalaryInfo> specification = createSpecification(criteria);
        return tblSalaryInfoRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link TblSalaryInfo} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TblSalaryInfo> findByCriteria(TblSalaryInfoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TblSalaryInfo> specification = createSpecification(criteria);
        return tblSalaryInfoRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TblSalaryInfoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TblSalaryInfo> specification = createSpecification(criteria);
        return tblSalaryInfoRepository.count(specification);
    }

    /**
     * Function to convert {@link TblSalaryInfoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TblSalaryInfo> createSpecification(TblSalaryInfoCriteria criteria) {
        Specification<TblSalaryInfo> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TblSalaryInfo_.id));
            }
            if (criteria.getEmployeeId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEmployeeId(), TblSalaryInfo_.employeeId));
            }
            if (criteria.getBaseSalary() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBaseSalary(), TblSalaryInfo_.baseSalary));
            }
            if (criteria.getHousingRights() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHousingRights(), TblSalaryInfo_.housingRights));
            }
            if (criteria.getInternetRights() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getInternetRights(), TblSalaryInfo_.internetRights));
            }
            if (criteria.getGroceriesRights() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getGroceriesRights(), TblSalaryInfo_.groceriesRights));
            }
        }
        return specification;
    }
}
