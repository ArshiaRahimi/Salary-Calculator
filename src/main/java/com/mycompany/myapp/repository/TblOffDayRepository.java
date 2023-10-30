package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.TblOffDay;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TblOffDay entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TblOffDayRepository extends JpaRepository<TblOffDay, Long>, JpaSpecificationExecutor<TblOffDay> {}
