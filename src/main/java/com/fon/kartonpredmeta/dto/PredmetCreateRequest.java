package com.fon.kartonpredmeta.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PredmetCreateRequest {

    @NotBlank(message = "Naziv je obavezan")
    @Size(max = 50, message = "Naziv moze imati najvise 50 karaktera")
    private String naziv;

    @NotBlank(message = "Sifra je obavezna")
    @Size(max = 30, message = "Sifra moze imati najvise 30 karaktera")
    private String sifra;

    @NotNull(message = "ESPB je obavezan")
    @Min(value = 1, message = "ESPB mora biti najmanje 1")
    @Max(value = 30, message = "ESPB ne moze biti veci od 30")
    private Integer espb;


    @NotNull(message = "Broj casova predavanja je obavezan")
    @Min(value = 0, message = "Broj casova predavanja ne moze biti negativan")
    @Max(value = 30, message = "Broj casova predavanja je prevelik")
    private Integer brojCasovaPredavanja;


    @NotNull(message = "Broj casova vezbi je obavezan")
    @Min(value = 0, message = "Broj casova vezbi ne moze biti negativan")
    @Max(value = 30, message = "Broj casova vezbi je prevelik")
    private Integer brojCasovaVezbi;

    @NotNull(message = "Broj casova laboratorijskih vezbi je obavezan")
    @Min(value = 0, message = "Broj casova laboratorijskih vezbi ne moze biti negativan")
    @Max(value = 30, message = "Broj casova laboratorijskih vezbi je prevelik")
    private Integer brojCasovaLab;

    private String ciljPredmeta;
    private String sadrzajPredmeta;


    @Valid
    private List<LiteraturaDTO> literatura;

    @NotEmpty(message = "Mora imati bar jedan ishod")
    private List<Long> ishodIds;


    @NotEmpty(message = "Unesite bar jedno izvodjenje")
    @Valid
    private List<IzvodjenjeRequest> izvodjenja;
}
