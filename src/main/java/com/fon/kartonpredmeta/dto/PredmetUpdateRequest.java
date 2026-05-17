package com.fon.kartonpredmeta.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PredmetUpdateRequest {

    @Size(max = 50, message = "Naziv moze imati najvise 50 karkatera")
    private String naziv;

    @Size(max = 30, message = "Sifra moze imati najvise 30 karaktera")
    private String sifra;

    @Min(value = 1, message = "ESPB mora biti najmanje 1")
    @Max(value = 30, message = "ESPB moze biti najvise 30")
    private Integer espb;


    @Min(value = 0, message = "Broj casova predavanja ne moze biti negativan")
    @Max(value = 30, message = "Broj casova predavanja je prevelik")
    private Integer brojCasovaPredavanja;


    @Min(value = 0, message = "Broj casova vezbi ne moze biti negativan")
    @Max(value = 30, message = "Broj casova vezbi je prevelik")
    private Integer brojCasovaVezbi;

    @Min(value = 0, message = "Broj casova laboratorijskih vezbi ne moze biti negativan")
    @Max(value = 30, message = "Broj casova laboratorijskih vezbi je prevelik")
    private Integer brojCasovaLab;


    private String ciljPredmeta;
    private String sadrzajPredmeta;


    @Valid
    private List<LiteraturaDTO> literatura;


    private List<Long> ishodIds;

    @Valid
    private List<IzvodjenjeRequest> izvodjenja;

}
