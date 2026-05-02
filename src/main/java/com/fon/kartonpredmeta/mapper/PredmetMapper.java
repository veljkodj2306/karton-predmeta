package com.fon.kartonpredmeta.mapper;


import com.fon.kartonpredmeta.dto.*;
import com.fon.kartonpredmeta.entity.Ishod;
import com.fon.kartonpredmeta.entity.Izvodjenje;
import com.fon.kartonpredmeta.entity.Literatura;
import com.fon.kartonpredmeta.entity.Predmet;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PredmetMapper {

    @Mapping(target = "literatura", source = "literatura")
    @Mapping(target = "izvodjenja", source = "izvodjenja")
    PredmetResponse toResponse(Predmet predmet);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ishodi", ignore = true)
    @Mapping(target = "literatura", ignore = true)
    Predmet toEntity(PredmetCreateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "literatura", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ishodi", ignore = true)
    void update(PredmetUpdateRequest request, @MappingTarget Predmet predmet);


    LiteraturaDTO toLiteraturaDTO(Literatura literatura);

    @Mapping(target = "id", ignore = true)
    Literatura toLiteratura(LiteraturaDTO literaturaDTO);

    IshodDTO toIshodDTO(Ishod ishod);


    @Mapping(target = "nastavnikId", source = "nastavnik.id")
    @Mapping(target = "ime", source = "nastavnik.ime")
    @Mapping(target = "prezime", source = "nastavnik.prezime")
    @Mapping(target = "zvanje", source = "nastavnik.zvanje")
    @Mapping(target = "oblikNastave", source = "oblikNastave")
    IzvodjenjeDTO toIzvodjenjeDTO(Izvodjenje izvodjenje);

}
