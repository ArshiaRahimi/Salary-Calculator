package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.TblSensorReadings;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TblSensorReadings entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TblSensorReadingsRepository extends JpaRepository<TblSensorReadings, Long> {
    @Query(value = "SELECT * FROM TBL_SENSOR_READINGS WHERE READING_TIME BETWEEN :startDate AND :endDate", nativeQuery = true)
    List<TblSensorReadings> findReadingsBetweenDates(@Param("startDate") String startDate, @Param("endDate") String endDate);
}
