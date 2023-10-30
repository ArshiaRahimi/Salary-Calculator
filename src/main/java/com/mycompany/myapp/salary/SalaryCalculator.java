package com.mycompany.myapp.salary;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.repository.*;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;
import javafx.util.Pair;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.stereotype.Component;

@Component
public class SalaryCalculator {

    @Autowired
    TblSensorReadingsRepository sensorReadingsRepository;

    @Autowired
    TblEmployeeInformationRepository employeeInformationRepository;

    @Autowired
    TblOffDayRepository offDayRepository;

    @Autowired
    TblWorkingHoursRepository workingHoursRepository;

    @Autowired
    TblEmployeeSalaryRepository employeeSalaryRepository;

    @Autowired
    TblSalaryInfoRepository salaryInfoRepository;

    @Autowired
    SensorReadingInsertRepo sensorReadingInsertRepo;

    //@Value("${salary.calculation.interval.in.days}")
    private final int salaryCalculationIntervalInDays = 30;
    //@Value("${under.time.coefficient}")
    private final double underTimeCoefficient = 2;
    //@Value("${over.time.coefficient}")
    private final double overTimeCoefficient = 1.4;
    //@Value("${off.day.coefficient}")
    private final double offDayCoefficient = 1.8;

    String startDate = "2023-09-23";
    String endDate = "2023-10-23";

    private Set<LocalDate> offDays = new HashSet<>();
    private Map<LocalDate, StartTimeEndTime> dateToStartTimeEndTimeMap = new HashMap<>();

    @PostConstruct
    public void fillCache() {
        LocalDate startDateLocalDate = LocalDate.parse(startDate);
        LocalDate endDateLocalDate = LocalDate.parse(endDate);
        offDayRepository
            .findAll()
            .forEach(date -> {
                try {
                    LocalDate localDate = LocalDate.of(date.getYear(), date.getMonth(), date.getDay());
                    offDays.add(localDate);
                } catch (Exception ignored) {}
            });

        for (LocalDate date = startDateLocalDate; date.isBefore(endDateLocalDate); date = date.plusDays(1)) {
            if (!offDays.contains(date)) {
                DayOfWeek day = date.getDayOfWeek();
                TblWorkingHours workingHours = workingHoursRepository.findTblWorkingHoursByDayName(day.name());
                LocalTime startTime = LocalTime.of(workingHours.getStartTimeHour(), workingHours.getStartTimeMinute());
                LocalTime endTime = LocalTime.of(workingHours.getEndTimeHour(), workingHours.getEndTimeMinute());
                StartTimeEndTime startTimeEndTime = new StartTimeEndTime();
                startTimeEndTime.setEndTime(endTime).setStartTime(startTime);

                dateToStartTimeEndTimeMap.putIfAbsent(date, startTimeEndTime);
            }
        }
    }

    //@PostConstruct
    // HAPPY SCENARIO
    public void initializeSensorData() {
        //todo implement start time and end time of process
        List<TblSensorReadings> sensorReadingsList = new ArrayList<>();
        LocalDate date = LocalDate.of(2023, 9, 23);
        LocalTime morning = LocalTime.of(9, 30);
        LocalTime afternoon = LocalTime.of(17, 30);

        for (int i = 0; i < 30; i++) {
            LocalDateTime morningDateTime = LocalDateTime.of(date, morning);
            ZonedDateTime zdt = morningDateTime.atZone(ZoneId.systemDefault());
            Instant instant = zdt.toInstant();
            TblSensorReadings sensorReadings = new TblSensorReadings();
            sensorReadings
                .employeeId(1L)
                //.readingTime(Date.from(instant))
                .readingTime(zdt);
            sensorReadingInsertRepo.insertWithQuery(sensorReadings);

            sensorReadingsList.add(sensorReadings);
            LocalDateTime afterNoonDateTime = LocalDateTime.of(date, afternoon);
            ZonedDateTime zdt2 = afterNoonDateTime.atZone(ZoneId.systemDefault());
            Instant instant2 = zdt2.toInstant();
            TblSensorReadings sensorReadings2 = new TblSensorReadings();
            sensorReadings2
                .employeeId(1L)
                //.readingTime(Date.from(instant2))
                .readingTime(zdt2);
            sensorReadingsList.add(sensorReadings2);
            sensorReadingInsertRepo.insertWithQuery(sensorReadings2);
            date = date.plusDays(1);
        }
        //sensorReadingsRepository.saveAll(sensorReadingsList);
    }

