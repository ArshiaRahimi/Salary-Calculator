package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TblWorkingHoursTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TblWorkingHours.class);
        TblWorkingHours tblWorkingHours1 = new TblWorkingHours();
        tblWorkingHours1.setId(1L);
        TblWorkingHours tblWorkingHours2 = new TblWorkingHours();
        tblWorkingHours2.setId(tblWorkingHours1.getId());
        assertThat(tblWorkingHours1).isEqualTo(tblWorkingHours2);
        tblWorkingHours2.setId(2L);
        assertThat(tblWorkingHours1).isNotEqualTo(tblWorkingHours2);
        tblWorkingHours1.setId(null);
        assertThat(tblWorkingHours1).isNotEqualTo(tblWorkingHours2);
    }
}
