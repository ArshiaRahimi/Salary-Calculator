package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.TblEmployeeSalary} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.TblEmployeeSalaryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tbl-employee-salaries?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TblEmployeeSalaryCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private ZonedDateTimeFilter dateCalculated;

    private IntegerFilter employeeId;

    private StringFilter undertime;

    private StringFilter overtime;

    private StringFilter overtimeOffday;

    private StringFilter totalSalary;

    private Boolean distinct;

    public TblEmployeeSalaryCriteria() {}

    public TblEmployeeSalaryCriteria(TblEmployeeSalaryCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.dateCalculated = other.dateCalculated == null ? null : other.dateCalculated.copy();
        this.employeeId = other.employeeId == null ? null : other.employeeId.copy();
        this.undertime = other.undertime == null ? null : other.undertime.copy();
        this.overtime = other.overtime == null ? null : other.overtime.copy();
        this.overtimeOffday = other.overtimeOffday == null ? null : other.overtimeOffday.copy();
        this.totalSalary = other.totalSalary == null ? null : other.totalSalary.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TblEmployeeSalaryCriteria copy() {
        return new TblEmployeeSalaryCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public ZonedDateTimeFilter getDateCalculated() {
        return dateCalculated;
    }

    public ZonedDateTimeFilter dateCalculated() {
        if (dateCalculated == null) {
            dateCalculated = new ZonedDateTimeFilter();
        }
        return dateCalculated;
    }

    public void setDateCalculated(ZonedDateTimeFilter dateCalculated) {
        this.dateCalculated = dateCalculated;
    }

    public IntegerFilter getEmployeeId() {
        return employeeId;
    }

    public IntegerFilter employeeId() {
        if (employeeId == null) {
            employeeId = new IntegerFilter();
        }
        return employeeId;
    }

    public void setEmployeeId(IntegerFilter employeeId) {
        this.employeeId = employeeId;
    }

    public StringFilter getUndertime() {
        return undertime;
    }

    public StringFilter undertime() {
        if (undertime == null) {
            undertime = new StringFilter();
        }
        return undertime;
    }

    public void setUndertime(StringFilter undertime) {
        this.undertime = undertime;
    }

    public StringFilter getOvertime() {
        return overtime;
    }

    public StringFilter overtime() {
        if (overtime == null) {
            overtime = new StringFilter();
        }
        return overtime;
    }

    public void setOvertime(StringFilter overtime) {
        this.overtime = overtime;
    }

    public StringFilter getOvertimeOffday() {
        return overtimeOffday;
    }

    public StringFilter overtimeOffday() {
        if (overtimeOffday == null) {
            overtimeOffday = new StringFilter();
        }
        return overtimeOffday;
    }

    public void setOvertimeOffday(StringFilter overtimeOffday) {
        this.overtimeOffday = overtimeOffday;
    }

    public StringFilter getTotalSalary() {
        return totalSalary;
    }

    public StringFilter totalSalary() {
        if (totalSalary == null) {
            totalSalary = new StringFilter();
        }
        return totalSalary;
    }

    public void setTotalSalary(StringFilter totalSalary) {
        this.totalSalary = totalSalary;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TblEmployeeSalaryCriteria that = (TblEmployeeSalaryCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(dateCalculated, that.dateCalculated) &&
            Objects.equals(employeeId, that.employeeId) &&
            Objects.equals(undertime, that.undertime) &&
            Objects.equals(overtime, that.overtime) &&
            Objects.equals(overtimeOffday, that.overtimeOffday) &&
            Objects.equals(totalSalary, that.totalSalary) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateCalculated, employeeId, undertime, overtime, overtimeOffday, totalSalary, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TblEmployeeSalaryCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (dateCalculated != null ? "dateCalculated=" + dateCalculated + ", " : "") +
            (employeeId != null ? "employeeId=" + employeeId + ", " : "") +
            (undertime != null ? "undertime=" + undertime + ", " : "") +
            (overtime != null ? "overtime=" + overtime + ", " : "") +
            (overtimeOffday != null ? "overtimeOffday=" + overtimeOffday + ", " : "") +
            (totalSalary != null ? "totalSalary=" + totalSalary + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
