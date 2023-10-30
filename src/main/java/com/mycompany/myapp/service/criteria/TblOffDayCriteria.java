package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.TblOffDay} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.TblOffDayResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tbl-off-days?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TblOffDayCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter day;

    private IntegerFilter month;

    private IntegerFilter year;

    private Boolean distinct;

    public TblOffDayCriteria() {}

    public TblOffDayCriteria(TblOffDayCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.day = other.day == null ? null : other.day.copy();
        this.month = other.month == null ? null : other.month.copy();
        this.year = other.year == null ? null : other.year.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TblOffDayCriteria copy() {
        return new TblOffDayCriteria(this);
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

    public IntegerFilter getDay() {
        return day;
    }

    public IntegerFilter day() {
        if (day == null) {
            day = new IntegerFilter();
        }
        return day;
    }

    public void setDay(IntegerFilter day) {
        this.day = day;
    }

    public IntegerFilter getMonth() {
        return month;
    }

    public IntegerFilter month() {
        if (month == null) {
            month = new IntegerFilter();
        }
        return month;
    }

    public void setMonth(IntegerFilter month) {
        this.month = month;
    }

    public IntegerFilter getYear() {
        return year;
    }

    public IntegerFilter year() {
        if (year == null) {
            year = new IntegerFilter();
        }
        return year;
    }

    public void setYear(IntegerFilter year) {
        this.year = year;
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
        final TblOffDayCriteria that = (TblOffDayCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(day, that.day) &&
            Objects.equals(month, that.month) &&
            Objects.equals(year, that.year) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, day, month, year, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TblOffDayCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (day != null ? "day=" + day + ", " : "") +
            (month != null ? "month=" + month + ", " : "") +
            (year != null ? "year=" + year + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
