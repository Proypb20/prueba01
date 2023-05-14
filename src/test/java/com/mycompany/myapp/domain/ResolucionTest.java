package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ResolucionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Resolucion.class);
        Resolucion resolucion1 = new Resolucion();
        resolucion1.setId(1L);
        Resolucion resolucion2 = new Resolucion();
        resolucion2.setId(resolucion1.getId());
        assertThat(resolucion1).isEqualTo(resolucion2);
        resolucion2.setId(2L);
        assertThat(resolucion1).isNotEqualTo(resolucion2);
        resolucion1.setId(null);
        assertThat(resolucion1).isNotEqualTo(resolucion2);
    }
}
