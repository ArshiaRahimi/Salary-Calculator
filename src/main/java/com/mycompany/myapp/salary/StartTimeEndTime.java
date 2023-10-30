package com.mycompany.myapp.salary;

import java.time.LocalTime;

public class StartTimeEndTime {

    private LocalTime startTime;
    private LocalTime endTime;

    public LocalTime getStartTime() {
        return startTime;
    }

    public StartTimeEndTime setStartTime(LocalTime startTime) {
        this.startTime = startTime;
        return this;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public StartTimeEndTime setEndTime(LocalTime endTime) {
        this.endTime = endTime;
        return this;
    }
}
