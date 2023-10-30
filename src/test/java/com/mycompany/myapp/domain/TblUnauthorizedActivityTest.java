package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TblUnauthorizedActivityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TblUnauthorizedActivity.class);
        TblUnauthorizedActivity tblUnauthorizedActivity1 = new TblUnauthorizedActivity();
        tblUnauthorizedActivity1.setId(1L);
        TblUnauthorizedActivity tblUnauthorizedActivity2 = new TblUnauthorizedActivity();
        tblUnauthorizedActivity2.setId(tblUnauthorizedActivity1.getId());
        assertThat(tblUnauthorizedActivity1).isEqualTo(tblUnauthorizedActivity2);
        tblUnauthorizedActivity2.setId(2L);
        assertThat(tblUnauthorizedActivity1).isNotEqualTo(tblUnauthorizedActivity2);
        tblUnauthorizedActivity1.setId(null);
        assertThat(tblUnauthorizedActivity1).isNotEqualTo(tblUnauthorizedActivity2);
    }
}
