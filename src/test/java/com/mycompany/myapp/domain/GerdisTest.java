package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GerdisTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Gerdis.class);
        Gerdis gerdis1 = new Gerdis();
        gerdis1.setId(1L);
        Gerdis gerdis2 = new Gerdis();
        gerdis2.setId(gerdis1.getId());
        assertThat(gerdis1).isEqualTo(gerdis2);
        gerdis2.setId(2L);
        assertThat(gerdis1).isNotEqualTo(gerdis2);
        gerdis1.setId(null);
        assertThat(gerdis1).isNotEqualTo(gerdis2);
    }
}
