package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ResofomDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ResofomDTO.class);
        ResofomDTO resofomDTO1 = new ResofomDTO();
        resofomDTO1.setId(1L);
        ResofomDTO resofomDTO2 = new ResofomDTO();
        assertThat(resofomDTO1).isNotEqualTo(resofomDTO2);
        resofomDTO2.setId(resofomDTO1.getId());
        assertThat(resofomDTO1).isEqualTo(resofomDTO2);
        resofomDTO2.setId(2L);
        assertThat(resofomDTO1).isNotEqualTo(resofomDTO2);
        resofomDTO1.setId(null);
        assertThat(resofomDTO1).isNotEqualTo(resofomDTO2);
    }
}
