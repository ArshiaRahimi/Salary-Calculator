package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.TblSensorReadings;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TblSensorReadings entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TblSensorReadingsRepository extends JpaRepository<TblSensorReadings, Long> {}
