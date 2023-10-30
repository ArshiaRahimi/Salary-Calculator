package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.TblSalaryInfo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TblSalaryInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TblSalaryInfoRepository extends JpaRepository<TblSalaryInfo, Long>, JpaSpecificationExecutor<TblSalaryInfo> {
    @Query("select e from TblSalaryInfo e where e.employeeId = ?1")
    TblSalaryInfo findTblSalaryInfoByEmployeeID(Integer id);
}
