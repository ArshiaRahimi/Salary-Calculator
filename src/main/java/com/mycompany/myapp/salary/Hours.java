package com.mycompany.myapp.salary;

public class Hours {

    private double overtime = 0;
    private double undertime = 0;
    private double offtime = 0;

    public double getOvertime() {
        return overtime;
    }

    public Hours setOvertime(double overtime) {
        this.overtime = overtime;
        return this;
    }

    public double getUndertime() {
        return undertime;
    }

    public Hours setUndertime(double undertime) {
        this.undertime = undertime;
        return this;
    }

    public double getOfftime() {
        return offtime;
    }

    public Hours setOfftime(double offtime) {
        this.offtime = offtime;
        return this;
    }
}
