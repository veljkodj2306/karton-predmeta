package com.fon.kartonpredmeta.mapper;


import com.fon.kartonpredmeta.dto.IshodDTO;
import com.fon.kartonpredmeta.entity.Ishod;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IshodMapper {

    IshodDTO toIshodDTO(Ishod ishod);

    @Mapping(target = "id", ignore = true)
    Ishod toEntity(IshodDTO ishodDTO);
}
