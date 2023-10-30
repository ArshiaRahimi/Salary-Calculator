package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TblEmployeeAuthenticationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TblEmployeeAuthentication.class);
        TblEmployeeAuthentication tblEmployeeAuthentication1 = new TblEmployeeAuthentication();
        tblEmployeeAuthentication1.setId(1L);
        TblEmployeeAuthentication tblEmployeeAuthentication2 = new TblEmployeeAuthentication();
        tblEmployeeAuthentication2.setId(tblEmployeeAuthentication1.getId());
        assertThat(tblEmployeeAuthentication1).isEqualTo(tblEmployeeAuthentication2);
        tblEmployeeAuthentication2.setId(2L);
        assertThat(tblEmployeeAuthentication1).isNotEqualTo(tblEmployeeAuthentication2);
        tblEmployeeAuthentication1.setId(null);
        assertThat(tblEmployeeAuthentication1).isNotEqualTo(tblEmployeeAuthentication2);
    }
}
