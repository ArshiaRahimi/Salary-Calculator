package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.TblUnauthorizedActivity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TblUnauthorizedActivity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TblUnauthorizedActivityRepository
    extends JpaRepository<TblUnauthorizedActivity, Long>, JpaSpecificationExecutor<TblUnauthorizedActivity> {
    @Query("select max(e.attempt) from TblUnauthorizedActivity e where e.rfidId = ?1")
    Long fetchAttemptByRfidID(String rfidId);
}