    //@Scheduled(cron = "0 0 0 25 * ?")
    @org.springframework.context.event.EventListener(ApplicationReadyEvent.class)
    public void salaryCalculator() {
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate startDateLocalDate = LocalDate.parse(startDate);
        LocalDate endDateLocalDate = LocalDate.parse(endDate);
        List<TblSensorReadings> sensorReadingsList = sensorReadingsRepository.findReadingsBetweenDates(startDate, endDate);

        Map<Long, List<TblSensorReadings>> employeeIdToLogsMap = sensorReadingsList
            .stream()
            .collect(Collectors.groupingBy(TblSensorReadings::getEmployeeId));

        for (Map.Entry<Long, List<TblSensorReadings>> employeeLogData : employeeIdToLogsMap.entrySet()) {
            employeeLogData.getValue().sort(Comparator.comparing(TblSensorReadings::getReadingTime));

            List<Long> epochList = new ArrayList<>();
            Map<LocalDate, List<Long>> dateToLogsMap = new HashMap<>();
            int workingDays = 0; // initialize the counter
            double overTimeHours = 0;
            double offDayHours = 0;
            double totalWorkingHours;
            double underTimeHours = 0;
            for (LocalDate date = startDateLocalDate; date.isBefore(endDateLocalDate); date = date.plusDays(1)) {
                if (!offDays.contains(date)) {
                    workingDays++;
                    DayOfWeek day = date.getDayOfWeek();
                    TblWorkingHours workingHours = workingHoursRepository.findTblWorkingHoursByDayName(day.name());
                    LocalTime startTime = LocalTime.of(workingHours.getStartTimeHour(), workingHours.getStartTimeMinute());
                    LocalTime endTime = LocalTime.of(workingHours.getEndTimeHour(), workingHours.getEndTimeMinute());
                    Duration duration = Duration.between(startTime, endTime);
                    double totalValidHours = duration.toMinutes() / 60.0;
                    underTimeHours += totalValidHours;
                }
            }

            totalWorkingHours = underTimeHours;

            for (TblSensorReadings sensorReadings : employeeLogData.getValue()) {
                Instant instant = sensorReadings.getReadingTime().toInstant();
                LocalDate localDate = instant.atZone(zoneId).toLocalDate(); // Convert to LocalDate
                dateToLogsMap.putIfAbsent(localDate, new ArrayList<>());
                dateToLogsMap.get(localDate).add(sensorReadings.getReadingTime().toInstant().toEpochMilli());
                epochList.add(sensorReadings.getReadingTime().toInstant().toEpochMilli());
            }
            List<Pair<Long, Long>> pairList = new ArrayList<Pair<Long, Long>>();
            for (int i = 0; i < epochList.size() - 1; i += 2) {
                Pair<Long, Long> pair = new Pair<Long, Long>(epochList.get(i), epochList.get(i + 1));
                pairList.add(pair);
            }

            for (Pair<Long, Long> pair : pairList) {
                Hours hours = calculateHoursBasedOnPair(pair, zoneId);
                underTimeHours = underTimeHours - hours.getUndertime();
                offDayHours = offDayHours + hours.getOfftime();
                overTimeHours = overTimeHours + hours.getOvertime();
            }

            TblEmployeeInformation employee = employeeInformationRepository.findById(employeeLogData.getKey()).get();
            TblSalaryInfo salaryInfo = salaryInfoRepository.findTblSalaryInfoByEmployeeID(employeeLogData.getKey().intValue());
            double hourlySalary = (double) salaryInfo.getBaseSalary() / totalWorkingHours;
            double housingRights = Objects.isNull(salaryInfo.getHousingRights()) ? 0.0 : salaryInfo.getHousingRights();
            double internetRights = Objects.isNull(salaryInfo.getInternetRights()) ? 0.0 : salaryInfo.getInternetRights();
            double groceriesRights = Objects.isNull(salaryInfo.getGroceriesRights()) ? 0.0 : salaryInfo.getGroceriesRights();
            double salary = (double) salaryInfo.getBaseSalary() +
            (overTimeCoefficient * overTimeHours * hourlySalary) +
            (offDayCoefficient * offDayHours * hourlySalary) -
            (underTimeCoefficient * underTimeHours * hourlySalary) +
            housingRights +
            internetRights +
            groceriesRights;

            Date date = new Date();
            Instant instant = date.toInstant();
            TblEmployeeSalary employeeSalary = new TblEmployeeSalary();
            employeeSalary
                .overtime(String.valueOf(overTimeHours))
                .undertime(String.valueOf(underTimeHours))
                .overtimeOffday(String.valueOf(offDayHours))
                .totalSalary(String.valueOf(Math.max(salary, 0)))
                .dateCalculated(ZonedDateTime.ofInstant(instant, zoneId))
                .employeeId(employee.getId().intValue());
            employeeSalaryRepository.save(employeeSalary);
        }
    }

