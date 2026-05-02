package com.fon.kartonpredmeta.mapper;

import com.fon.kartonpredmeta.dto.NastavnikDTO;
import com.fon.kartonpredmeta.entity.Nastavnik;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface NastavnikMapper {

    NastavnikDTO toDTO(Nastavnik nastavnik);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "izvodjenja", ignore = true)
    Nastavnik toEntity(NastavnikDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "izvodjenja", ignore = true)
    void update(NastavnikDTO dto, @MappingTarget Nastavnik nastavnik);
}
