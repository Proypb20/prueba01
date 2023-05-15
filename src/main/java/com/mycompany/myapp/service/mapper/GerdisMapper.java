package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Gerdis;
import com.mycompany.myapp.service.dto.GerdisDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Gerdis} and its DTO {@link GerdisDTO}.
 */
@Mapper(componentModel = "spring")
public interface GerdisMapper extends EntityMapper<GerdisDTO, Gerdis> {}
