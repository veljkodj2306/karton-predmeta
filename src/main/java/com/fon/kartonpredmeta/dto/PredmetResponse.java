package com.fon.kartonpredmeta.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PredmetResponse {

    private Long id;
    private String naziv;
    private String sifra;
    private int espb;
    private int brojCasovaPredavanja;
    private int brojCasovaVezbi;
    private int brojCasovaLab;

    private String ciljPredmeta;
    private String sadrzajPredmeta;

    private List<LiteraturaDTO> literatura;

    private List<IshodDTO> ishodi;

    private List<IzvodjenjeDTO> izvodjenja;

}
