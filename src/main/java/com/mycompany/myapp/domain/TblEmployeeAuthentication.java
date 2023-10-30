package com.mycompany.myapp.domain;

import java.io.Serializable;
import javax.persistence.*;

/**
 * A TblEmployeeAuthentication.
 */
@Entity
@Table(name = "tbl_employee_authentication")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TblEmployeeAuthentication implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "employee_id")
    private Integer employeeId;

    @Column(name = "rfid_id")
    private String rfidId;

    @Lob
    @Column(name = "fingerprint")
    private String fingerprint;

    @Column(name = "is_active")
    private Integer isActive;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TblEmployeeAuthentication id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getEmployeeId() {
        return this.employeeId;
    }

    public TblEmployeeAuthentication employeeId(Integer employeeId) {
        this.setEmployeeId(employeeId);
        return this;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getRfidId() {
        return this.rfidId;
    }

    public TblEmployeeAuthentication rfidId(String rfidId) {
        this.setRfidId(rfidId);
        return this;
    }

    public void setRfidId(String rfidId) {
        this.rfidId = rfidId;
    }

    public String getFingerprint() {
        return this.fingerprint;
    }

    public TblEmployeeAuthentication fingerprint(String fingerprint) {
        this.setFingerprint(fingerprint);
        return this;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public Integer getIsActive() {
        return this.isActive;
    }

    public TblEmployeeAuthentication isActive(Integer isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TblEmployeeAuthentication)) {
            return false;
        }
        return id != null && id.equals(((TblEmployeeAuthentication) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TblEmployeeAuthentication{" +
            "id=" + getId() +
            ", employeeId=" + getEmployeeId() +
            ", rfidId='" + getRfidId() + "'" +
            ", fingerprint='" + getFingerprint() + "'" +
            ", isActive=" + getIsActive() +
            "}";
    }
}
