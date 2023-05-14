package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ResolucionMapperTest {

    private ResolucionMapper resolucionMapper;

    @BeforeEach
    public void setUp() {
        resolucionMapper = new ResolucionMapperImpl();
    }
}
