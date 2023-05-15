package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Gerdis;
import com.mycompany.myapp.domain.Resofom;
import com.mycompany.myapp.domain.Resolucion;
import com.mycompany.myapp.service.dto.GerdisDTO;
import com.mycompany.myapp.service.dto.ResofomDTO;
import com.mycompany.myapp.service.dto.ResolucionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Resofom} and its DTO {@link ResofomDTO}.
 */
@Mapper(componentModel = "spring")
public interface ResofomMapper extends EntityMapper<ResofomDTO, Resofom> {
    @Mapping(target = "resolucion", source = "resolucion", qualifiedByName = "resolucionResolucion")
    @Mapping(target = "gerdis", source = "gerdis", qualifiedByName = "gerdisDescripcion")
    ResofomDTO toDto(Resofom s);

    @Named("resolucionResolucion")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "resolucion", source = "resolucion")
    ResolucionDTO toDtoResolucionResolucion(Resolucion resolucion);

    @Named("gerdisDescripcion")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "descripcion", source = "descripcion")
    GerdisDTO toDtoGerdisDescripcion(Gerdis gerdis);
}
