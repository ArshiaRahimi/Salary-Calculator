package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.TblEmployeeSalary;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TblEmployeeSalary entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TblEmployeeSalaryRepository extends JpaRepository<TblEmployeeSalary, Long>, JpaSpecificationExecutor<TblEmployeeSalary> {}
