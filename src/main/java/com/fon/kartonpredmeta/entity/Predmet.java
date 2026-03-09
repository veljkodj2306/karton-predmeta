package com.fon.kartonpredmeta.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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

    @ElementCollection
    @CollectionTable(name = "predmet_nastavnici", joinColumns = @JoinColumn(name = "predmet_id"))
    @Column(name = "nastavnik")
    private List<String> nastavnici = new ArrayList<>();

    @Column(name = "cilj_predmeta")
    private String ciljPredmeta;

    @Column(name = "ishod_predmeta")
    private String ishodPredmeta;

    @Column(name = "sadrzaj_predmeta")
    private String sadrzajPredmeta;

    @ManyToMany
    @JoinTable(name = "predmet_literatura", joinColumns = @JoinColumn(name = "predmet_id"),
            inverseJoinColumns = @JoinColumn(name = "literatura_id"))
    private List<Literatura> literatura = new ArrayList<>();

}
