package com.fon.kartonpredmeta.dto;

import com.fon.kartonpredmeta.entity.TipPredmeta;
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
    private int semestar;
    private int godinaStudija;
    private TipPredmeta tipPredmeta;
    private int brojCasovaPredavanja;
    private int brojCasovaVezbi;

    private List<String> nastavnici;
    private String ciljPredmeta;
    private String ishodPredmeta;
    private String sadrzajPredmeta;

    private List<LiteraturaDTO> literatura;

}
