package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ResolucionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ResolucionDTO.class);
        ResolucionDTO resolucionDTO1 = new ResolucionDTO();
        resolucionDTO1.setId(1L);
        ResolucionDTO resolucionDTO2 = new ResolucionDTO();
        assertThat(resolucionDTO1).isNotEqualTo(resolucionDTO2);
        resolucionDTO2.setId(resolucionDTO1.getId());
        assertThat(resolucionDTO1).isEqualTo(resolucionDTO2);
        resolucionDTO2.setId(2L);
        assertThat(resolucionDTO1).isNotEqualTo(resolucionDTO2);
        resolucionDTO1.setId(null);
        assertThat(resolucionDTO1).isNotEqualTo(resolucionDTO2);
    }
}
