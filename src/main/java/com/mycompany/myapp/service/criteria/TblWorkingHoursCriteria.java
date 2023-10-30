package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.TblWorkingHours} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.TblWorkingHoursResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tbl-working-hours?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TblWorkingHoursCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter day;

    private IntegerFilter startTimeHour;

    private IntegerFilter startTimeMinute;

    private IntegerFilter endTimeHour;

    private IntegerFilter endTimeMinute;

    private Boolean distinct;

    public TblWorkingHoursCriteria() {}

    public TblWorkingHoursCriteria(TblWorkingHoursCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.day = other.day == null ? null : other.day.copy();
        this.startTimeHour = other.startTimeHour == null ? null : other.startTimeHour.copy();
        this.startTimeMinute = other.startTimeMinute == null ? null : other.startTimeMinute.copy();
        this.endTimeHour = other.endTimeHour == null ? null : other.endTimeHour.copy();
        this.endTimeMinute = other.endTimeMinute == null ? null : other.endTimeMinute.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TblWorkingHoursCriteria copy() {
        return new TblWorkingHoursCriteria(this);
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

    public StringFilter getDay() {
        return day;
    }

    public StringFilter day() {
        if (day == null) {
            day = new StringFilter();
        }
        return day;
    }

    public void setDay(StringFilter day) {
        this.day = day;
    }

    public IntegerFilter getStartTimeHour() {
        return startTimeHour;
    }

    public IntegerFilter startTimeHour() {
        if (startTimeHour == null) {
            startTimeHour = new IntegerFilter();
        }
        return startTimeHour;
    }

    public void setStartTimeHour(IntegerFilter startTimeHour) {
        this.startTimeHour = startTimeHour;
    }

    public IntegerFilter getStartTimeMinute() {
        return startTimeMinute;
    }

    public IntegerFilter startTimeMinute() {
        if (startTimeMinute == null) {
            startTimeMinute = new IntegerFilter();
        }
        return startTimeMinute;
    }

    public void setStartTimeMinute(IntegerFilter startTimeMinute) {
        this.startTimeMinute = startTimeMinute;
    }

    public IntegerFilter getEndTimeHour() {
        return endTimeHour;
    }

    public IntegerFilter endTimeHour() {
        if (endTimeHour == null) {
            endTimeHour = new IntegerFilter();
        }
        return endTimeHour;
    }

    public void setEndTimeHour(IntegerFilter endTimeHour) {
        this.endTimeHour = endTimeHour;
    }

    public IntegerFilter getEndTimeMinute() {
        return endTimeMinute;
    }

    public IntegerFilter endTimeMinute() {
        if (endTimeMinute == null) {
            endTimeMinute = new IntegerFilter();
        }
        return endTimeMinute;
    }

    public void setEndTimeMinute(IntegerFilter endTimeMinute) {
        this.endTimeMinute = endTimeMinute;
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
        final TblWorkingHoursCriteria that = (TblWorkingHoursCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(day, that.day) &&
            Objects.equals(startTimeHour, that.startTimeHour) &&
            Objects.equals(startTimeMinute, that.startTimeMinute) &&
            Objects.equals(endTimeHour, that.endTimeHour) &&
            Objects.equals(endTimeMinute, that.endTimeMinute) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, day, startTimeHour, startTimeMinute, endTimeHour, endTimeMinute, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TblWorkingHoursCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (day != null ? "day=" + day + ", " : "") +
            (startTimeHour != null ? "startTimeHour=" + startTimeHour + ", " : "") +
            (startTimeMinute != null ? "startTimeMinute=" + startTimeMinute + ", " : "") +
            (endTimeHour != null ? "endTimeHour=" + endTimeHour + ", " : "") +
            (endTimeMinute != null ? "endTimeMinute=" + endTimeMinute + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
