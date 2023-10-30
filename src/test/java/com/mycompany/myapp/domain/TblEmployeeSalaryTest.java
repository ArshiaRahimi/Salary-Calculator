package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TblEmployeeSalaryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TblEmployeeSalary.class);
        TblEmployeeSalary tblEmployeeSalary1 = new TblEmployeeSalary();
        tblEmployeeSalary1.setId(1L);
        TblEmployeeSalary tblEmployeeSalary2 = new TblEmployeeSalary();
        tblEmployeeSalary2.setId(tblEmployeeSalary1.getId());
        assertThat(tblEmployeeSalary1).isEqualTo(tblEmployeeSalary2);
        tblEmployeeSalary2.setId(2L);
        assertThat(tblEmployeeSalary1).isNotEqualTo(tblEmployeeSalary2);
        tblEmployeeSalary1.setId(null);
        assertThat(tblEmployeeSalary1).isNotEqualTo(tblEmployeeSalary2);
    }
}
