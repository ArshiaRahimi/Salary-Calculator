package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.TblEmployeeInformation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TblEmployeeInformation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TblEmployeeInformationRepository
    extends JpaRepository<TblEmployeeInformation, Long>, JpaSpecificationExecutor<TblEmployeeInformation> {}
