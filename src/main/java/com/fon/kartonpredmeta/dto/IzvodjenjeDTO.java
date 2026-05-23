package com.fon.kartonpredmeta.dto;

import com.fon.kartonpredmeta.entity.OblikNastave;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IzvodjenjeDTO {


    private Long id;
    private Long nastavnikId;
    private String ime;
    private String prezime;
    private String zvanje;
    private OblikNastave oblikNastave;
}
