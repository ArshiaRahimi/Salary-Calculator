package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.TblWorkingHours;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TblWorkingHours entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TblWorkingHoursRepository extends JpaRepository<TblWorkingHours, Long>, JpaSpecificationExecutor<TblWorkingHours> {
    @Query("select e from TblWorkingHours e where e.day = ?1")
    TblWorkingHours findTblWorkingHoursByDayName(String name);
}