    /*public static void main(String[] args) {
        double baseSalary = 12000000;
        double hourlySalary = baseSalary / 160;
        double salary = baseSalary + (1.4 * hourlySalary * 16.5) + (1.8 * hourlySalary * 16.5) - (2 * hourlySalary * 23);
        System.out.println(salary);
    }*/

    public Hours calculateHoursBasedOnPair(Pair<Long, Long> pair, ZoneId zoneId) {
        Instant entryPair = Instant.ofEpochMilli(pair.getKey());
        Hours hours = new Hours();
        LocalDateTime entryLocalDateTime = entryPair.atZone(zoneId).toLocalDateTime();
        Instant leavePair = Instant.ofEpochMilli(pair.getValue());
        LocalDateTime leaveEntryLocalDateTime = leavePair.atZone(zoneId).toLocalDateTime();
        boolean isOffDayStart = offDays.contains(entryLocalDateTime.toLocalDate());
        boolean isOffDayEnd = offDays.contains(leaveEntryLocalDateTime.toLocalDate());
        //todo 9-10/
        if (
            !isOffDayStart &&
            !isOffDayEnd &&
            (
                entryLocalDateTime.toLocalTime().isAfter(dateToStartTimeEndTimeMap.get(entryLocalDateTime.toLocalDate()).getStartTime()) ||
                entryLocalDateTime.toLocalTime().equals(dateToStartTimeEndTimeMap.get(entryLocalDateTime.toLocalDate()).getStartTime())
            ) &&
            (
                leaveEntryLocalDateTime
                    .toLocalTime()
                    .isBefore(dateToStartTimeEndTimeMap.get(leaveEntryLocalDateTime.toLocalDate()).getEndTime()) ||
                leaveEntryLocalDateTime.toLocalTime().equals(dateToStartTimeEndTimeMap.get(entryLocalDateTime.toLocalDate()).getEndTime())
            ) &&
            entryLocalDateTime.toLocalDate().equals(leaveEntryLocalDateTime.toLocalDate())
        ) {
            // while working hours
            Duration duration = Duration.between(entryLocalDateTime.toLocalTime(), leaveEntryLocalDateTime.toLocalTime());
            double totalValidHours = duration.toMinutes() / 60.0;
            hours.setUndertime(totalValidHours);
        } else if (
            !isOffDayStart &&
            !isOffDayEnd &&
            (
                entryLocalDateTime.toLocalTime().isAfter(dateToStartTimeEndTimeMap.get(entryLocalDateTime.toLocalDate()).getStartTime()) ||
                entryLocalDateTime.toLocalTime().equals(dateToStartTimeEndTimeMap.get(entryLocalDateTime.toLocalDate()).getStartTime())
            ) &&
            (
                entryLocalDateTime.toLocalTime().isBefore(dateToStartTimeEndTimeMap.get(entryLocalDateTime.toLocalDate()).getEndTime()) &&
                (
                    leaveEntryLocalDateTime
                        .toLocalTime()
                        .isAfter(dateToStartTimeEndTimeMap.get(leaveEntryLocalDateTime.toLocalDate()).getEndTime())
                )
            )
        ) {
            // while working hours
            Duration duration = Duration.between(
                entryLocalDateTime.toLocalTime(),
                dateToStartTimeEndTimeMap.get(leaveEntryLocalDateTime.toLocalDate()).getEndTime()
            );
            Duration duration1 = Duration.between(
                dateToStartTimeEndTimeMap.get(leaveEntryLocalDateTime.toLocalDate()).getEndTime(),
                leaveEntryLocalDateTime.toLocalTime()
            );
            double totalValidHours = duration.toMinutes() / 60.0;
            double totalOverTimeHours = duration1.toMinutes() / 60.0;
            hours.setUndertime(totalValidHours);
            hours.setOvertime(totalOverTimeHours);
        } else if (
            !isOffDayStart &&
            !isOffDayEnd &&
            (
                entryLocalDateTime.toLocalTime().isAfter(dateToStartTimeEndTimeMap.get(entryLocalDateTime.toLocalDate()).getEndTime()) ||
                entryLocalDateTime.toLocalTime().equals(dateToStartTimeEndTimeMap.get(entryLocalDateTime.toLocalDate()).getEndTime())
            ) &&
            (
                leaveEntryLocalDateTime
                    .toLocalTime()
                    .isAfter(dateToStartTimeEndTimeMap.get(leaveEntryLocalDateTime.toLocalDate()).getEndTime())
            )
        ) {
            // while working hours
            Duration duration = Duration.between(entryLocalDateTime.toLocalTime(), leaveEntryLocalDateTime.toLocalTime());
            double totalValidHours = duration.toMinutes() / 60.0;
            hours.setOvertime(totalValidHours);
        } else if (isOffDayStart && isOffDayEnd) {
            //he worked on off day
            Duration duration = Duration.between(entryLocalDateTime.toLocalTime(), leaveEntryLocalDateTime.toLocalTime());
            double totalValidHours = duration.toMinutes() / 60.0;
            hours.setOfftime(totalValidHours);
        } else if (
            !isOffDayStart &&
            !isOffDayEnd &&
            entryLocalDateTime.toLocalTime().isBefore(dateToStartTimeEndTimeMap.get(entryLocalDateTime.toLocalDate()).getStartTime()) &&
            (
                leaveEntryLocalDateTime
                    .toLocalTime()
                    .isBefore(dateToStartTimeEndTimeMap.get(leaveEntryLocalDateTime.toLocalDate()).getEndTime()) ||
                leaveEntryLocalDateTime.toLocalTime().equals(dateToStartTimeEndTimeMap.get(entryLocalDateTime.toLocalDate()).getEndTime())
            )
        ) {
            Duration duration = Duration.between(
                entryLocalDateTime.toLocalTime(),
                dateToStartTimeEndTimeMap.get(leaveEntryLocalDateTime.toLocalDate()).getStartTime()
            );
            Duration duration1 = Duration.between(
                dateToStartTimeEndTimeMap.get(leaveEntryLocalDateTime.toLocalDate()).getStartTime(),
                leaveEntryLocalDateTime.toLocalTime()
            );
            double totalOverTime = duration.toMinutes() / 60.0;
            double totalWorkTime = duration1.toMinutes() / 60.0;
            hours.setUndertime(totalWorkTime);
            hours.setOvertime(totalOverTime);
        } else if (
            !isOffDayStart &&
            !isOffDayEnd &&
            entryLocalDateTime.toLocalTime().isBefore(dateToStartTimeEndTimeMap.get(entryLocalDateTime.toLocalDate()).getStartTime()) &&
            (
                leaveEntryLocalDateTime
                    .toLocalTime()
                    .isAfter(dateToStartTimeEndTimeMap.get(leaveEntryLocalDateTime.toLocalDate()).getEndTime())
            )
        ) {
            Duration legalWorkingDuration = Duration.between(
                dateToStartTimeEndTimeMap.get(entryLocalDateTime.toLocalDate()).getStartTime(),
                dateToStartTimeEndTimeMap.get(leaveEntryLocalDateTime.toLocalDate()).getEndTime()
            );
            Duration extraDurationBefore = Duration.between(
                entryLocalDateTime.toLocalTime(),
                dateToStartTimeEndTimeMap.get(entryLocalDateTime.toLocalDate()).getStartTime()
            );
            Duration extraDurationAfter = Duration.between(
                dateToStartTimeEndTimeMap.get(leaveEntryLocalDateTime.toLocalDate()).getEndTime(),
                leaveEntryLocalDateTime.toLocalTime()
            );
            double legalWorkingTime = legalWorkingDuration.toMinutes() / 60.0;
            double extraTimeBefore = extraDurationBefore.toMinutes() / 60.0;
            double extraTimeAfter = extraDurationAfter.toMinutes() / 60.0;
            hours.setUndertime(legalWorkingTime);
            hours.setOvertime(extraTimeBefore + extraTimeAfter);
        } else if (
            !isOffDayStart &&
            !isOffDayEnd &&
            (
                entryLocalDateTime.toLocalTime().isAfter(dateToStartTimeEndTimeMap.get(entryLocalDateTime.toLocalDate()).getStartTime()) ||
                entryLocalDateTime.toLocalTime().equals(dateToStartTimeEndTimeMap.get(entryLocalDateTime.toLocalDate()).getStartTime())
            ) &&
            (
                leaveEntryLocalDateTime
                    .toLocalTime()
                    .isBefore(dateToStartTimeEndTimeMap.get(leaveEntryLocalDateTime.toLocalDate()).getEndTime()) ||
                leaveEntryLocalDateTime.toLocalTime().equals(dateToStartTimeEndTimeMap.get(entryLocalDateTime.toLocalDate()).getEndTime())
            ) &&
            !entryLocalDateTime.toLocalDate().equals(leaveEntryLocalDateTime.toLocalDate())
        ) {
            Duration extraHours = Duration.between(entryLocalDateTime.toLocalTime(), leaveEntryLocalDateTime.toLocalTime());
            double legalWorkingTime = extraHours.toMinutes() / 60.0;
            hours.setOvertime(24.0 + legalWorkingTime);
        } else if (
            !isOffDayStart &&
            isOffDayEnd &&
            entryLocalDateTime.toLocalTime().isAfter(dateToStartTimeEndTimeMap.get(entryLocalDateTime.toLocalDate()).getEndTime())
        ) {
            Duration extraHours = Duration.between(entryLocalDateTime.toLocalTime(), LocalTime.of(0, 0));
            Duration offDay = Duration.between(LocalTime.of(0, 0), leaveEntryLocalDateTime.toLocalTime());
            double legalWorkingTime = extraHours.toMinutes() / 60.0;
            double offDayTime = offDay.toMinutes() / 60.0;
            hours.setOvertime(24 + legalWorkingTime);
            hours.setOfftime(offDayTime);
        }
        return hours;
    }
}
