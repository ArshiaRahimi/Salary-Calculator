package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.TblSalaryInfo} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.TblSalaryInfoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tbl-salary-infos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TblSalaryInfoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter employeeId;

    private IntegerFilter baseSalary;

    private IntegerFilter housingRights;

    private IntegerFilter internetRights;

    private IntegerFilter groceriesRights;

    private Boolean distinct;

    public TblSalaryInfoCriteria() {}

    public TblSalaryInfoCriteria(TblSalaryInfoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.employeeId = other.employeeId == null ? null : other.employeeId.copy();
        this.baseSalary = other.baseSalary == null ? null : other.baseSalary.copy();
        this.housingRights = other.housingRights == null ? null : other.housingRights.copy();
        this.internetRights = other.internetRights == null ? null : other.internetRights.copy();
        this.groceriesRights = other.groceriesRights == null ? null : other.groceriesRights.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TblSalaryInfoCriteria copy() {
        return new TblSalaryInfoCriteria(this);
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

    public IntegerFilter getBaseSalary() {
        return baseSalary;
    }

    public IntegerFilter baseSalary() {
        if (baseSalary == null) {
            baseSalary = new IntegerFilter();
        }
        return baseSalary;
    }

    public void setBaseSalary(IntegerFilter baseSalary) {
        this.baseSalary = baseSalary;
    }

    public IntegerFilter getHousingRights() {
        return housingRights;
    }

    public IntegerFilter housingRights() {
        if (housingRights == null) {
            housingRights = new IntegerFilter();
        }
        return housingRights;
    }

    public void setHousingRights(IntegerFilter housingRights) {
        this.housingRights = housingRights;
    }

    public IntegerFilter getInternetRights() {
        return internetRights;
    }

    public IntegerFilter internetRights() {
        if (internetRights == null) {
            internetRights = new IntegerFilter();
        }
        return internetRights;
    }

    public void setInternetRights(IntegerFilter internetRights) {
        this.internetRights = internetRights;
    }

    public IntegerFilter getGroceriesRights() {
        return groceriesRights;
    }

    public IntegerFilter groceriesRights() {
        if (groceriesRights == null) {
            groceriesRights = new IntegerFilter();
        }
        return groceriesRights;
    }

    public void setGroceriesRights(IntegerFilter groceriesRights) {
        this.groceriesRights = groceriesRights;
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
        final TblSalaryInfoCriteria that = (TblSalaryInfoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(employeeId, that.employeeId) &&
            Objects.equals(baseSalary, that.baseSalary) &&
            Objects.equals(housingRights, that.housingRights) &&
            Objects.equals(internetRights, that.internetRights) &&
            Objects.equals(groceriesRights, that.groceriesRights) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, employeeId, baseSalary, housingRights, internetRights, groceriesRights, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TblSalaryInfoCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (employeeId != null ? "employeeId=" + employeeId + ", " : "") +
            (baseSalary != null ? "baseSalary=" + baseSalary + ", " : "") +
            (housingRights != null ? "housingRights=" + housingRights + ", " : "") +
            (internetRights != null ? "internetRights=" + internetRights + ", " : "") +
            (groceriesRights != null ? "groceriesRights=" + groceriesRights + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
