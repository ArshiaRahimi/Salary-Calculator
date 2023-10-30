package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.TblEmployeeAuthentication;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TblEmployeeAuthentication entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TblEmployeeAuthenticationRepository
    extends JpaRepository<TblEmployeeAuthentication, Long>, JpaSpecificationExecutor<TblEmployeeAuthentication> {
    @Query("select e.employeeId from TblEmployeeAuthentication e where e.rfidId = ?1")
    Long findEmployeeIdByRfidId(String rfidId);

    @Query("select e from TblEmployeeAuthentication e where e.rfidId = ?1")
    TblEmployeeAuthentication findByRfidId(String rfidId);
}
