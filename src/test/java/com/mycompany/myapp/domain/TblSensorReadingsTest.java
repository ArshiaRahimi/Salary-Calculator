package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TblSensorReadingsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TblSensorReadings.class);
        TblSensorReadings tblSensorReadings1 = new TblSensorReadings();
        tblSensorReadings1.setId(1L);
        TblSensorReadings tblSensorReadings2 = new TblSensorReadings();
        tblSensorReadings2.setId(tblSensorReadings1.getId());
        assertThat(tblSensorReadings1).isEqualTo(tblSensorReadings2);
        tblSensorReadings2.setId(2L);
        assertThat(tblSensorReadings1).isNotEqualTo(tblSensorReadings2);
        tblSensorReadings1.setId(null);
        assertThat(tblSensorReadings1).isNotEqualTo(tblSensorReadings2);
    }
}
