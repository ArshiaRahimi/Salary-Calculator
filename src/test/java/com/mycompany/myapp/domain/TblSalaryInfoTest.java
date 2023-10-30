package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TblSalaryInfoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TblSalaryInfo.class);
        TblSalaryInfo tblSalaryInfo1 = new TblSalaryInfo();
        tblSalaryInfo1.setId(1L);
        TblSalaryInfo tblSalaryInfo2 = new TblSalaryInfo();
        tblSalaryInfo2.setId(tblSalaryInfo1.getId());
        assertThat(tblSalaryInfo1).isEqualTo(tblSalaryInfo2);
        tblSalaryInfo2.setId(2L);
        assertThat(tblSalaryInfo1).isNotEqualTo(tblSalaryInfo2);
        tblSalaryInfo1.setId(null);
        assertThat(tblSalaryInfo1).isNotEqualTo(tblSalaryInfo2);
    }
}
