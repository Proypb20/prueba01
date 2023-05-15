package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ResofomTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Resofom.class);
        Resofom resofom1 = new Resofom();
        resofom1.setId(1L);
        Resofom resofom2 = new Resofom();
        resofom2.setId(resofom1.getId());
        assertThat(resofom1).isEqualTo(resofom2);
        resofom2.setId(2L);
        assertThat(resofom1).isNotEqualTo(resofom2);
        resofom1.setId(null);
        assertThat(resofom1).isNotEqualTo(resofom2);
    }
}
