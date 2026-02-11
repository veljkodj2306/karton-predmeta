package com.fon.kartonpredmeta.dto;

import com.fon.kartonpredmeta.entity.TipPredmeta;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PredmetCreateRequest {

    @NotBlank(message = "Naziv je obavezan")
    @Size(max = 255, message = "Naziv moze imati najvise 255 karaktera")
    private String naziv;

    @NotBlank(message = "Sifra je obavezna")
    @Size(max = 30, message = "Sifra moze imati najvise 30 karaktera")
    private String sifra;

    @NotNull(message = "ESPB je obavezan")
    @Min(value = 1, message = "ESPB mora biti najmanje 1")
    @Max(value = 30, message = "ESPB ne moze biti veci od 30")
    private Integer espb;

    @NotNull(message = "Semestar je obavezan")
    @Min(value = 1, message = "Semestar mora biti najmanje 1")
    @Max(value = 12, message = "Semestar moze biti najvise 12")
    private Integer semestar;


    @NotNull(message = "Godina studija je obavezna")
    @Min(value = 1, message = "Godina studija mora biti najmanje 1")
    @Max(value = 6, message = "Godina studija moze biti najvise 6")
    private Integer godinaStudija;

    @NotNull(message = "Tip predmeta je obavezan (IZBORNI/OBAVEZAN)")
    private TipPredmeta tipPredmeta;

    @NotNull(message = "Broj casova predavanja je obavezan")
    @Min(value = 0, message = "Broj casova predavanja ne moze biti negativan")
    @Max(value = 30, message = "Broj casova predavanja je prevelik")
    private Integer brojCasovaPredavanja;


    @NotNull(message = "Broj casova vezbi je obavezan")
    @Min(value = 0, message = "Broj casova vezbi ne moze biti negativan")
    @Max(value = 30, message = "Broj casova vezbi je prevelik")
    private Integer brojCasovaVezbi;
}
