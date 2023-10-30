package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.TblSensorReadings;
import com.mycompany.myapp.domain.TblUnauthorizedActivity;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
public class SensorReadingInsertRepo {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void insertWithQuery(TblSensorReadings sensorReadings) {
        LocalDateTime ldt = sensorReadings.getReadingTime().toLocalDateTime();
        Instant instant = sensorReadings.getReadingTime().toInstant();
        entityManager
            .createNativeQuery("INSERT INTO tbl_sensor_readings (employee_id, reading_time) VALUES (?,?)")
            //.setParameter(1, sensorReadings.getId())
            .setParameter(1, sensorReadings.getEmployeeId())
            .setParameter(2, Date.from(instant))
            .executeUpdate();
    }

    @Transactional
    public void insertUnAuthorized(TblUnauthorizedActivity unauthorizedActivity) {
        LocalDateTime ldt = unauthorizedActivity.getReadingTime().toLocalDateTime();
        Instant instant = unauthorizedActivity.getReadingTime().toInstant();
        entityManager
            .createNativeQuery(
                "INSERT INTO tbl_unauthorized_activity (rfid_id, employee_id, reading_time, attempt, fingerprint) VALUES (?,?,?,?,?)"
            )
            //.setParameter(1, sensorReadings.getId())
            .setParameter(1, unauthorizedActivity.getRfidId())
            .setParameter(2, unauthorizedActivity.getEmployeeId())
            .setParameter(3, Date.from(instant))
            .setParameter(4, unauthorizedActivity.getAttempt())
            .setParameter(5, unauthorizedActivity.getFingerprint())
            .executeUpdate();
    }
}
