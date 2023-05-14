package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Resolucion;
import com.mycompany.myapp.service.dto.ResolucionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Resolucion} and its DTO {@link ResolucionDTO}.
 */
@Mapper(componentModel = "spring")
public interface ResolucionMapper extends EntityMapper<ResolucionDTO, Resolucion> {}
