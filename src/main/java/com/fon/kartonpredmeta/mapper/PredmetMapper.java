package com.fon.kartonpredmeta.mapper;


import com.fon.kartonpredmeta.dto.PredmetCreateRequest;
import com.fon.kartonpredmeta.dto.PredmetResponse;
import com.fon.kartonpredmeta.dto.PredmetUpdateRequest;
import com.fon.kartonpredmeta.entity.Predmet;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PredmetMapper {

    PredmetResponse toResponse(Predmet predmet);

    @Mapping(target = "id", ignore = true)
    Predmet toEntity(PredmetCreateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void update(PredmetUpdateRequest request, @MappingTarget Predmet predmet);


}
