package com.fon.kartonpredmeta.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "izvodjenje")
public class Izvodjenje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "oblik_nastave", nullable = false)
    private OblikNastave oblikNastave;


    @ManyToOne
    @JoinColumn(name = "predmet_id", nullable = false)
    private Predmet predmet;


    @ManyToOne
    @JoinColumn(name = "nastavnik_id", nullable = false)
    private Nastavnik nastavnik;


}
