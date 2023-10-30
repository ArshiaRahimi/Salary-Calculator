package com.mycompany.myapp.domain;

import java.io.Serializable;
import javax.persistence.*;

/**
 * A TblWorkingHours.
 */
@Entity
@Table(name = "tbl_working_hours")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TblWorkingHours implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "day")
    private String day;

    @Column(name = "start_time_hour")
    private Integer startTimeHour;

    @Column(name = "start_time_minute")
    private Integer startTimeMinute;

    @Column(name = "end_time_hour")
    private Integer endTimeHour;

    @Column(name = "end_time_minute")
    private Integer endTimeMinute;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TblWorkingHours id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDay() {
        return this.day;
    }

    public TblWorkingHours day(String day) {
        this.setDay(day);
        return this;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Integer getStartTimeHour() {
        return this.startTimeHour;
    }

    public TblWorkingHours startTimeHour(Integer startTimeHour) {
        this.setStartTimeHour(startTimeHour);
        return this;
    }

    public void setStartTimeHour(Integer startTimeHour) {
        this.startTimeHour = startTimeHour;
    }

    public Integer getStartTimeMinute() {
        return this.startTimeMinute;
    }

    public TblWorkingHours startTimeMinute(Integer startTimeMinute) {
        this.setStartTimeMinute(startTimeMinute);
        return this;
    }

    public void setStartTimeMinute(Integer startTimeMinute) {
        this.startTimeMinute = startTimeMinute;
    }

    public Integer getEndTimeHour() {
        return this.endTimeHour;
    }

    public TblWorkingHours endTimeHour(Integer endTimeHour) {
        this.setEndTimeHour(endTimeHour);
        return this;
    }

    public void setEndTimeHour(Integer endTimeHour) {
        this.endTimeHour = endTimeHour;
    }

    public Integer getEndTimeMinute() {
        return this.endTimeMinute;
    }

    public TblWorkingHours endTimeMinute(Integer endTimeMinute) {
        this.setEndTimeMinute(endTimeMinute);
        return this;
    }

    public void setEndTimeMinute(Integer endTimeMinute) {
        this.endTimeMinute = endTimeMinute;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TblWorkingHours)) {
            return false;
        }
        return id != null && id.equals(((TblWorkingHours) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TblWorkingHours{" +
            "id=" + getId() +
            ", day='" + getDay() + "'" +
            ", startTimeHour=" + getStartTimeHour() +
            ", startTimeMinute=" + getStartTimeMinute() +
            ", endTimeHour=" + getEndTimeHour() +
            ", endTimeMinute=" + getEndTimeMinute() +
            "}";
    }
}
