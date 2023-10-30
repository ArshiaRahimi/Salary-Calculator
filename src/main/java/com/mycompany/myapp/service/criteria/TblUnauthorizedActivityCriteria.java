package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.TblUnauthorizedActivity} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.TblUnauthorizedActivityResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tbl-unauthorized-activities?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TblUnauthorizedActivityCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter rfidId;

    private IntegerFilter employeeId;

    private ZonedDateTimeFilter readingTime;

    private IntegerFilter attempt;

    private Boolean distinct;

    public TblUnauthorizedActivityCriteria() {}

    public TblUnauthorizedActivityCriteria(TblUnauthorizedActivityCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.rfidId = other.rfidId == null ? null : other.rfidId.copy();
        this.employeeId = other.employeeId == null ? null : other.employeeId.copy();
        this.readingTime = other.readingTime == null ? null : other.readingTime.copy();
        this.attempt = other.attempt == null ? null : other.attempt.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TblUnauthorizedActivityCriteria copy() {
        return new TblUnauthorizedActivityCriteria(this);
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

    public ZonedDateTimeFilter getReadingTime() {
        return readingTime;
    }

    public ZonedDateTimeFilter readingTime() {
        if (readingTime == null) {
            readingTime = new ZonedDateTimeFilter();
        }
        return readingTime;
    }

    public void setReadingTime(ZonedDateTimeFilter readingTime) {
        this.readingTime = readingTime;
    }

    public IntegerFilter getAttempt() {
        return attempt;
    }

    public IntegerFilter attempt() {
        if (attempt == null) {
            attempt = new IntegerFilter();
        }
        return attempt;
    }

    public void setAttempt(IntegerFilter attempt) {
        this.attempt = attempt;
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
        final TblUnauthorizedActivityCriteria that = (TblUnauthorizedActivityCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(rfidId, that.rfidId) &&
            Objects.equals(employeeId, that.employeeId) &&
            Objects.equals(readingTime, that.readingTime) &&
            Objects.equals(attempt, that.attempt) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, rfidId, employeeId, readingTime, attempt, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TblUnauthorizedActivityCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (rfidId != null ? "rfidId=" + rfidId + ", " : "") +
            (employeeId != null ? "employeeId=" + employeeId + ", " : "") +
            (readingTime != null ? "readingTime=" + readingTime + ", " : "") +
            (attempt != null ? "attempt=" + attempt + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
