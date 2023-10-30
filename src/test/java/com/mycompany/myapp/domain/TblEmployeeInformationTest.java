package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TblEmployeeInformationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TblEmployeeInformation.class);
        TblEmployeeInformation tblEmployeeInformation1 = new TblEmployeeInformation();
        tblEmployeeInformation1.setId(1L);
        TblEmployeeInformation tblEmployeeInformation2 = new TblEmployeeInformation();
        tblEmployeeInformation2.setId(tblEmployeeInformation1.getId());
        assertThat(tblEmployeeInformation1).isEqualTo(tblEmployeeInformation2);
        tblEmployeeInformation2.setId(2L);
        assertThat(tblEmployeeInformation1).isNotEqualTo(tblEmployeeInformation2);
        tblEmployeeInformation1.setId(null);
        assertThat(tblEmployeeInformation1).isNotEqualTo(tblEmployeeInformation2);
    }
}
