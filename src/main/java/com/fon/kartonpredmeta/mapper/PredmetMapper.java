package com.fon.kartonpredmeta.mapper;


import com.fon.kartonpredmeta.dto.LiteraturaDTO;
import com.fon.kartonpredmeta.dto.PredmetCreateRequest;
import com.fon.kartonpredmeta.dto.PredmetResponse;
import com.fon.kartonpredmeta.dto.PredmetUpdateRequest;
import com.fon.kartonpredmeta.entity.Literatura;
import com.fon.kartonpredmeta.entity.Predmet;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PredmetMapper {

    @Mapping(target = "literatura", source = "literatura")
    PredmetResponse toResponse(Predmet predmet);

    @Mapping(target = "id", ignore = true)
    Predmet toEntity(PredmetCreateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "literatura", ignore = true)
    @Mapping(target = "id", ignore = true)
    void update(PredmetUpdateRequest request, @MappingTarget Predmet predmet);


    LiteraturaDTO toLiteraturaDTO(Literatura literatura);

    @Mapping(target = "id", ignore = true)
    Literatura toLiteratura(LiteraturaDTO literaturaDTO);

}
