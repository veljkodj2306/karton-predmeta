package com.fon.kartonpredmeta.dto;


import com.fon.kartonpredmeta.entity.TipPredmeta;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PredmetUpdateRequest {

    @Size(max = 255, message = "Naziv moze imati najvise 255 karkatera")
    private String naziv;

    @Size(max = 30, message = "Sifra moze imati najvise 30 karaktera")
    private String sifra;

    @Min(value = 1, message = "ESPB mora biti najmanje 1")
    @Max(value = 30, message = "ESPB moze biti najvise 30")
    private Integer espb;

    @Min(value = 1, message = "Semestar mora biti najmanje 1")
    @Max(value = 12, message = "Semestar moze biti najvise 12")
    private Integer semestar;

    @Min(value = 1, message = "Godina studija mora biti najmanje 1")
    @Max(value = 6, message = "Godina studija moze biti najvise 6")
    private Integer godinaStudija;

    private TipPredmeta tipPredmeta;

    @Min(value = 0, message = "Broj casova predavanja ne moze biti negativan")
    @Max(value = 30, message = "Broj casova predavanja je prevelik")
    private Integer brojCasovaPredavanja;


    @Min(value = 0, message = "Broj casova vezbi ne moze biti negativan")
    @Max(value = 30, message = "Broj casova vezbi je prevelik")
    private Integer brojCasovaVezbi;
}
