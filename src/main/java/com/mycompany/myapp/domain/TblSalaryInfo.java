package com.mycompany.myapp.domain;

import java.io.Serializable;
import javax.persistence.*;

/**
 * A TblSalaryInfo.
 */
@Entity
@Table(name = "tbl_salary_info")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TblSalaryInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "employee_id")
    private Integer employeeId;

    @Column(name = "base_salary")
    private Integer baseSalary;

    @Column(name = "housing_rights")
    private Integer housingRights;

    @Column(name = "internet_rights")
    private Integer internetRights;

    @Column(name = "groceries_rights")
    private Integer groceriesRights;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TblSalaryInfo id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getEmployeeId() {
        return this.employeeId;
    }

    public TblSalaryInfo employeeId(Integer employeeId) {
        this.setEmployeeId(employeeId);
        return this;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getBaseSalary() {
        return this.baseSalary;
    }

    public TblSalaryInfo baseSalary(Integer baseSalary) {
        this.setBaseSalary(baseSalary);
        return this;
    }

    public void setBaseSalary(Integer baseSalary) {
        this.baseSalary = baseSalary;
    }

    public Integer getHousingRights() {
        return this.housingRights;
    }

    public TblSalaryInfo housingRights(Integer housingRights) {
        this.setHousingRights(housingRights);
        return this;
    }

    public void setHousingRights(Integer housingRights) {
        this.housingRights = housingRights;
    }

    public Integer getInternetRights() {
        return this.internetRights;
    }

    public TblSalaryInfo internetRights(Integer internetRights) {
        this.setInternetRights(internetRights);
        return this;
    }

    public void setInternetRights(Integer internetRights) {
        this.internetRights = internetRights;
    }

    public Integer getGroceriesRights() {
        return this.groceriesRights;
    }

    public TblSalaryInfo groceriesRights(Integer groceriesRights) {
        this.setGroceriesRights(groceriesRights);
        return this;
    }

    public void setGroceriesRights(Integer groceriesRights) {
        this.groceriesRights = groceriesRights;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TblSalaryInfo)) {
            return false;
        }
        return id != null && id.equals(((TblSalaryInfo) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TblSalaryInfo{" +
            "id=" + getId() +
            ", employeeId=" + getEmployeeId() +
            ", baseSalary=" + getBaseSalary() +
            ", housingRights=" + getHousingRights() +
            ", internetRights=" + getInternetRights() +
            ", groceriesRights=" + getGroceriesRights() +
            "}";
    }
}
