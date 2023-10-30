package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.TblEmployeeAuthentication} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.TblEmployeeAuthenticationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tbl-employee-authentications?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TblEmployeeAuthenticationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter employeeId;

    private StringFilter rfidId;

    private IntegerFilter isActive;

    private Boolean distinct;

    public TblEmployeeAuthenticationCriteria() {}

    public TblEmployeeAuthenticationCriteria(TblEmployeeAuthenticationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.employeeId = other.employeeId == null ? null : other.employeeId.copy();
        this.rfidId = other.rfidId == null ? null : other.rfidId.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TblEmployeeAuthenticationCriteria copy() {
        return new TblEmployeeAuthenticationCriteria(this);
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

    public StringFilter getRfidId() {
        return rfidId;
    }

    public StringFilter rfidId() {
        if (rfidId == null) {
            rfidId = new StringFilter();
        }
        return rfidId;
    }

    public void setRfidId(StringFilter rfidId) {
        this.rfidId = rfidId;
    }

    public IntegerFilter getIsActive() {
        return isActive;
    }

    public IntegerFilter isActive() {
        if (isActive == null) {
            isActive = new IntegerFilter();
        }
        return isActive;
    }

    public void setIsActive(IntegerFilter isActive) {
        this.isActive = isActive;
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
        final TblEmployeeAuthenticationCriteria that = (TblEmployeeAuthenticationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(employeeId, that.employeeId) &&
            Objects.equals(rfidId, that.rfidId) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, employeeId, rfidId, isActive, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TblEmployeeAuthenticationCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (employeeId != null ? "employeeId=" + employeeId + ", " : "") +
            (rfidId != null ? "rfidId=" + rfidId + ", " : "") +
            (isActive != null ? "isActive=" + isActive + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
