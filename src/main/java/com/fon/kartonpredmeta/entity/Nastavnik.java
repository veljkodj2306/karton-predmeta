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
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "nastavnik")
public class Nastavnik {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ime", nullable = false)
    private String ime;

    @Column(name = "prezime", nullable = false)
    private String prezime;

    @Column(name = "zvanje", nullable = false)
    private String zvanje;


    @OneToMany(mappedBy = "nastavnik")
    private List<Izvodjenje> izvodjenja = new ArrayList<>();
}
