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


    @Column(name = "broj_casova_predavanja")
    private int brojCasovaPredavanja;

    @Column(name = "broj_casova_vezbi")
    private int brojCasovaVezbi;

    @Column(name = "broj_casova_lab")
    private int brojCasovaLab;

    @Column(name = "cilj_predmeta")
    private String ciljPredmeta;


    @Column(name = "sadrzaj_predmeta")
    private String sadrzajPredmeta;

    @ManyToMany
    @JoinTable(name = "predmet_literatura", joinColumns = @JoinColumn(name = "predmet_id"),
            inverseJoinColumns = @JoinColumn(name = "literatura_id"))
    private List<Literatura> literatura = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "predmet_ishod",
            joinColumns = @JoinColumn(name = "predmet_id"),
            inverseJoinColumns = @JoinColumn(name = "ishod_id"))
    private List<Ishod> ishodi = new ArrayList<>();

    @OneToMany(mappedBy = "predmet")
    private List<Izvodjenje> izvodjenja = new ArrayList<>();
}
