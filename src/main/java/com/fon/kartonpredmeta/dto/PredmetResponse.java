package com.fon.kartonpredmeta.dto;

import com.fon.kartonpredmeta.entity.TipPredmeta;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PredmetResponse {

    private Long id;
    private String naziv;
    private String sifra;
    private int espb;
    private int semestar;
    private int godinaStudija;
    private TipPredmeta tipPredmeta;
    private int brojCasovaPredavanja;
    private int brojCasovaVezbi;

}
