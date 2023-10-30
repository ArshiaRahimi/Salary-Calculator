package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.TblEmployeeAuthentication;
import com.mycompany.myapp.domain.TblSensorReadings;
import com.mycompany.myapp.domain.TblUnauthorizedActivity;
import com.mycompany.myapp.repository.SensorReadingInsertRepo;
import com.mycompany.myapp.repository.TblEmployeeAuthenticationRepository;
import com.mycompany.myapp.repository.TblSensorReadingsRepository;
import com.mycompany.myapp.repository.TblUnauthorizedActivityRepository;
import com.mycompany.myapp.salary.HttpResponse;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class SensorController {

    @Autowired
    TblSensorReadingsRepository sensorReadingsRepository;

    @Autowired
    TblEmployeeAuthenticationRepository employeeAuthenticationRepository;

    @Autowired
    SensorReadingInsertRepo sensorReadingInsertRepo;

    @Autowired
    TblUnauthorizedActivityRepository unauthorizedActivityRepository;

    @PostMapping("/rfidSensor")
    public String logSensorData(@RequestBody String sensorReading) {
        String[] sensorReadingsString = sensorReading.split(",");
        //Long readTimestamp = Long.parseLong(sensorReadingsString[0]);
        Date date = new Date();
        String sensorId = sensorReadingsString[1];
        Long employeeId = employeeAuthenticationRepository.findEmployeeIdByRfidId(sensorId);
        if (employeeId == null) {
            return "invalid sensor data";
        }

        LocalDateTime morningDateTime = LocalDateTime.now();
        ZonedDateTime zdt = morningDateTime.atZone(ZoneId.systemDefault());

        TblSensorReadings sensorReadings = new TblSensorReadings();
        sensorReadings.employeeId(employeeId).readingTime(zdt);
        //sensorReadingsRepository.save(sensorReadings);
        sensorReadingInsertRepo.insertWithQuery(sensorReadings);

        return "Sensor Data added successfully";
    }

    @GetMapping("/isRegistered/{id}")
    private ResponseEntity<String> isRegistered(@PathVariable("id") String rfidId) {
        try {
            HttpResponse httpResponse = isRegisteredCheck(rfidId);
            return new ResponseEntity<>(httpResponse.getResponseDesc(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public HttpResponse isRegisteredCheck(String rfid) {
        TblEmployeeAuthentication employeeAuthentication = employeeAuthenticationRepository.findByRfidId(rfid);
        if (Objects.nonNull(employeeAuthentication) && employeeAuthentication.getIsActive() == 1) {
            return new HttpResponse().setResponseDesc("yes");
        }
        Long employeeId = null;
        if (Objects.nonNull(employeeAuthentication) && employeeAuthentication.getIsActive() == 0) {
            employeeId = Long.valueOf(employeeAuthentication.getEmployeeId());
        }
        Long prevAttempts = unauthorizedActivityRepository.fetchAttemptByRfidID(rfid);
        if (prevAttempts == null) prevAttempts = 0L;

        LocalDateTime morningDateTime = LocalDateTime.now();
        ZonedDateTime zdt = morningDateTime.atZone(ZoneId.systemDefault());

        TblUnauthorizedActivity unauthorizedActivity = new TblUnauthorizedActivity();
        unauthorizedActivity
            .employeeId(Objects.nonNull(employeeId) ? employeeId.intValue() : null)
            .rfidId(rfid)
            .readingTime(zdt)
            .attempt(prevAttempts.intValue() + 1);
        //unauthorizedActivityRepository.save(unauthorizedActivity);
        sensorReadingInsertRepo.insertUnAuthorized(unauthorizedActivity);
        return new HttpResponse().setResponseDesc("no");
    }
}
