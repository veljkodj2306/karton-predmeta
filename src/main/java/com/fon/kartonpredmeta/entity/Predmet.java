package com.fon.kartonpredmeta.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "predmet")
public class Predmet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "naziv")
    private String naziv;

    @Column(name = "sifra")
    private String sifra;

    @Column(name = "espb")
    private int espb;

    @Column(name = "semestar")
    private int semestar;

    @Column(name = "godina_studija")
    private int godinaStudija;

    @Enumerated(EnumType.STRING)
    @Column(name = "tip_predmeta")
    private TipPredmeta tipPredmeta;

    @Column(name = "broj_casova_predavanja")
    private int brojCasovaPredavanja;

    @Column(name = "broj_casova_vezbi")
    private int brojCasovaVezbi;


}
