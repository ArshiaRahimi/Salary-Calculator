package com.mycompany.myapp.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;

/**
 * A TblUnauthorizedActivity.
 */
@Entity
@Table(name = "tbl_unauthorized_activity")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TblUnauthorizedActivity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "rfid_id")
    private String rfidId;

    @Column(name = "employee_id")
    private Integer employeeId;

    @Column(name = "reading_time")
    private ZonedDateTime readingTime;

    @Column(name = "attempt")
    private Integer attempt;

    @Lob
    @Column(name = "fingerprint")
    private String fingerprint;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TblUnauthorizedActivity id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRfidId() {
        return this.rfidId;
    }

    public TblUnauthorizedActivity rfidId(String rfidId) {
        this.setRfidId(rfidId);
        return this;
    }

    public void setRfidId(String rfidId) {
        this.rfidId = rfidId;
    }

    public Integer getEmployeeId() {
        return this.employeeId;
    }

    public TblUnauthorizedActivity employeeId(Integer employeeId) {
        this.setEmployeeId(employeeId);
        return this;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public ZonedDateTime getReadingTime() {
        return this.readingTime;
    }

    public TblUnauthorizedActivity readingTime(ZonedDateTime readingTime) {
        this.setReadingTime(readingTime);
        return this;
    }

    public void setReadingTime(ZonedDateTime readingTime) {
        this.readingTime = readingTime;
    }

    public Integer getAttempt() {
        return this.attempt;
    }

    public TblUnauthorizedActivity attempt(Integer attempt) {
        this.setAttempt(attempt);
        return this;
    }

    public void setAttempt(Integer attempt) {
        this.attempt = attempt;
    }

    public String getFingerprint() {
        return this.fingerprint;
    }

    public TblUnauthorizedActivity fingerprint(String fingerprint) {
        this.setFingerprint(fingerprint);
        return this;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TblUnauthorizedActivity)) {
            return false;
        }
        return id != null && id.equals(((TblUnauthorizedActivity) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TblUnauthorizedActivity{" +
            "id=" + getId() +
            ", rfidId='" + getRfidId() + "'" +
            ", employeeId=" + getEmployeeId() +
            ", readingTime='" + getReadingTime() + "'" +
            ", attempt=" + getAttempt() +
            ", fingerprint='" + getFingerprint() + "'" +
            "}";
    }
}
