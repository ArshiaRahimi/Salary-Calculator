package com.mycompany.myapp.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;

/**
 * A TblSensorReadings.
 */
@Entity
@Table(name = "tbl_sensor_readings")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TblSensorReadings implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "employee_id")
    private Long employeeId;

    @Column(name = "reading_time")
    private ZonedDateTime readingTime;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TblSensorReadings id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEmployeeId() {
        return this.employeeId;
    }

    public TblSensorReadings employeeId(Long employeeId) {
        this.setEmployeeId(employeeId);
        return this;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public ZonedDateTime getReadingTime() {
        return this.readingTime;
    }

    public TblSensorReadings readingTime(ZonedDateTime readingTime) {
        this.setReadingTime(readingTime);
        return this;
    }

    public void setReadingTime(ZonedDateTime readingTime) {
        this.readingTime = readingTime;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TblSensorReadings)) {
            return false;
        }
        return id != null && id.equals(((TblSensorReadings) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TblSensorReadings{" +
            "id=" + getId() +
            ", employeeId=" + getEmployeeId() +
            ", readingTime='" + getReadingTime() + "'" +
            "}";
    }
}
