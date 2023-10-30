package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TblOffDayTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TblOffDay.class);
        TblOffDay tblOffDay1 = new TblOffDay();
        tblOffDay1.setId(1L);
        TblOffDay tblOffDay2 = new TblOffDay();
        tblOffDay2.setId(tblOffDay1.getId());
        assertThat(tblOffDay1).isEqualTo(tblOffDay2);
        tblOffDay2.setId(2L);
        assertThat(tblOffDay1).isNotEqualTo(tblOffDay2);
        tblOffDay1.setId(null);
        assertThat(tblOffDay1).isNotEqualTo(tblOffDay2);
    }
}
