package com.mycompany.myapp.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;

/**
 * A TblEmployeeSalary.
 */
@Entity
@Table(name = "tbl_employee_salary")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TblEmployeeSalary implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "date_calculated")
    private ZonedDateTime dateCalculated;

    @Column(name = "employee_id")
    private Integer employeeId;

    @Column(name = "undertime")
    private String undertime;

    @Column(name = "overtime")
    private String overtime;

    @Column(name = "overtime_offday")
    private String overtimeOffday;

    @Column(name = "total_salary")
    private String totalSalary;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TblEmployeeSalary id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDateCalculated() {
        return this.dateCalculated;
    }

    public TblEmployeeSalary dateCalculated(ZonedDateTime dateCalculated) {
        this.setDateCalculated(dateCalculated);
        return this;
    }

    public void setDateCalculated(ZonedDateTime dateCalculated) {
        this.dateCalculated = dateCalculated;
    }

    public Integer getEmployeeId() {
        return this.employeeId;
    }

    public TblEmployeeSalary employeeId(Integer employeeId) {
        this.setEmployeeId(employeeId);
        return this;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getUndertime() {
        return this.undertime;
    }

    public TblEmployeeSalary undertime(String undertime) {
        this.setUndertime(undertime);
        return this;
    }

    public void setUndertime(String undertime) {
        this.undertime = undertime;
    }

    public String getOvertime() {
        return this.overtime;
    }

    public TblEmployeeSalary overtime(String overtime) {
        this.setOvertime(overtime);
        return this;
    }

    public void setOvertime(String overtime) {
        this.overtime = overtime;
    }

    public String getOvertimeOffday() {
        return this.overtimeOffday;
    }

    public TblEmployeeSalary overtimeOffday(String overtimeOffday) {
        this.setOvertimeOffday(overtimeOffday);
        return this;
    }

    public void setOvertimeOffday(String overtimeOffday) {
        this.overtimeOffday = overtimeOffday;
    }

    public String getTotalSalary() {
        return this.totalSalary;
    }

    public TblEmployeeSalary totalSalary(String totalSalary) {
        this.setTotalSalary(totalSalary);
        return this;
    }

    public void setTotalSalary(String totalSalary) {
        this.totalSalary = totalSalary;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TblEmployeeSalary)) {
            return false;
        }
        return id != null && id.equals(((TblEmployeeSalary) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TblEmployeeSalary{" +
            "id=" + getId() +
            ", dateCalculated='" + getDateCalculated() + "'" +
            ", employeeId=" + getEmployeeId() +
            ", undertime='" + getUndertime() + "'" +
            ", overtime='" + getOvertime() + "'" +
            ", overtimeOffday='" + getOvertimeOffday() + "'" +
            ", totalSalary='" + getTotalSalary() + "'" +
            "}";
    }
}
