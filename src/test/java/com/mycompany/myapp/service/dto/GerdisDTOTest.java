package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GerdisDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GerdisDTO.class);
        GerdisDTO gerdisDTO1 = new GerdisDTO();
        gerdisDTO1.setId(1L);
        GerdisDTO gerdisDTO2 = new GerdisDTO();
        assertThat(gerdisDTO1).isNotEqualTo(gerdisDTO2);
        gerdisDTO2.setId(gerdisDTO1.getId());
        assertThat(gerdisDTO1).isEqualTo(gerdisDTO2);
        gerdisDTO2.setId(2L);
        assertThat(gerdisDTO1).isNotEqualTo(gerdisDTO2);
        gerdisDTO1.setId(null);
        assertThat(gerdisDTO1).isNotEqualTo(gerdisDTO2);
    }
}
